<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;

class SuperAdminController extends Controller
{
    /**
     * Get all data for super admin
     *
     * @OA\Get(
     *      path="/api/super-admin",
     *      tags={"Super Admin"},
     *      summary="Get all data for super admin",
     *      description="Get all data for super admin",
     *      operationId="getAllDataSuperAdmin",
     *      security={{"sanctum":{}}},
     *      @OA\Response(
     *         response=200,
     *        description="Success",
     *       @OA\JsonContent(
     *         @OA\Property(property="super_admin", type="array", @OA\Items(type="object", ref="#/components/schemas/SuperAdmin")),
     *      ),
     *    ),
     *      @OA\Response(
     *        response=401,
     *       description="Unauthenticated",
     *     @OA\JsonContent(
     *      @OA\Property(property="message", type="string", example="Unauthenticated"),
     *     ),
     *    ),
     * @OA\Response(
     *  response=500,
     * description="Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Server Error"),
     * ),
     * ),
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
