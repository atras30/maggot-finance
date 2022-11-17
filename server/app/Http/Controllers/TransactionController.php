<?php

namespace App\Http\Controllers;

use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;

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
}
