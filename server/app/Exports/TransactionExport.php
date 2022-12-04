<?php

namespace App\Exports;

use App\Models\Transaction;
use Illuminate\Contracts\View\View;
use Maatwebsite\Excel\Concerns\FromView;
use Maatwebsite\Excel\Concerns\ShouldAutoSize;
use Maatwebsite\Excel\Concerns\WithStyles;
use PhpOffice\PhpSpreadsheet\Worksheet\Worksheet;

class TransactionExport implements FromView, ShouldAutoSize
{
    private $transactions;

    public function __construct($transactions) {
        $this->transactions = $transactions;
    }

    public function view(): View
    {
        return view('export.trash_manaer_transaction', [
            'transactions' => $this->transactions
        ]);
    }
}
