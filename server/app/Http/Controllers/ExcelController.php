<?php

namespace App\Http\Controllers;

use App\Exports\TransactionExport;
use App\Models\Transaction;
use App\Models\TrashManager;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Maatwebsite\Excel\Facades\Excel;

class ExcelController extends Controller
{
    public function downloadReport($email, $beginDate, $endDate) {
        $user = User::firstWhere("email", $email);

        if(!$user) {
            $trashManager = TrashManager::firstWhere("email", $email);
            $transactions = Transaction::where("created_at", ">=", Carbon::parse($beginDate))->where("created_at", "<=", Carbon::parse($endDate))->where("trash_manager_id", $trashManager->id)->where("transaction_type", "trash_manager_transaction")->get();
            return Excel::download(new TransactionExport($transactions), 'Transaksi Pengelola Bank Sampah.xlsx');
        }

        if($user->role == "farmer") {
            $transactions = Transaction::where("created_at", ">=", Carbon::parse($beginDate)->startOfDay())->where("created_at", "<=", Carbon::parse($endDate)->endOfDay())->where("farmer_id", $user->id)->where("transaction_type", "farmer_transaction")->get()->sortByDesc("created_at");
            return Excel::download(new TransactionExport($transactions), 'Transaksi Peternak.xlsx');
        } else if($user->role == "shop") {
            $transactions = Transaction::where("created_at", ">=", Carbon::parse($beginDate))->where("created_at", "<=", Carbon::parse($endDate))->where("shop_id", $user->id)->where("transaction_type", "shop_transaction")->get();
            return Excel::download(new TransactionExport($transactions), 'Transaksi Warung.xlsx');
        }
    }
}
