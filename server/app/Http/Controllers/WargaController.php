<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Response;

class WargaController extends Controller {
  public function sellMaggots(Request $request) {
    $validated = $request->validate([
      "description" => "string",
      "weight_in_kg" => "numeric|required",
      "amount_per_kg" => "numeric|required",
    ]);

    if(!isset($validated['description'])) {
      $validated['description'] = "";
    }

    //Transaction dari warga ke pengepul
    $userId = auth()->user()->id;
    $warga = User::find($userId);
    $validated["warga_id"] = $warga->id;
    $validated['pengelola_bank_sampah_id'] = $warga->trash_manager->id;
    $validated['type'] = "income";
    $validated['transaction_type'] = "transaksi_warga";
    $validated['total_amount'] = $validated['weight_in_kg'] * $validated['amount_per_kg'];
    $warga->balance += $validated['total_amount'];

    $transaction1 = Transaction::create($validated);
    $warga->save();
    
    //Transaction dari pengepul ke warga
    $pengepulTransaction['pengelola_bank_sampah_id'] = $warga->trash_manager->id;
    $pengepulTransaction['description'] = $validated['description'];
    $pengepulTransaction['weight_in_kg'] = $validated['weight_in_kg'];
    $pengepulTransaction['amount_per_kg'] = $validated['amount_per_kg'];
    $pengepulTransaction['warga_id'] = $warga->id;
    $pengepulTransaction['type'] = 'expense';
    $pengepulTransaction['transaction_type'] = "transaksi_pengelola";
    $pengepulTransaction['total_amount'] = $validated['weight_in_kg'] * $validated['amount_per_kg'];

    $transaction2 = Transaction::create($pengepulTransaction);

    return response()->json([
      "transaction1" => $transaction1,
      "transaction2" => $transaction2
    ]);
  }
}
