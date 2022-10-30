<?php

use App\Http\Controllers\AuthenticationController;
use App\Http\Controllers\MailController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\SuperAdminController;
use App\Http\Controllers\TransactionController;
use App\Http\Controllers\TrashManagerController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

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
Route::middleware('auth:sanctum')->group(function () {
  //Super Admin
  Route::get("/super-admin/trash-managers", [SuperAdminController::class, "getTrashManagers"]);

  //Transaction List
  Route::get("/transactions", [TransactionController::class, "index"]);
  Route::post("/transaction/trash-manager/buy/maggot", [TrashManagerController::class, "buyMaggot"]);

  //Warga
  Route::post("/user/warga/buy/shop", [UserController::class, "sellMaggots"]);

  //Authentication
  Route::prefix("auth")->group(function () {
    Route::post("/logout", [AuthenticationController::class, "logout"]);
    Route::get("/user", [AuthenticationController::class, "getUser"]);
  });
});

//Trash Managers Endpoints
Route::get("/trash-manager", [TrashManagerController::class, "index"]);

//User Endpoints
Route::get("/user", [UserController::class, "index"]);
Route::get("/user/role/{role}", [UserController::class, "getUserByRole"]);
Route::get("/user/username/{username}", [UserController::class, "getUserByUsername"]);
Route::post("/user", [UserController::class, "store"]);
Route::put("/user/{id}", [UserController::class, "edit"]);


//Authentication
Route::prefix("auth")->group(function () {
  Route::post("/login", [AuthenticationController::class, "loginUser"]);
  Route::post("/user/register", [AuthenticationController::class, "registerUser"]);

  Route::post("/login/super-admin", [AuthenticationController::class, "loginSuperAdmin"]);
  Route::post("/login/trash-manager", [AuthenticationController::class, "loginTrashManager"]);
});

//Mailing Endpoints
Route::post('/send-registration-mail', [MailController::class, "sendRegistrationMail"]);
Route::post('/send-transaction-bill', [MailController::class, "sendTransactionBill"]);
