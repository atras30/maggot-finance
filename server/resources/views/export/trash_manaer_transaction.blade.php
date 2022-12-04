<table>
    <tbody>
        <tr>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Nomor</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Kode</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Deskripsi</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Berat (KG)</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Harga Per KG</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Pengeluaran</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Pemasukan</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Tanggal Transaksi</td>
        </tr>

        @foreach ($transactions as $transaction)
            <tr>
                <td style="text-align:center;">{{$loop->index + 1}}</td>
                <td style="text-align:center;">{{$transaction->type == "expense" ? "Pengeluaran" : "Pemasukan"}}</td>
                <td style="text-align:center;">{{$transaction->description}}</td>
                <td style="text-align:center;">{{$transaction->weight_in_kg ? $transaction->weight_in_kg : "-"}}</td>
                <td style="text-align:center;">{{$transaction->amount_per_kg ? $transaction->amount_per_kg : "-"}}</td>
                <td style="text-align:center;">{{$transaction->type == "expense" ? $transaction->total_amount : "-"}}</td>
                <td style="text-align:center;">{{$transaction->type == "income" ? $transaction->total_amount : "-"}}</td>
                <td style="text-align:center;">{{$transaction->created_at}}</td>
            </tr>
        @endforeach
    </tbody>
</table>
