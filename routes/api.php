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
Route::get("/user/pengepul", [UserController::class, "getPengepul"]);
Route::get("/user/warung", [UserController::class, "getWarung"]);
Route::get("/user/peternak", [UserController::class, "getPeternak"]);
Route::post("/user", [UserController::class, "store"]);
Route::put("/user/{id}", [UserController::class, "edit"]);
