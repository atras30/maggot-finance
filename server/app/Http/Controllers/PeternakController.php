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
      "weight_in_kg" => "numeric|required",
      "amount_per_kg" => "numeric|required"
    ]);

    $validated["total_amount"] = $validated['weight_in_kg'] * $validated['amount_per_kg'];
    $farmer = User::find(auth()->user()->id);
    $collector = User::find($validated['related_transaction_user_id']);

    if($farmer->role == "pengepul") {
      return response()->json([
        "message" => "Hanya peternak yang boleh melakukan transaksi ini."
      ], Response::HTTP_OK);
    }

    $validated['user_id'] = auth()->user()->id;
    $validated['related_transaction_user_id'] = auth()->user()->id;
    
    $collector->balance -= $validated['total_amount'];
    
    $validated['user_id'] = $validated['related_transaction_user_id'];
    $validated['type'] = "expense";

    $createdTransaction = Transaction::create($validated);
    $createdTransaction = Transaction::create($validated);

    $farmer->save();
    $collector->save();

    return response()->json([
      "message" => $createdTransaction
    ]);
  }
}
