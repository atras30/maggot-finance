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
    public function createTransactionDummyDataForSpecificUser(
        $email,
        $transactionAmount
    ) {
        $farmer = User::where("email", $email)
            ->get()
            ->first();

        for ($i = 0; $i < $transactionAmount; $i++) {
            $weightInKg = fake()->randomFloat(2, 0.5, 10);
            $amountPerKg = fake()->numberBetween(3000, 10000);

            $farmerId = $farmer->id;

            $trashmanagers = TrashManager::all();
            $trashManagerId =
                $trashmanagers[
                    fake()->numberBetween(0, $trashmanagers->count() - 1)
                ]->id;

            $description = fake()->words(3, true);

            Transaction::create([
                "type" => "income",
                "description" => $description,
                "weight_in_kg" => $weightInKg,
                "amount_per_kg" => $amountPerKg,
                "total_amount" => $weightInKg * $amountPerKg,
                "farmer_id" => $farmerId,
                "trash_manager_id" => $trashManagerId,
                "transaction_type" => "farmer_transaction",
            ]);

            Transaction::create([
                "type" => "expense",
                "description" => $description,
                "weight_in_kg" => $weightInKg,
                "amount_per_kg" => $amountPerKg,
                "total_amount" => $weightInKg * $amountPerKg,
                "farmer_id" => $farmerId,
                "trash_manager_id" => $trashManagerId,
                "transaction_type" => "trash_manager_transaction",
            ]);
        }

        return response()->json([
            "message" => "{$transactionAmount} data was successfully created.",
        ]);
    }

    /**
     * Register user
     *
     * @OA\Post(
     *  path="/api/register/user",
     * tags={"Authentication"},
     * summary="Register user",
     * description="Register user as farmer or shop",
     * operationId="RegisterUser",
     * security={{"sacntum":{}}},
     * @OA\RequestBody(
     *   required=true,
     *  description="Register user",
     * @OA\JsonContent(
     * @OA\Property(property="full_name", type="string", example="John Doe"),
     * @OA\Property(property="email", type="string", example="john.doe@email.co.id"),
     * @OA\Property(property="role", type="string", example="farmer"),
     * @OA\Property(property="trash_manager_id", type="integer", example="1"),
     * @OA\Property(property="address", type="string", example="Jl. Jalan"),
     * @OA\Property(property="phone_number", type="string", example="08123456789"),
     * ),
     * ),
     * @OA\Response(
     * response=201,
     * description="User was successfully created.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="User was successfully created."),
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="Bad request.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="The given data was invalid."),
     * @OA\Property(property="errors", type="object", example={
     * "full_name": {"The full name field is required."},
     * "email": {"The email field is required."},
     * "role": {"The role field is required."},
     * "trash_manager_id": {"The trash manager id field is required."},
     * "address": {"The address field is required."},
     * "phone_number": {"The phone number field is required."},
     * }),
     * ),
     * ),
     * @OA\Response(
     * response=409,
     * description="User is already registered in our app.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="User is already registered in our app."),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Internal server error.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Internal server error."),
     * ),
     * ),
     * )
     *
     */
    public function registerUser(Request $request)
    {
        $validated = $request->validate(
            [
                "full_name" => "string|required",
                // "username" => "string|required|unique:users,username|not_in:pengepul,peternak,warung|alpha_dash",
                "email" =>
                    "string|required|email:rfc,dns|unique:users,email|unique:trash_managers,email",
                // "password" => "string|required",
                "role" => "string|required|in:farmer,shop",
                "trash_manager_id" => "numeric|required",
                "address" => "string|required",
                "phone_number" => "string|required",
            ],
            [
                "role.in" => "Role must be either 'farmer' or 'shop'",
            ]
        );

        // $validated['password'] = bcrypt($validated['password']);

        $user = User::where("email", $validated["email"])
            ->get()
            ->first();

        if ($user) {
            return response()->json(
                [
                    "message" => "user is already registered in our app.",
                ],
                Response::HTTP_OK
            );
        }

        try {
            $createdUser = User::create($validated);
            $trashManager = TrashManager::findOrFail(
                $validated["trash_manager_id"]
            );
            // Mail::to($trashManager->email)->send(
            //     new RequestUserRegistrationMail(
            //         $createdUser->full_name,
            //         $createdUser->role
            //     )
            // );
            // Mail::to($validated['email'])->send(
            //     new SendUserRegistrationMail($createdUser, $trashManager)
            // );
        } catch (\Exception $e) {
            return response()->json(
                [
                    "error" => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }

        return response()->json(
            [
                "message" => "User was successfully created.",
            ],
            Response::HTTP_CREATED
        );
    }

    public function approveUserRequest(Request $request)
    {
        $validated = $request->validate([
            "email" => "string|required",
        ]);

        try {
            $user = User::where("email", $validated["email"])
                ->get()
                ->first();
            $user->is_verified = 1;
            $user->save();
            // Mail::to($user->email)->send(
            //     new ApprovalUserRegistrationMail($user->full_name)
            // );
            return response()->json([
                "message" => "Approval request success.",
            ]);
        } catch (\Exception $e) {
            return response()->json(
                [
                    "message" => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }
    }

    public function rejectUserRequest(Request $request)
    {
        $validated = $request->validate([
            "email" => "string|required",
        ]);

        try {
            $user = User::where("email", $validated["email"])
                ->get()
                ->first();
            // Mail::to($user->email)->send(
            //     new RejectUserRegistrationMail($user->full_name)
            // );
            $user->delete();
            return response()->json([
                "message" => "Reject request success.",
            ]);
        } catch (\Exception $e) {
            return response()->json(
                [
                    "message" => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Approval User Request
     *
     * @OA\Post(
     *     path="/api/register/user/approval",
     *    tags={"Authentication"},
     *    summary="Approval for User Request",
     *   description="Approve User Request",
     *   operationId="approvalUserRequest",
     *  security={{"sanctum":{}}},
     *  @OA\RequestBody(
     *   required=true,
     *  description="Approval User Request",
     * @OA\JsonContent(
     * @OA\Property(property="email", type="string", example="farmer@magfin.id"),
     * @OA\Property(property="is_verified", type="boolean", example=true),
     * ),
     * ),
     * @OA\Response(
     * response=200,
     * description="Approval request success.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Approval request success."),
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="Bad request.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Bad request."),
     * ),
     * ),
     * @OA\Response(
     * response=401,
     * description="Unauthenticated.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated."),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Internal server error.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Internal server error."),
     * ),
     * ),
     * )
     */
    public function approvalUserRequest(Request $request)
    {
        try {
            $validated = $request->validate([
                "email" => "string|required",
                "is_verified" => "boolean|required",
            ]);
            $user = User::where("email", $validated["email"])
                ->get()
                ->first();

            if (!$validated["is_verified"] || !$user) {
                $user->delete();
                return response()->json([
                    "message" => "Reject request success.",
                ]);
            }

            $user->is_verified = 1;
            $user->save();
            return response()->json([
                "message" => "Approval request success.",
            ]);
        } catch (\Exception $e) {
            return response()->json(
                [
                    "message" => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }
    }
}
