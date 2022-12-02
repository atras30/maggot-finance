<?php

namespace App\Http\Controllers;

use App\Models\Notification;
use App\Models\TrashManager;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Ramsey\Uuid\Uuid;

class NotificationController extends Controller
{
    public function getNotificationQueue(Request $request) {
        if(auth()->user()->role == "trash_manager") {
            $notifications = Notification::where("trash_manager_id", auth()->user()->id)->get();
        } else if(auth()->user()->role == "farmer") {
            $notifications = Notification::where("farmer_id", auth()->user()->id)->get();
        }

        return response()->json([
            "notifications" => $notifications
        ], Response::HTTP_OK);
    }

    public function create(Request $request)
    {
        $validated = $request->validate([
            'type' => 'string|required',
            'description' => 'string',
            'weight_in_kg' => 'numeric|required',
            'amount_per_kg' => 'numeric|required',
            'farmer_id' => 'numeric|required',
        ]);

        if(!in_array($validated['type'], ["payment_confirmation"])) {
            return response()->json([
                "message" => "Type must be 'payment_confirmation'"
            ], Response::HTTP_NOT_ACCEPTABLE);
        }

        $validated['trash_manager_id'] = auth()->user()->id;

        $validated['expired_at'] = now()->addMinutes(15);
        $validated['token'] = Uuid::uuid4();

        try {
            Notification::create($validated);
        } catch(\Exception $e) {
            return response()->json([
                'message' => $e->getMessage(),
            ], Response::HTTP_NOT_ACCEPTABLE);
        }

        return response()->json([
            'message' => 'Notification successfully created.',
        ]);
    }

    public function delete(Request $request)
    {
        $validated = $request->validate([
            'token' => 'string|required',
            'status' => "string|required"
        ]);

        if(!in_array($validated['status'], ["confirm", "cancel"])) {
            return response()->json([
                "message" => "status must be either 'confirm' or 'cancel'."
            ]);
        }

        if($validated['status'] == "cancel") {
            return response()->json([
                "message" => "Payment Cancelled."
            ]);
        }

        //Delete all expired token
        foreach (
            Notification::where('expired_at', '<', now())->get()
            as $expiredNotification
        ) {
            $expiredNotification->delete();
        }

        //select related token
        $notification = Notification::where('token', $request->token)
            ->get()
            ->first();

        // Token was not found, return error.
        if (!$notification) {
            return response()->json(
                [
                    'message' => 'Token was not found.',
                ],
                Response::HTTP_OK
            );
        }

        // Token found, create Transaction
        $result = TrashManager::buyMaggot(
            $notification->description,
            $notification->weight_in_kg,
            $notification->amount_per_kg,
            $notification->farmer->email
        );
        $notification->delete();

        if ($result['message'] != 'Transaksi Berhasil') {
            return response()->json(
                [
                    'message' => 'Payment Failed.',
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        return response()->json(
            [
                'message' => 'Payment successfull',
            ],
            Response::HTTP_OK
        );
    }
}
