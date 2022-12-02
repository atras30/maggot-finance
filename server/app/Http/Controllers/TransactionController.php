<?php

namespace App\Http\Controllers;

use App\Models\Notification;
use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class TransactionController extends Controller {
  public function index(Request $request) {
    $request->validate([
      "email" => "string|required"
    ]);

    $email = $request->email;

    $user = User::where("email", $email)->get()->first();

    $trashManager = null;
    if(!$user) {
      $trashManager = TrashManager::where("email", $email)->get()->first();
      if(!$trashManager) {
        return response()->json([
          "message" => "User was not found"
        ]);
      }
    }

    if (isset($trashManager) && $trashManager->role == "trash_manager") {
      $transactionHistory = Transaction::where("trash_manager_id", $trashManager->id)->where("transaction_type", "trash_manager_transaction")->orderBy("created_at", "desc")->get();
      return response()->json([
        "data" => $transactionHistory
      ]);
    } else if ($user->role == "farmer") {
      $transactionHistory = Transaction::where("farmer_id", $user->id)->where("transaction_type", "farmer_transaction")->orderBy("created_at", "desc")->get();
      return response()->json([
        "data" => $transactionHistory
      ]);
    }
  }

  public function approveFarmerWithdrawal(Request $request) {
    $validated = $request->validate([
        "token" => "string|required"
    ]);

    $notification = Notification::firstWhere("token", $validated['token']);

    if(!$notification) {
        return response()->json([
            "message" => "Notification was not found."
        ], Response::HTTP_NOT_FOUND);
    }

    $farmer = User::findOrFail($notification->farmer_id);

    try {
        $farmer->balance += $notification->withdrawal_amount;
        $notification->delete();
        $farmer->save();
    } catch (\Exception $e) {
        return response()->json([
            "message" => $e->getMessage()
        ], Response::HTTP_INTERNAL_SERVER_ERROR);
    }

    return response()->json([
        "message" => "Withdrawal Success."
    ], Response::HTTP_OK);
  }
}
