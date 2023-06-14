<?php

namespace App\Http\Controllers;

use App\Models\Notification;
use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use \Log;

class TransactionController extends Controller
{
    public function index(Request $request)
    {
        $request->validate([
            "email" => "string|required",
        ]);

        $email = $request->email;

        $user = User::where("email", $email)
            ->get()
            ->first();

        $trashManager = null;
        if (!$user) {
            $trashManager = TrashManager::where("email", $email)
                ->get()
                ->first();
            if (!$trashManager) {
                return response()->json([
                    "message" => "User tidak ditemukan",
                ]);
            }
        }

        if (isset($trashManager) && $trashManager->role == "trash_manager") {
            $transactionHistory = Transaction::where(
                "trash_manager_id",
                $trashManager->id
            )
                ->where("transaction_type", "trash_manager_transaction")
                ->orderBy("created_at", "desc")
                ->get();
            return response()->json([
                "data" => $transactionHistory,
            ]);
        } elseif ($user->role == "farmer") {
            $transactionHistory = Transaction::where("farmer_id", $user->id)
                ->where("transaction_type", "farmer_transaction")
                ->orderBy("created_at", "desc")
                ->get();
            return response()->json([
                "data" => $transactionHistory,
            ]);
        } elseif ($user->role == "shop") {
            $transactionHistory = Transaction::where("shop_id", $user->id)
                ->where("transaction_type", "shop_transaction")
                ->orderBy("created_at", "desc")
                ->get();
            return response()->json([
                "data" => $transactionHistory,
            ]);
        }
    }

    public function approveFarmerWithdrawal(Request $request)
    {
        $this->deleteExpiredTokens();

        $validated = $request->validate([
            "token" => "string|required",
        ]);

        $notification = Notification::firstWhere("token", $validated["token"]);

        if (!$notification) {
            return response()->json(
                [
                    "message" => "Token kadaluarsa.",
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $farmer = User::findOrFail($notification->farmer_id);

        if ($farmer->balance - $notification->withdrawal_amount < 0) {
            return response()->json(
                [
                    "message" => "Saldo tidak cukup.",
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        try {
            $farmer->balance -= $notification->withdrawal_amount;

            $farmerTransactions = Transaction::create([
                "type" => "expense",
                "transaction_type" => "farmer_transaction",
                "description" => "Pencairan Uang",
                "total_amount" => $notification->withdrawal_amount,
                "farmer_id" => $farmer->id,
                "trash_manager_id" => $notification->trash_manager->id,
            ]);

            $trashManagerTransaction = Transaction::create([
                "type" => "expense",
                "transaction_type" => "trash_manager_transaction",
                "description" => "Pencairan Uang Peternak",
                "total_amount" => $notification->withdrawal_amount,
                "farmer_id" => $farmer->id,
                "trash_manager_id" => $notification->trash_manager->id,
            ]);

            $notification->delete();
            $farmer->save();
        } catch (\Exception $e) {
            return response()->json(
                [
                    "message" => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }

        return response()->json(
            [
                "message" => "Penarikan uang berhasil.",
            ],
            Response::HTTP_OK
        );
    }

    public function rejectFarmerWithdrawal(Request $request)
    {
        $this->deleteExpiredTokens();

        $validated = $request->validate([
            "token" => "string|required",
        ]);

        $notification = Notification::firstWhere("token", $validated["token"]);

        if (!$notification) {
            return response()->json(
                [
                    "message" => "Token kadaluarsa.",
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $notification->delete();

        return response()->json(
            [
                "message" => "Penghapusan permintaan uang berhasil.",
            ],
            Response::HTTP_OK
        );
    }

    public function rejectFarmerPurchase(Request $request)
    {
        $this->deleteExpiredTokens();

        $validated = $request->validate([
            "token" => "string|required",
        ]);

        $notification = Notification::firstWhere("token", $validated["token"]);

        if (!$notification) {
            return response()->json(
                [
                    "message" => "Token kadaluarsa.",
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $notification->delete();

        return response()->json(
            [
                "message" => "Permintaan berhasil dihapus.",
            ],
            Response::HTTP_OK
        );
    }

    public function deleteExpiredTokens()
    {
        foreach (
            Notification::where("expired_at", "<", now())->get()
            as $expiredNotification
        ) {
            $expiredNotification->delete();
        }
    }
}
