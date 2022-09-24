<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Transaction;
use App\Models\User;
use Illuminate\Http\Response;

class PeternakController extends Controller {
  public function sellMaggots(Request $request) {
    $validated = $request->validate([
      "related_transaction_user_id" => "numeric|required",
      "amount" => "min:0|required|numeric",
      "type" => "in:income,expense|required|string",
      "description" => "string|required",
      "weight" => "numeric|required"
    ]);

    $farmer = User::find(auth()->user()->id);
    $collector = User::find($validated['related_transaction_user_id']);

    if($collector->balance - $validated['amount'] < 0) {
      return response()->json([
        "message" => "Seller's balance is not sufficent."
      ], Response::HTTP_OK);
    }

    $validated['user_id'] = auth()->user()->id;
    $farmer->balance += $validated['amount'];
    $createdTransaction = Transaction::create($validated);
    $farmer->save();

    $collector->balance -= $validated['amount'];
    $validated['user_id'] = $validated['related_transaction_user_id'];
    $validated['related_transaction_user_id'] = auth()->user()->id;
    $validated['type'] = "expense";
    $createdTransaction = Transaction::create($validated);
    $collector->save();

    return response()->json([
      "message" => $createdTransaction
    ]);
  }
}
