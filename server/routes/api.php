<?php

use App\Http\Controllers\AuthenticationController;
use App\Http\Controllers\MailController;
use App\Http\Controllers\NotificationController;
use App\Http\Controllers\RegistrationController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\SuperAdminController;
use App\Http\Controllers\TransactionController;
use App\Http\Controllers\TrashManagerController;
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

// //Authenticated User
Route::middleware('auth:sanctum')->group(function () {
    //Super Admin
    Route::get('/super-admin', [
        SuperAdminController::class,
        'getAllData',
    ]);

    //Authentication
    Route::prefix('auth')->group(function () {
        Route::post('/logout', [AuthenticationController::class, 'logout']);
        Route::get('/user', [AuthenticationController::class, 'getUser']);
        Route::post('/token/refresh', [AuthenticationController::class, "refreshToken"]);
    });

    //Notifications
    Route::get('/notifications', [NotificationController::class, 'getNotificationQueue']);

    // Notifications endpoint for Farmer Withdrawal
    Route::post('/notifications/trash-manager/withdrawal/farmer', [NotificationController::class, 'createFarmerWithdrawalNotification']);
    Route::post('/notifications/farmer/buy/shop', [NotificationController::class, 'createFarmerPaymentToShopNotification']);
    Route::post('/notifications/shop/farmer/purchase/approve', [TransactionController::class, 'approveFarmerPurchase']);
    Route::post('/notifications/farmer/withdrawal/approve', [TransactionController::class, 'approveFarmerWithdrawal']);
    Route::post('/notifications/farmer/withdrawal/reject', [TransactionController::class, 'rejectFarmerWithdrawal']);

    //Transaction List
    Route::prefix("transaction")->group(function() {
        Route::post('/trash-manager/buy/maggot', [
            TrashManagerController::class,
            'buyMaggot',
        ]);

        //Warga
        Route::post('/farmer/buy/shop', [UserController::class, 'buyFromShop']);
    });

    //Trash Manager
    Route::post('/trash-manager', [TrashManagerController::class, 'store']);
});

// Transactions Endpoints
Route::get('/transactions', [TransactionController::class, 'index']);

//Trash Managers Endpoints
Route::get('/trash-manager', [TrashManagerController::class, 'index']);
Route::get('/trash-manager/list/user', [
    TrashManagerController::class,
    'listUser',
]);

//User Endpoints
Route::get('/user', [UserController::class, 'index']);
Route::get('/user/role/{role}', [UserController::class, 'getUserByRole']);
Route::get('/user/email/{email}', [UserController::class, 'getUserByEmail']);
Route::post('/user', [UserController::class, 'store']);
Route::get('/user/unauthenticated', [
    UserController::class,
    'unauthenticatedUser',
]);
// Route::put("/user/{id}", [UserController::class, "edit"]);

//Authentication
Route::prefix('auth')->group(function () {
    Route::post('/login', [AuthenticationController::class, 'login']);
    Route::post('/login/super-admin', [AuthenticationController::class, 'loginSuperAdmin']);
});

//Registration
Route::post('/register/user', [RegistrationController::class, 'registerUser']);
Route::post('/register/user/approve', [
    RegistrationController::class,
    'approveUserRequest',
]);
Route::post('/register/user/reject', [
    RegistrationController::class,
    'rejectUserRequest',
]);
Route::post('/register/trash-manager/approve', [
    RegistrationController::class,
    'rejectTrashManagerRequest',
]);
Route::post('/register/trash-manager/reject', [
    RegistrationController::class,
    'rejectTrashManagerRequest',
]);

//Mailing Endpoints
Route::post('/send-registration-mail', [
    MailController::class,
    'sendRegistrationMail',
]);
Route::post('/send-transaction-bill', [
    MailController::class,
    'sendTransactionBill',
]);

//Dummy Data Endpoints
Route::post('/dummy-data/transaction/{email}/{transactionAmount}', [
    RegistrationController::class,
    'createTransactionDummyDataForSpecificUser',
]);
