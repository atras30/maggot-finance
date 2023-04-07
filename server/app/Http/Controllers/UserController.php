<?php

namespace App\Http\Controllers;

use App\Models\Transaction;
use App\Models\TrashManager;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use App\Models\User;
use Illuminate\Support\Facades\DB;

class UserController extends Controller
{
    public function destroy($id) {
        $user = User::findOrFail($id);

        try {
            $user->delete();
        } catch(\Exception $e) {
            return response()->json(
                [
                    'message' => $e->getMessage(),
                ],
                Response::HTTP_OK
            );
        }

        return response()->json(
            [
                'message' => "User berhasil dihapus.",
            ],
            Response::HTTP_OK
        );
    }

    public function index()
    {
        return response()->json(
            [
                'users' => User::all(),
            ],
            Response::HTTP_OK
        );
    }

    public function unauthenticatedUser()
    {
        return response()->json(
            [
                'data' => User::where('email_verified_at', null)
                    ->where('role', 'farmer')
                    ->get(),
            ],
            Response::HTTP_OK
        );
    }

    public function buyFromShop(Request $request)
    {
        $validated = $request->validate([
            'total_amount' => 'numeric|required|min:1',
            'shop_email' => 'string|required',
        ]);

        $user = auth()->user();
        if((int) $user->balance - (int) $validated['total_amount'] < 0) {
            return response()->json([
                "message" => "Transaksi Gagal, Saldo Anda Tidak Cukup!"
            ], Response::HTTP_UNPROCESSABLE_ENTITY);
        }

        $user->balance -= $validated['total_amount'];

        $shop = User::where("email", $validated['shop_email'])->get()->first();
        $shop->balance += $validated['total_amount'];

        $data = [
            "validated" => $validated,
            "user" => $user,
            "shop" => $shop
        ];

        DB::transaction(function () use($data) {
            $data['user']->save();
            $data['shop']->save();

            Transaction::create([
                'type' => 'expense',
                'description' => "Belanja Sembako",
                'weight_in_kg' => null,
                'amount_per_kg' => null,
                'total_amount' => $data['validated']['total_amount'],
                'farmer_id' => $data['user']->id,
                'trash_manager_id' => null,
                'shop_id' => $data['shop']->id,
                'transaction_type' => 'farmer_transaction',
            ]);

            Transaction::create([
                'type' => 'income',
                'description' => "Belanja Sembako",
                'weight_in_kg' => null,
                'amount_per_kg' => null,
                'total_amount' => $data['validated']['total_amount'],
                'farmer_id' => $data['user']->id,
                'trash_manager_id' => null,
                'shop_id' => $data['shop']->id,
                'transaction_type' => 'shop_transaction',
            ]);
        });

        return response()->json([
            "message" => "Transaksi berhasil dibuat."
        ], Response::HTTP_CREATED);
    }

    public function getUserByEmail($email)
    {
        $user = User::where('email', $email)->get()->first();

        if (!$user->count()) {
            return response()->json(
                [
                    'message' => "User {$email} tidak ditemukan.",
                ],
                Response::HTTP_OK
            );
        }

        return response()->json(
            [
                'user' => $user,
            ],
            Response::HTTP_OK
        );
    }

    public function getUserByRole($role)
    {
        $validRoles = ['shop', 'farmer'];

        if (!in_array($role, $validRoles)) {
            return response()->json(
                [
                    'message' => "Invalid Role! Role must be either 'farmer', or 'shop'",
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        $users = User::where('role', $role)->get();
        foreach($users as $user) {
            $user->nama_pengelola = TrashManager::find($user->trash_manager_id)->nama_pengelola;
        }

        return response()->json(
            [
                'total_record' => $users->count(),
                'users' => $users,
            ],
            Response::HTTP_OK
        );
    }

    public function edit(Request $request, $id)
    {
        $user = User::findOrFail($id);

        $validated = $request->validate([
            'full_name' => 'string',
            'username' => "string|unique:users,username,{$id}",
            'email' => "string|unique:users,email,{$id}",
            'password' => 'string',
            'role' => 'string|in:pengepul,peternak,warung',
        ]);

        if (isset($validated['password'])) {
            $validated['password'] = bcrypt($request->password);
        }

        try {
            $user->update($validated);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'error' => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }

        return response()->json(
            [
                'message' => 'User berhasil dibuat.',
            ],
            Response::HTTP_INTERNAL_SERVER_ERROR
        );
    }
}
