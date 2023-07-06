<?php

namespace App\Http\Controllers;

use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Routing\Controller as BaseController;

/**
 * @OA\Info(
 *      version="1.0.0",
 *      title="MagFin API Documentation",
 *      description="MagFin API Documentation",
 * )
 *
 * @OA\Server(
 *     url=CONST_HOST,
 *    description=CONST_HOST_NAME
 * )
 *
 * @OA\Schema(
 *     schema="User",
 *     title="User",
 *     description="User model",
 *      @OA\Property(
 *         property="id",
 *         type="integer",
 *      ),
 *      @OA\Property(
 *        property="full_name",
 *       type="string",
 *      ),
 *      @OA\Property(
 *      property="shop_name",
 *      type="string",
 *  ),
 *       @OA\Property(
 *      property="email",
 *      type="string",
 * ),
 *       @OA\Property(
 * property="balance",
 * type="integer",
 * ),
 *       @OA\Property(
 * property="role",
 * type="string",
 * ),
 *       @OA\Property(
 * property="phone_number",
 * type="string",
 * ),
 *       @OA\Property(
 * property="address",
 * type="string",
 * ),
 *       @OA\Property(
 * property="trahs_manager_id",
 * type="integer",
 * ),
 *       @OA\Property(
 * property="created_at",
 * type="string",
 * format="date-time"
 * ),
 *       @OA\Property(
 * property="updated_at",
 * type="string",
 * format="date-time"
 * ),
 *       @OA\Property(
 * property="is_verified",
 * type="boolean",
 * ),
 *       @OA\Property(
 * property="deleted_at",
 * type="string",
 * format="date-time"
 * ),
 * ),
 *
 * @OA\Schema(
 *    schema="TrashManager",
 *   title="TrashManager",
 * description="TrashManager model",
 *   @OA\Property(
 *    property="id",
 *  type="integer",
 * ),
 *  @OA\Property(
 *  property="nama_pengelola",
 * type="string",
 * ),
 * @OA\Property(
 * property="tempat",
 * type="string",
 * ),
 * @OA\Property(
 * property="email",
 * type="string",
 * ),
 * @OA\Property(
 * property="role",
 * type="string",
 * ),
 * @OA\Property(
 * property="super_admin_id",
 * type="integer",
 * ),
 * @OA\Property(
 * property="created_at",
 * type="string",
 * format="date-time"
 * ),
 * @OA\Property(
 * property="updated_at",
 * type="string",
 * format="date-time"
 * ),
 * @OA\Property(
 * property="users",
 * type="array",
 * @OA\Items(
 * ref="#/components/schemas/User"
 * ),
 * ),
 * ),
 *
 * @OA\Schema(
 * schema="Transaction",
 * title="Transaction",
 * description="Transaction model",
 * @OA\Property(property="id", type="integer"),
 * @OA\Property(property="type", type="string"),
 * @OA\Property(property="transaction_type", type="string"),
 * @OA\Property(property="description", type="string"),
 * @OA\Property(property="weight_in_kg", type="integer"),
 * @OA\Property(property="amount_per_kg", type="integer"),
 * @OA\Property(property="total_amount", type="integer"),
 * @OA\Property(property="farmer_id", type="integer"),
 * @OA\Property(property="trash_manager_id", type="integer"),
 * @OA\Property(property="shop_id", type="integer"),
 * @OA\Property(property="created_at", type="string", format="date-time"),
 * @OA\Property(property="updated_at", type="string", format="date-time"),
 * ),
 *
 * @OA\Schema(
 * schema="SuperAdmin",
 * title="SuperAdmin",
 * description="SuperAdmin model",
 * @OA\Property(property="id", type="integer"),
 * @OA\Property(property="nama_pengelola", type="string"),
 * @OA\Property(property="tempat", type="string"),
 * @OA\Property(property="email", type="string"),
 * @OA\Property(property="password", type="string"),
 * @OA\Property(property="created_at", type="string", format="date-time"),
 * @OA\Property(property="updated_at", type="string", format="date-time"),
 * @OA\Property(property="trash_managers", type="array", @OA\Items(ref="#/components/schemas/TrashManager")),
 * ),
 * 
 */

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;
}
