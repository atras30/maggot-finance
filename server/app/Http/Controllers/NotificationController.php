<?php

namespace App\Http\Controllers;

use App\Models\Notification;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Ramsey\Uuid\Uuid;

class NotificationController extends Controller
{
    public function getNotificationQueue(Request $request)
    {
        //Delete all expired token
        $this->deleteExpiredTokens();

        if (auth()->user()->role == 'trash_manager') {
            return response()->json([
                "message" => "Trash Manager dont have any use for this feature."
            ], Response::HTTP_OK);
        } else if (auth()->user()->role == 'farmer') {
            $notifications = Notification::where(
                'farmer_id',
                auth()->user()->id
            )->where("type", "farmer_withdrawal")->get();

            foreach ($notifications as $notification) {
                $notification->nama_peternak = $notification->farmer->full_name;
                $notification->nama_pengelola = $notification->trash_manager->nama_pengelola;
                unset($notification->farmer);
                unset($notification->trash_manager);
            }
        } else if (auth()->user()->role == 'shop') {
            $notifications = Notification::where(
                'farmer_id',
                auth()->user()->id
            )->get();
        }

        return response()->json(
            [
                'notifications' => $notifications,
            ],
            Response::HTTP_OK
        );
    }

    public function createFarmerWithdrawalNotification(Request $request)
    {
        $this->deleteExpiredTokens();

        if (auth()->user()->role != "trash_manager") {
            return response()->json([
                "message" => ucfirst(auth()->user()->role) . " have no access to this feature."
            ], Response::HTTP_UNAUTHORIZED);
        }

        $validated = $request->validate([
            'farmer_email' => 'string|required',
            "withdrawal_amount" => "numeric|required"
        ]);

        $validated['type'] = "farmer_withdrawal";

        try {
            $validated['farmer_id'] = User::firstWhere("email", $validated['farmer_email'])->id;
        } catch (\Exception $e) {
            return response()->json([
                "message" => "Failed to create payment request. User was not found"
            ]);
        }

        $validated['trash_manager_id'] = auth()->user()->id;
        $validated['expired_at'] = now()->addMinutes(5);
        $validated['token'] = Uuid::uuid4();

        try {
            Notification::create($validated);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'message' => $e->getMessage(),
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        return response()->json([
            'message' => 'Farmer\'s Withdrawal Notification Successfully Created.',
        ]);
    }

    public function createFarmerPaymentToShopNotification(Request $request)
    {
        $this->deleteExpiredTokens();

        if (auth()->user()->role != "farmer") {
            return response()->json([
                "message" => ucfirst(auth()->user()->role) . " have no access to this feature."
            ], Response::HTTP_UNAUTHORIZED);
        }

        $validated = $request->validate([
            'shop_email' => 'string|required',
            "total_amount" => "numeric|required"
        ]);

        $validated['type'] = "farmer_purchase";

        try {
            $validated['shop_id'] = User::firstWhere("email", $validated['shop_email'])->id;
        } catch (\Exception $e) {
            return response()->json([
                "message" => "Failed to create payment request. User was not found"
            ]);
        }

        $validated['farmer_id'] = auth()->user()->id;
        $validated['expired_at'] = now()->addMinutes(5);
        $validated['token'] = Uuid::uuid4();

        try {
            Notification::create($validated);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'message' => $e->getMessage(),
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        return response()->json([
            'message' => 'Farmer\'s Purchase Notification Successfully Created.',
        ]);
    }

    public function createBuyMaggotNotification(Request $request)
    {
        if (auth()->user()->role != "trash_manager") {
            return response()->json([
                "message" => ucfirst(auth()->user()->role) . " have no access to this feature."
            ], Response::HTTP_UNAUTHORIZED);
        }

        $validated = $request->validate([
            'description' => 'string',
            'weight_in_kg' => 'numeric|required',
            'amount_per_kg' => 'numeric|required',
            'farmer_id' => 'numeric|required',
        ]);

        $validated['type'] = "farmer_withdrawal";

        try {
            User::findOrFail($validated['farmer_id']);
        } catch (\Exception $e) {
            return response()->json([
                "message" => "Failed to fetch User was not found"
            ]);
        }

        $this->deleteExpiredTokens();

        $validated['trash_manager_id'] = auth()->user()->id;

        $validated['expired_at'] = now()->addMinutes();
        $validated['token'] = Uuid::uuid4();

        try {
            Notification::create($validated);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'message' => $e->getMessage(),
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        return response()->json([
            'message' => 'Farmer\'s Withdrawal Notification Successfully Created.',
        ]);
    }

    public function delete(Request $request)
    {
        $validated = $request->validate([
            'token' => 'string|required',
            'status' => 'string|required',
        ]);

        if (!in_array($validated['status'], ['confirm', 'cancel'])) {
            return response()->json([
                'message' => "status must be either 'confirm' or 'cancel'.",
            ]);
        }

        if ($validated['status'] == 'cancel') {
            return response()->json([
                'message' => 'Payment Cancelled.',
            ]);
        }

        //Delete all expired token
        $this->deleteExpiredTokens();

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

    public function deleteExpiredTokens()
    {
        foreach (Notification::where('expired_at', '<', now())->get()
            as $expiredNotification) {
            $expiredNotification->delete();
        }
    }
}
