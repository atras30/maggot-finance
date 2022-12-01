<?php

namespace App\Http\Controllers;

use App\Exports\TransactionExport;
use Illuminate\Http\Request;
use Maatwebsite\Excel\Facades\Excel;

class ExcelController extends Controller
{
    public function allTransactions() {
        return Excel::download(new TransactionExport, 'Transaksi Pengelola Bank Sampah.xlsx');
    }
}
