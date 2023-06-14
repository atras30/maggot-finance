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
    public function destroy($id)
    {
        $user = User::findOrFail($id);

        try {
            $user->delete();
        } catch (\Exception $e) {
            return response()->json(
                [
                    "message" => $e->getMessage(),
                ],
                Response::HTTP_OK
            );
        }

        return response()->json(
            [
                "message" => "User berhasil dihapus.",
            ],
            Response::HTTP_OK
        );
    }

    /**
     * Displaying all users
     *
     * @OA\Get(
     *      path="/api/user",
     *      tags={"Super Admin"},
     *      summary="Displaying all users",
     *      description="Displaying all users",
     *      operationId="getAllUsers",
     *      @OA\Response(
     *         response=200,
     *        description="Success",
     *       @OA\JsonContent(
     *         @OA\Property(property="users", type="array", @OA\Items(ref="#/components/schemas/User")),
     *      ),
     *    ),
     * ),
     */
    public function index()
    {
        return response()->json(
            [
                "users" => User::all(),
            ],
            Response::HTTP_OK
        );
    }

    public function unauthenticatedUser()
    {
        return response()->json(
            [
                "data" => User::where("email_verified_at", null)
                    ->where("role", "farmer")
                    ->get(),
            ],
            Response::HTTP_OK
        );
    }

    /**
     * Display a list of user by trash manager.
     *
     * @OA\Post(
     *     path="/api/farmer/buy/shop",
     *    tags={"Farmer and Shop"},
     *    summary="Create buying on shop transactions",
     *   description="Create buying on shop transactions",
     *   operationId="buyShop",
     *  security={{"sanctum":{}}},
     * @OA\RequestBody(
     *   required=true,
     * @OA\JsonContent(
     *    @OA\Property(property="total_amount", type="integer", example="10000"),
     *   @OA\Property(property="shop_email", type="string", example="warungsukamundur@magfin.id"),
     * ),
     * ),
     *  @OA\Response(
     *    response=200,
     *   description="Success",
     *  @OA\JsonContent(
     *        @OA\Property(property="message", type="string", example="Transaksi Berhasil!"),
     *  ),
     * ),
     * @OA\Response(
     *  response=422,
     * description="Unprocessable Entity",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Transaksi Gagal, Saldo Anda Tidak Cukup!")
     * ),
     * ),
     * @OA\Response(
     *   response=401,
     *  description="Unauthenticated",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated")
     * ),
     * ),
     * @OA\Response(
     *  response=403,
     * description="Forbidden",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Forbidden")
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="Not Found",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Not Found")
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Internal Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Internal Server Error")
     * ),
     * ),
     * )
     */
    public function buyFromShop(Request $request)
    {
        $validated = $request->validate([
            "total_amount" => "numeric|required|min:1",
            "shop_email" => "string|required",
        ]);

        $user = auth()->user();
        if ((int) $user->balance - (int) $validated["total_amount"] < 0) {
            return response()->json(
                [
                    "message" => "Transaksi Gagal, Saldo Anda Tidak Cukup!",
                ],
                Response::HTTP_UNPROCESSABLE_ENTITY
            );
        }

        $user->balance -= $validated["total_amount"];

        $shop = User::where("email", $validated["shop_email"])
            ->get()
            ->first();
        $shop->balance += $validated["total_amount"];

        $data = [
            "validated" => $validated,
            "user" => $user,
            "shop" => $shop,
        ];

        DB::transaction(function () use ($data) {
            $data["user"]->save();
            $data["shop"]->save();

            Transaction::create([
                "type" => "expense",
                "description" => "Belanja Sembako",
                "weight_in_kg" => null,
                "amount_per_kg" => null,
                "total_amount" => $data["validated"]["total_amount"],
                "farmer_id" => $data["user"]->id,
                "trash_manager_id" => null,
                "shop_id" => $data["shop"]->id,
                "transaction_type" => "farmer_transaction",
            ]);

            Transaction::create([
                "type" => "income",
                "description" => "Belanja Sembako",
                "weight_in_kg" => null,
                "amount_per_kg" => null,
                "total_amount" => $data["validated"]["total_amount"],
                "farmer_id" => $data["user"]->id,
                "trash_manager_id" => null,
                "shop_id" => $data["shop"]->id,
                "transaction_type" => "shop_transaction",
            ]);
        });

        return response()->json(
            [
                "message" => "Transaksi berhasil dibuat.",
            ],
            Response::HTTP_CREATED
        );
    }

    /**
     * Get user data by email.
     *
     * @OA\Get(
     *     path="/user/email/{email}",
     *    tags={"Trash Manager"},
     *    summary="Get user data by email",
     *   description="Get user data by email",
     *   operationId="getUserDataByEmail",
     *  security={{"sanctum":{}}},
     * @OA\Parameter(
     *   description="Email of user",
     *  in="path",
     *  name="email",
     * required=true,
     * example="farmer1@magfin.id"
     * ),
     *  @OA\Response(
     *    response=200,
     *   description="Success",
     *  @OA\JsonContent(ref="#/components/schemas/User"),
     * ),
     * @OA\Response(
     *   response=401,
     *  description="Unauthenticated",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated")
     * ),
     * ),
     * @OA\Response(
     *  response=403,
     * description="Forbidden",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Forbidden")
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="Not Found",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Not Found")
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Internal Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Internal Server Error")
     * ),
     * ),
     * )
     */
    public function getUserByEmail($email)
    {
        $user = User::where("email", $email)
            ->get()
            ->first();

        if (!$user->count()) {
            return response()->json(
                [
                    "message" => "User {$email} tidak ditemukan.",
                ],
                Response::HTTP_OK
            );
        }

        return response()->json(
            [
                "user" => $user,
            ],
            Response::HTTP_OK
        );
    }

    /**
     * Get user data by email.
     *
     * @OA\Get(
     *     path="/user/role/{role}",
     *    tags={"Super Admin"},
     *    summary="Get user data by role",
     *   description="Get user data by role",
     *   operationId="getUserDataByRole",
     *  security={{"sanctum":{}}},
     * @OA\Parameter(
     *   description="Role of user",
     *  in="path",
     *  name="role",
     * required=true,
     * example="farmer"
     * ),
     *  @OA\Response(
     *    response=200,
     *   description="Success",
     *  @OA\JsonContent(
     *       @OA\Property(property="total_records", type="integer", example=1),
     *       @OA\Property(property="users", type="array", @OA\Items(ref="#/components/schemas/User")),
     *  ),
     * ),
     * @OA\Response(
     *   response=401,
     *  description="Unauthenticated",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated")
     * ),
     * ),
     * @OA\Response(
     *  response=403,
     * description="Forbidden",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Forbidden")
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="Not Found",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Not Found")
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Internal Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Internal Server Error")
     * ),
     * ),
     * )
     */
    public function getUserByRole($role)
    {
        $validRoles = ["shop", "farmer"];

        if (!in_array($role, $validRoles)) {
            return response()->json(
                [
                    "message" =>
                        "Invalid Role! Role must be either 'farmer', or 'shop'",
                ],
                Response::HTTP_NOT_ACCEPTABLE
            );
        }

        $users = User::where("role", $role)->get();
        foreach ($users as $user) {
            $user->nama_pengelola = TrashManager::find(
                $user->trash_manager_id
            )->nama_pengelola;
        }

        return response()->json(
            [
                "total_record" => $users->count(),
                "users" => $users,
            ],
            Response::HTTP_OK
        );
    }

    public function edit(Request $request, $id)
    {
        $user = User::findOrFail($id);

        $validated = $request->validate([
            "full_name" => "string",
            "username" => "string|unique:users,username,{$id}",
            "email" => "string|unique:users,email,{$id}",
            "password" => "string",
            "role" => "string|in:pengepul,peternak,warung",
        ]);

        if (isset($validated["password"])) {
            $validated["password"] = bcrypt($request->password);
        }

        try {
            $user->update($validated);
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
                "message" => "User berhasil dibuat.",
            ],
            Response::HTTP_INTERNAL_SERVER_ERROR
        );
    }
}
