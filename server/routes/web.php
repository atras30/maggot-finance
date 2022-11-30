<?php

use Illuminate\Support\Facades\Route;
use App\Exports\UsersExport;
use App\Http\Controllers\ExcelController;
use Maatwebsite\Excel\Facades\Excel;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

// Route::get('/', function () {
//     return Excel::download(new UsersExport, 'users.xlsx');
// });

Route::get('/', [ExcelController::class, "allTransactions"]);
