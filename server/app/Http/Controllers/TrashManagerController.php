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

    public function store(Request $request) {
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
}
