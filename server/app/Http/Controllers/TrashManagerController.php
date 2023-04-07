<?php

namespace App\Http\Controllers;

use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class TrashManagerController extends Controller
{
    public function index()
    {
        return response()->json(
            [
                'trash_managers' => TrashManager::all(),
            ],
            Response::HTTP_OK
        );
    }

    public function listUser(Request $request)
    {
        $validated = $request->validate([
            'email' => 'string|required',
        ]);
        return TrashManager::where('email', $validated['email'])
            ->get()
            ->first()->users;
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            "nama_pengelola" => "string|required|",
            "tempat" => "string|required|",
            "email" => "string|required|",
        ]);

        TrashManager::create($validated);

        return response()->json([
            "message" => "Super admin created successfully"
        ], Response::HTTP_CREATED);
    }

    public static function buyMaggot(Request $request)
    {
        $validated['description'] = "Pembelian Maggot Warga";
        $validated['weight_in_kg'] = $request->weight_in_kg;
        $validated['amount_per_kg'] = $request->amount_per_kg;
        $validated['farmer_email'] = $request->farmer_email;

        if (!isset($validated['description'])) {
            $validated['description'] = '';
        }

        //Transaction dari warga ke pengepul
        $warga = User::where('email', $validated['farmer_email'])
            ->get()
            ->first();

        $validated['farmer_id'] = $warga->id;
        $validated['trash_manager_id'] = auth()->user()->id;
        $validated['type'] = 'income';
        $validated['transaction_type'] = 'farmer_transaction';
        $validated['total_amount'] = $validated['weight_in_kg'] * $validated['amount_per_kg'];
        $warga->balance += $validated['total_amount'];

        $transaction1 = Transaction::create($validated);
        $warga->save();

        //Transaction dari pengepul ke warga
        $pengepulTransaction['trash_manager_id'] = $warga->trash_manager->id;
        $pengepulTransaction['farmer_id'] = $warga->id;
        $pengepulTransaction['description'] = $validated['description'];
        $pengepulTransaction['weight_in_kg'] = $validated['weight_in_kg'];
        $pengepulTransaction['amount_per_kg'] = $validated['amount_per_kg'];
        $pengepulTransaction['type'] = 'expense';
        $pengepulTransaction['transaction_type'] = 'trash_manager_transaction';
        $pengepulTransaction['total_amount'] =
            $validated['weight_in_kg'] * $validated['amount_per_kg'];

        $transaction2 = Transaction::create($pengepulTransaction);

        return [
            'message' => 'Transaksi Berhasil',
            'transaction1' => $transaction1,
            'transaction2' => $transaction2,
        ];
    }
}
