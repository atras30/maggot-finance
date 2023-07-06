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
    /**
     * Get all transaction of user by email.
     *
     * @OA\Get(
     *    path="/api/transactions/{email}",
     *   tags={"Transaction"},
     *  summary="Get all transaction data by email",
     * description="Get all transaction data by email",
     * operationId="getAllTransaction",
     * security={{"sanctum":{}}},
     * @OA\Parameter(
     *   description="Email of user",
     *  in="path",
     * name="email",
     * required=true,
     * @OA\Schema(
     * type="string"
     * )
     * ),
     * @OA\Response(
     * response=200,
     * description="Success",
     * @OA\JsonContent(
     * @OA\Property(property="transactions", type="array", @OA\Items(type="object", ref="#/components/schemas/Transaction"))
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="Bad Request",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example={
     * "email": {"The email field is required."}}),
     * ),
     * ),
     * @OA\Response(
     * response=401,
     * description="Unauthenticated.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated."),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="User not found.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="User not found."),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Server Error"),
     * ),
     * ),
     * )
     */
    public function index($email)
    {
        // $request->validate([
        //     "email" => "string|required",
        // ]);

        // $email = $request->email;

        if (!$email) {
            return response()->json(
                [
                    "message" => "Email tidak boleh kosong",
                ],
                Response::HTTP_BAD_REQUEST
            );
        }

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

    /**
     * Approval for farmer withdrawal.
     *
     * @OA\Post(
     * path="/api/notifications/farmer/withdrawal/approval",
     * summary="Approval for farmer withdrawal",
     * description="Approval for farmer withdrawal",
     * operationId="approvalFarmerWithdrawal",
     * tags={"Notification"},
     * security={{"sanctum":{}}},
     * @OA\RequestBody(
     * required=true,
     * description="Approval for farmer withdrawal",
     * @OA\JsonContent(
     * required={"token", "approval_status"},
     * @OA\Property(property="token", type="string", example="token"),
     * @OA\Property(property="approval_status", type="boolean", example=true),
     * ),
     * ),
     * @OA\Response(
     * response=200,
     * description="Success",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Pencairan berhasil."),
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="Bad Request",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example={
     * "token": {"The token field is required."},
     * "approval_status": {"The approval status field is required."},})
     * ),
     * ),
     * @OA\Response(
     * response=401,
     * description="Unauthenticated",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated"),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="Not Found",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Token kadaluarsa."),
     * ),
     * ),
     * @OA\Response(
     * response=406,
     * description="Not Acceptable",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Saldo tidak cukup."),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Internal Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Internal Server Error")
     * ),
     * ),
     * )
     */
    public function approvalFarmerWithdrawal(Request $request)
    {
        try {
            $this->deleteExpiredTokens();
            $validated = $request->validate([
                "token" => "string|required",
                "approval_status" => "boolean|required",
            ]);
            $notification = Notification::firstWhere(
                "token",
                $validated["token"]
            );
            if (!$notification) {
                return response()->json(
                    [
                        "message" => "Token kadaluarsa.",
                    ],
                    Response::HTTP_NOT_FOUND
                );
            }
            if ($validated["approval_status"] == true) {
                $farmer = User::findOrFail($notification->farmer_id);
                if ($farmer->balance - $notification->withdrawal_amount < 0) {
                    return response()->json(
                        [
                            "message" => "Saldo tidak cukup.",
                        ],
                        Response::HTTP_NOT_ACCEPTABLE
                    );
                }
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
                return response()->json(
                    [
                        "message" => "Pencairan berhasil.",
                    ],
                    Response::HTTP_OK
                );
            } else {
                $notification->delete();
                return response()->json(
                    [
                        "message" => "Pencairan dibatalkan.",
                    ],
                    Response::HTTP_OK
                );
            }
        } catch (\Exception $e) {
            return response()->json(
                [
                    "message" => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
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
