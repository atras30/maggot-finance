<?php

namespace App\Http\Controllers;

use App\Models\Notification;
use App\Models\TrashManager;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Ramsey\Uuid\Uuid;

class NotificationController extends Controller
{
    public function create(Request $request)
    {
        $validated = $request->validate([
            'type' => 'string|required',
            'token' => 'string|required',
            'description' => 'string|required',
            'weight_in_kg' => 'numeric|required',
            'amount_per_kg' => 'numeric|required',
            'farmer_id' => 'numeric|required',
            'trash_manager_id' => 'numeric|required',
        ]);

        $validated['expired_at'] = now()->addMinutes(15);
        $validated['token'] = Uuid::uuid4();
        $notification = Notification::create($validated);

        if (!$notification) {
            return response()->json([
                'message' => 'Failed creating notification.',
            ], Response::HTTP_NOT_ACCEPTABLE);
        }

        return response()->json([
            'message' => 'Notification successfully created.',
        ]);
    }
    public function delete(Request $request)
    {
        $request->validate([
            'token' => 'string|required',
        ]);

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
