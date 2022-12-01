<table>
    <tbody>
        <tr>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Status</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Deskripsi</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Berat (KG)</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Harga Per KG</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Nama Peternak</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Email Peternak</td>
            <td style="text-align: center; border: 1px solid black; background-color: #EBF1DE; font-weight: bold;">Tanggal Transaksi</td>
        </tr>

        @foreach ($transactions as $transaction)
            <tr>
                <td>{{$transaction->type}}</td>
                <td>{{$transaction->description}}</td>
                <td>{{$transaction->weight_in_kg}}</td>
                <td>{{$transaction->amount_per_kg}}</td>
                <td>{{$transaction->farmer->full_name}}</td>
                <td>{{$transaction->farmer->email}}</td>
                <td>{{$transaction->created_at}}</td>
            </tr>
        @endforeach
    </tbody>
</table>
