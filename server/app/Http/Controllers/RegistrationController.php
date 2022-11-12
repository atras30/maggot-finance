<?php

namespace App\Http\Controllers;

use App\Mail\ApprovalUserRegistrationMail;
use App\Mail\RejectUserRegistrationMail;
use App\Mail\RequestUserRegistrationMail;
use App\Mail\SendUserRegistrationMail;
use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;
use Illuminate\Http\Response;

class RegistrationController extends Controller
{
    public function createTransactionDummyDataAtras() {
        for($i = 0; $i < 50; $i++) {
            $weightInKg = fake()->randomFloat(2, 0.5, 10);
            $amountPerKg = fake()->numberBetween(3000, 10000);

            $farmerId = 504;

            $trashmanagers = TrashManager::all();
            $trashManagerId = $trashmanagers[fake()->numberBetween(0, $trashmanagers->count()-1)]->id;

            $description = fake()->words(3, true);

            Transaction::create([
                'type' => 'income',
                'description' => $description,
                'weight_in_kg' => $weightInKg,
                'amount_per_kg' => $amountPerKg,
                'total_amount' => $weightInKg * $amountPerKg,
                'farmer_id' => $farmerId,
                'trash_manager_id' => $trashManagerId,
                'transaction_type' => 'farmer_transaction'
            ]);

            Transaction::create([
                'type' => 'expense',
                'description' => $description,
                'weight_in_kg' => $weightInKg,
                'amount_per_kg' => $amountPerKg,
                'total_amount' => $weightInKg * $amountPerKg,
                'farmer_id' => $farmerId,
                'trash_manager_id' => $trashManagerId,
                'transaction_type' => 'trash_manager_transaction'
            ]);
        }

        return response()->json([
            "message" => "200 data was successfully created."
        ]);
    }

    public function createDummyData()
    {
        $trashManagers = TrashManager::all();
        $maxTrashManagerIndex = $trashManagers->count() - 1;

        for ($i = 0; $i < 500; $i++) {
            User::create([
                'email' => fake()
                    ->unique()
                    ->safeEmail(),
                'full_name' => fake()->name(),
                'role' => fake()->randomElement(['farmer', 'shop']),
                'balance' => fake()->randomNumber(5, true),
                'phone_number' => fake()->phoneNumber(),
                'address' => fake()->address(),
                'trash_manager_id' =>
                    $trashManagers[
                        fake()->numberBetween(0, $maxTrashManagerIndex)
                    ]->id,
            ]);
        }

        return response()->json([
            'message' => '1000 user successfully created.',
        ]);
    }

    public function createTransactionDummyData() {
        for($i = 0; $i < 200; $i++) {
            $weightInKg = fake()->randomFloat(2, 0.5, 10);
            $amountPerKg = fake()->numberBetween(3000, 10000);

            $users = User::all();
            $farmerId = $users[fake()->numberBetween(0, $users->count()-1)]->id;

            $trashmanagers = TrashManager::all();
            $trashManagerId = $trashmanagers[fake()->numberBetween(0, $trashmanagers->count()-1)]->id;

            $description = fake()->words(3, true);

            Transaction::create([
                'type' => 'income',
                'description' => $description,
                'weight_in_kg' => $weightInKg,
                'amount_per_kg' => $amountPerKg,
                'total_amount' => $weightInKg * $amountPerKg,
                'farmer_id' => $farmerId,
                'trash_manager_id' => $trashManagerId,
                'transaction_type' => 'farmer_transaction'
            ]);

            Transaction::create([
                'type' => 'expense',
                'description' => $description,
                'weight_in_kg' => $weightInKg,
                'amount_per_kg' => $amountPerKg,
                'total_amount' => $weightInKg * $amountPerKg,
                'farmer_id' => $farmerId,
                'trash_manager_id' => $trashManagerId,
                'transaction_type' => 'trash_manager_transaction'
            ]);
        }

        return response()->json([
            "message" => "200 data was successfully created."
        ]);
    }

    public function registerUser(Request $request)
    {
        $validated = $request->validate(
            [
                'full_name' => 'string|required',
                // "username" => "string|required|unique:users,username|not_in:pengepul,peternak,warung|alpha_dash",
                'email' => 'string|required|email:rfc,dns|unique:users,email',
                // "password" => "string|required",
                'role' => 'string|required|in:farmer,shop',
                'trash_manager_id' => 'numeric|required',
                'address' => 'string|required',
                'phone_number' => 'string|required',
            ],
            [
                'role.in' => "Role must be either 'farmer' or 'shop'",
            ]
        );

        // $validated['password'] = bcrypt($validated['password']);

        $user = User::where('email', $validated['email'])
            ->get()
            ->first();

        if ($user) {
            return response()->json(
                [
                    'message' => 'user is already registered in our app.',
                ],
                Response::HTTP_OK
            );
        }

        try {
            $createdUser = User::create($validated);
            $trashManager = TrashManager::findOrFail(
                $validated['trash_manager_id']
            );
            Mail::to($trashManager->email)->send(
                new RequestUserRegistrationMail(
                    $createdUser->full_name,
                    $createdUser->role
                )
            );
            Mail::to($validated['email'])->send(
                new SendUserRegistrationMail($createdUser, $trashManager)
            );
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
                'message' => 'User was successfully created.',
            ],
            Response::HTTP_CREATED
        );
    }

    public function approveUserRequest(Request $request)
    {
        $validated = $request->validate([
            'email' => 'string|required',
        ]);

        try {
            $user = User::where('email', $validated['email'])
                ->get()
                ->first();
            $user->is_verified = 1;
            $user->save();
            Mail::to($user->email)->send(
                new ApprovalUserRegistrationMail($user->full_name)
            );
            return response()->json([
                'message' => 'Approval request success.',
            ]);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'message' => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }
    }

    public function rejectUserRequest(Request $request)
    {
        $validated = $request->validate([
            'email' => 'string|required',
        ]);

        try {
            $user = User::where('email', $validated['email'])
                ->get()
                ->first();
            Mail::to($user->email)->send(
                new RejectUserRegistrationMail($user->full_name)
            );
            $user->delete();
            return response()->json([
                'message' => 'Reject request success.',
            ]);
        } catch (\Exception $e) {
            return response()->json(
                [
                    'message' => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }
    }

    public function approveTrashManagerRequest()
    {
    }

    public function rejectTrashManagerRequest()
    {
    }
}
