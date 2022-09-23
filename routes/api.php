<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });

Route::get("/user", [UserController::class, "index"]);
Route::get("/user/role/{role}", [UserController::class, "getUserByRole"]);
Route::get("/user/username/{username}", [UserController::class, "getUserByUsername"]);
Route::post("/user", [UserController::class, "store"]);
Route::put("/user/{id}", [UserController::class, "edit"]);
