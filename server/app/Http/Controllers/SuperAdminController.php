<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;

class SuperAdminController extends Controller
{
    /**
     * Displaying all users
     *
     * @OA\Get(
     *      path="/api/super-admin",
     *      tags={"Super Admin"},
     *      summary="Displaying all users",
     *      description="Displaying all users",
     *      operationId="getAllDataSuperAdmin",
     *      security={{"sanctum":{}}},
     *      @OA\Response(
     *         response=200,
     *        description="Success",
     *       @OA\JsonContent(
     *         @OA\Property(property="users", type="array", @OA\Items(type="object", ref="#/components/schemas/User")),
     *      ),
     *      @OA\Response(
     *        response=401,
     *       description="Unauthenticated",
     *     ),
     *    ),
     * ),
     */
    public function getAllData(Request $request)
    {
        return response()->json(
            [
                "trash_managers" => auth()->user(),
            ],
            Response::HTTP_OK
        );
    }
}
