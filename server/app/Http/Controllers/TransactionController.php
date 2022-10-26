<?php

namespace App\Http\Controllers;

use App\Models\Transaction;
use Illuminate\Http\Request;

class TransactionController extends Controller {
  public function index(Request $request) {
    if (auth()->user()->role == "trash_manager") {
      $transactionHistory = Transaction::where("trash_manager_id", auth()->user()->id)->where("transaction_type", "trash_manager_transaction")->get();
      return response()->json([
        "data" => $transactionHistory
      ]);
    } else if (auth()->user()->role == "farmer") {
      $transactionHistory = Transaction::where("farmer_id", auth()->user()->id)->where("transaction_type", "farmer_transaction")->get();
      return response()->json([
        "data" => $transactionHistory
      ]);
    }
  }
}
