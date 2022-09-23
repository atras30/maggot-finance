<?php

use App\Http\Controllers\AuthenticationController;
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

//Authenticated User
Route::middleware('auth:sanctum')->prefix("auth")->group(function () {
  //Authentication
  Route::post("/logout", [AuthenticationController::class, "logout"]);
  Route::get("/user", [AuthenticationController::class, "getUser"]);
});

Route::get("/user", [UserController::class, "index"]);
Route::get("/user/role/{role}", [UserController::class, "getUserByRole"]);
Route::get("/user/username/{username}", [UserController::class, "getUserByUsername"]);
Route::post("/user", [UserController::class, "store"]);
Route::put("/user/{id}", [UserController::class, "edit"]);

//Authentication
Route::prefix("auth")->group(function () {
  Route::post("/login", [AuthenticationController::class, "login"]);
  Route::post("/register", [AuthenticationController::class, "register"]);
});
