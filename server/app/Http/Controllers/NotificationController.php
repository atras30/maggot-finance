<?php

namespace App\Http\Controllers;

use App\Models\Notification;
use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Ramsey\Uuid\Uuid;

class NotificationController extends Controller
{
    /**
     * Get all notification queue.
     * 
     * @OA\Get(
     *     path="/api/notifications",
     *    tags={"Notification"},
     *    summary="Get all notification queue",
     *      description="Get all notification queue",
     *     operationId="getNotificationQueue",
     *    security={{"sanctum":{}}},
     *   @OA\Response(
     *     response=200,
     *   description="Success",
     *  @OA\JsonContent(
     *   @OA\Property(property="notifications", type="array", @OA\Items(type="object", ref="")),
     * ),
     * ),
     *  @OA\Response(
     *  response=401,
     * description="Unauthenticated",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated"),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Server Error"),
     * ),
     * ),
     * ),
     * )
     */
    public function getNotificationQueue(Request $request)
    {
        //Delete all expired token
        $this->deleteExpiredTokens();

        if (auth()->user()->role == 'trash_manager') {
            return response()->json([
                "message" => "Pengelola bank sampah tidak punya akses ke fitur ini."
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
                'shop_id',
                auth()->user()->id
            )->get();
        } else {
            error_log(auth()->user());
        }

        return response()->json(
            [
                'notifications' => $notifications,
            ],
            Response::HTTP_OK
        );
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

    public function createFarmerWithdrawalNotification(Request $request)
    {
        $this->deleteExpiredTokens();

        if (auth()->user()->role != "trash_manager") {
            return response()->json([
                "message" => ucfirst(auth()->user()->role) . " tidak punya akses ke fitur ini."
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
                "message" => "User tidak ditemukan"
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

    public function createShopWithdrawalNotification(Request $request)
    {
        $this->deleteExpiredTokens();

        if (auth()->user()->role != "trash_manager") {
            return response()->json([
                "message" => ucfirst(auth()->user()->role) . " tidak punya akses ke fitur ini."
            ], Response::HTTP_UNAUTHORIZED);
        }

        $validated = $request->validate([
            'shop_email' => 'string|required',
            "withdrawal_amount" => "numeric|required"
        ]);

        $validated['type'] = "shop_withdrawal";

        try {
            $validated['shop_id'] = User::firstWhere("email", $validated['shop_email'])->id;
        } catch (\Exception $e) {
            return response()->json([
                "message" => "User tidak ditemukan."
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
            'message' => 'Permintaan penarikan uang warung berhasil dibuat.',
        ]);
    }

    public function approveShopWithdrawal(Request $request)
    {
        $this->deleteExpiredTokens();

        $validated = $request->validate([
            'token' => 'string|required',
        ]);

        $notification = Notification::firstWhere('token', $validated['token']);

        if (!$notification) {
            return response()->json(
                [
                    'message' => 'Token kadaluarsa.',
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $shop = User::findOrFail($notification->shop_id);

        if($shop->balance - $notification->withdrawal_amount < 0) {
            return response()->json([
                "message" => "Saldo tidak cukup"
            ], Response::HTTP_NOT_ACCEPTABLE);
        }

        try {
            $shop->balance -= $notification->withdrawal_amount;

            $shopTransactions = Transaction::create([
                'type' => "expense",
                'transaction_type' => 'shop_transaction',
                "description" => "Pencairan Uang",
                'total_amount' => $notification->withdrawal_amount,
                'shop_id' => $shop->id,
                'trash_manager_id' => $notification->trash_manager->id,
            ]);

            $trashManagerTransaction = Transaction::create([
                'type' => "expense",
                'transaction_type' => 'trash_manager_transaction',
                "description" => "Pencairan Uang Warung",
                'total_amount' => $notification->withdrawal_amount,
                'shop_id' => $shop->id,
                'trash_manager_id' => $notification->trash_manager->id,
            ]);

            $notification->delete();
            $shop->save();
        } catch (\Exception $e) {
            return response()->json(
                [
                    'message' => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }

        return response()->json(
            [
                'message' => 'Penarikan Uang Berhasil.',
            ],
            Response::HTTP_OK
        );
    }

    public function rejectShopWithdrawal(Request $request)
    {
        $this->deleteExpiredTokens();

        $validated = $request->validate([
            'token' => 'string|required',
        ]);

        $notification = Notification::firstWhere('token', $validated['token']);

        if (!$notification) {
            return response()->json(
                [
                    'message' => 'Token Kadaluarsa.',
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $notification->delete();

        return response()->json(
            [
                'message' => 'Penghapusan permintaan penarikan uang berhasil.',
            ],
            Response::HTTP_OK
        );
    }

    public function createBuyMaggotNotification(Request $request)
    {
        if (auth()->user()->role != "trash_manager") {
            return response()->json([
                "message" => ucfirst(auth()->user()->role) . " tidak punya akses ke fitur ini."
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
                "message" => "User tidak ditemukan."
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
            'message' => 'Notifikasi permintaan penarikan uang peternak berhasil dibuat.',
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
                'message' => 'Pembayaran dibatalkan',
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
                    'message' => 'Token tidak ditemukan.',
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
                    'message' => 'Transaksi gagal.',
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        return response()->json(
            [
                'message' => 'Pembayaran berhasil',
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
