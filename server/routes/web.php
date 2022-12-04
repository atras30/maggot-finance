<?php

use Illuminate\Support\Facades\Route;
use App\Exports\UsersExport;
use App\Http\Controllers\ExcelController;
use App\Models\Transaction;
use App\Models\User;
use Illuminate\Support\Facades\Http;
use Maatwebsite\Excel\Facades\Excel;
use Carbon\Carbon;

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

Route::get('/', );
Route::get('/excel/export/{email}/{beginDate}/{endDate}', [ExcelController::class, "downloadReport"]);
