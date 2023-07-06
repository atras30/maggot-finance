<?php

namespace App\Http\Controllers;

use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class TrashManagerController extends Controller
{
    /**
     * Display a listing of the trash manager data.
     *
     * @OA\Get(
     *     path="/api/trash-manager",
     *    tags={"Trash Manager"},
     *    summary="Get all trash manager data",
     *   description="Get all trash manager data",
     *   operationId="getAllTrashManager",
     *  security={{"sanctum":{}}},
     *  @OA\Response(
     *    response=200,
     *   description="Success",
     *  @OA\JsonContent(
     *         @OA\Property(property="trash_managers", type="array", @OA\Items(type="object", ref="#/components/schemas/TrashManager"))
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
    public function index()
    {
        return response()->json(
            [
                "trash_managers" => TrashManager::all(),
            ],
            Response::HTTP_OK
        );
    }

    /**
     * Display a listing of the trash manager data.
     *
     * @OA\Post(
     *     path="/api/trash-manager",
     *    tags={"Trash Manager"},
     *    summary="Create new trash manager",
     *   description="Create new trash manager",
     *   operationId="createTrashManager",
     *  security={{"sanctum":{}}},
     * @OA\RequestBody(
     *   required=true,
     * description="Create new trash manager",
     * @OA\JsonContent(
     * @OA\Property(property="nama_pengelola", type="string", example="string"),
     * @OA\Property(property="tempat", type="string", example="string"),
     * @OA\Property(property="email", type="string", example="string"),
     * ),
     * ),
     *  @OA\Response(
     *    response=201,
     *   description="Success",
     *  @OA\JsonContent(
     *         @OA\Property(property="message", type="string", example="Trash manager created successfully")
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
    public function store(Request $request)
    {
        $validated = $request->validate([
            "nama_pengelola" => "string|required|",
            "tempat" => "string|required|",
            "email" => "string|required|",
        ]);

        TrashManager::create($validated);

        return response()->json(
            [
                "message" => "Super admin created successfully",
            ],
            Response::HTTP_CREATED
        );
    }

    /**
     * Display a list of user by trash manager.
     *
     * @OA\Get(
     *     path="/api/trash-manager/list/users/{email}",
     *    tags={"Trash Manager"},
     *    summary="Get list of user by trash manager",
     *   description="Get list of user by trash manager",
     *   operationId="getListUser",
     *  security={{"sanctum":{}}},
     * @OA\Parameter(
     *   description="Email of trash manager",
     *  in="path",
     * name="email",
     * required=true,
     * @OA\Schema(
     * type="string"
     * )
     * ),
     *  @OA\Response(
     *    response=200,
     *   description="Success",
     *  @OA\JsonContent(
     *         @OA\Property(property="trash_managers", type="array", @OA\Items(type="object", ref="#/components/schemas/TrashManager"))
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
    public function listUser($email)
    {
        // $validated = $request->validate([
        //     "email" => "string|required",
        // ]);
        if (!$email) {
            return response()->json(
                [
                    "message" => "Email is required",
                ],
                Response::HTTP_BAD_REQUEST
            );
        }

        return TrashManager::where("email", $email)
            ->get()
            ->first()->users;
    }

    /**
     * Display a list of user by trash manager.
     *
     * @OA\Post(
     *     path="/api/transaction/trash-manager/buy/maggot",
     *    tags={"Transaction"},
     *    summary="Create buying maggot transaction",
     *   description="Create buying maggot transaction",
     *   operationId="buyMaggot",
     *  security={{"sanctum":{}}},
     * @OA\RequestBody(
     *   required=true,
     * @OA\JsonContent(
     * @OA\Property(property="description", type="string", example="Pembelian Maggot Warga"),
     * @OA\Property(property="weight_in_kg", type="integer", example=5),
     * @OA\Property(property="amount_per_kg", type="integer", example=10000),
     * @OA\Property(property="farmer_email", type="string", example="farmer1@magfin.id"),
     * ),
     * ),
     *  @OA\Response(
     *    response=200,
     *   description="Success",
     *  @OA\JsonContent(
     *        @OA\Property(property="message", type="string", example="Transaksi berhasil dibuat"),
     *       @OA\Property(property="transaction1", type="object", ref="#/components/schemas/Transaction"),
     *         @OA\Property(property="transaction2", type="object", ref="#/components/schemas/Transaction")
     *
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
    public static function buyMaggot(Request $request)
    {
        try {
            $validated["description"] = "Pembelian Maggot Warga";
            $validated["weight_in_kg"] = $request->weight_in_kg;
            $validated["amount_per_kg"] = $request->amount_per_kg;
            $validated["farmer_email"] = $request->farmer_email;

            if (!isset($validated["description"])) {
                $validated["description"] = "";
            }

            //Transaction dari warga ke pengepul
            $warga = User::where("email", $validated["farmer_email"])
                ->get()
                ->first();

            error_log($validated["farmer_email"]);

            $validated["farmer_id"] = $warga->id;
            $validated["trash_manager_id"] = auth()->user()->id;
            $validated["type"] = "income";
            $validated["transaction_type"] = "farmer_transaction";
            $validated["total_amount"] =
                $validated["weight_in_kg"] * $validated["amount_per_kg"];
            $warga->balance += $validated["total_amount"];

            $transaction1 = Transaction::create($validated);
            $warga->save();

            //Transaction dari pengepul ke warga
            $pengepulTransaction["trash_manager_id"] =
                $warga->trash_manager->id;
            $pengepulTransaction["farmer_id"] = $warga->id;
            $pengepulTransaction["description"] = $validated["description"];
            $pengepulTransaction["weight_in_kg"] = $validated["weight_in_kg"];
            $pengepulTransaction["amount_per_kg"] = $validated["amount_per_kg"];
            $pengepulTransaction["type"] = "expense";
            $pengepulTransaction["transaction_type"] =
                "trash_manager_transaction";
            $pengepulTransaction["total_amount"] =
                $validated["weight_in_kg"] * $validated["amount_per_kg"];

            $transaction2 = Transaction::create($pengepulTransaction);

            return [
                "message" => "Transaksi Berhasil",
                "transaction1" => $transaction1,
                "transaction2" => $transaction2,
            ];
        } catch (\Exception $e) {
            error_log($e->getMessage());
            return response()->json(
                [
                    "message" => $e->getMessage(),
                ],
                Response::HTTP_INTERNAL_SERVER_ERROR
            );
        }
    }
}
