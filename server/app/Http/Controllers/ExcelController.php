<?php

namespace App\Http\Controllers;

use App\Exports\TransactionExport;
use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Maatwebsite\Excel\Facades\Excel;

class ExcelController extends Controller
{
    public function downloadReport($email, $beginDate, $endDate) {
        $beginDate = Carbon::parse($beginDate, "Asia/Jakarta");
        $endDate = Carbon::parse($endDate, "Asia/Jakarta");

        $user = User::firstWhere("email", $email);

        if(!$user) {
            $trashManager = TrashManager::firstWhere("email", $email);
            $transactions = collect(DB::select("select * from transactions where created_at >= ? and created_at <= ? and trash_manager_id = ? and transaction_type = ?", [$beginDate->startOfDay(), $endDate->endOfDay(), $trashManager->id, "trash_manager_transaction"]))->sortByDesc("created_at");

            // $transactions = Transaction::where("created_at", ">=", Carbon::parse($beginDate))->where("created_at", "<=", Carbon::parse($endDate))->where("trash_manager_id", $trashManager->id)->where("transaction_type", "trash_manager_transaction")->get();
            return Excel::download(new TransactionExport($transactions), 'Transaksi Pengelola Bank Sampah.xlsx');
        }

        if($user->role == "farmer") {
            $transactions = collect(DB::select("select * from transactions where created_at >= ? and created_at <= ? and farmer_id = ? and transaction_type = ?", [$beginDate->startOfDay(), $endDate->endOfDay(), $user->id, "farmer_transaction"]))->sortByDesc("created_at");
            // $transactions = Transaction::where("created_at", ">=", Carbon::parse($beginDate)->startOfDay())->where("created_at", "<=", Carbon::parse($endDate)->endOfDay())->where("farmer_id", $user->id)->where("transaction_type", "farmer_transaction")->get()->sortByDesc("created_at");
            return Excel::download(new TransactionExport($transactions), 'Transaksi Peternak.xlsx');
        } else if($user->role == "shop") {
            $transactions = collect(DB::select("select * from transactions where created_at >= ? and created_at <= ? and shop_id = ? and transaction_type = ?", [$beginDate->startOfDay(), $endDate->endOfDay(), $user->id, "shop_transaction"]))->sortByDesc("created_at");
            return Excel::download(new TransactionExport($transactions), 'Transaksi Warung.xlsx');
        }
    }
}
