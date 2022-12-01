<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Laravel\Sanctum\HasApiTokens;
use App\Models\User;

class TrashManager extends Authenticatable
{
    use HasFactory, HasApiTokens;

    protected $guarded = ['id', 'created_at', 'updated_at'];

    protected $with = ['users'];

    public function users()
    {
        return $this->hasMany(User::class);
    }

    public function managed_by()
    {
        return $this->belongsTo(SuperAdmin::class, 'super_admin_id');
    }

    public static function buyMaggot($description,$weightInKg,$amountPerKg,$farmerEmail) {
        $validated['description'] = $description;
        $validated['weight_in_kg'] = $weightInKg;
        $validated['amount_per_kg'] = $amountPerKg;
        $validated['farmer_email'] = $farmerEmail;

        if (!isset($validated['description'])) {
            $validated['description'] = '';
        }

        //Transaction dari warga ke pengepul
        $warga = User::where('email', $validated['farmer_email'])
            ->get()
            ->first();

        $validated['farmer_id'] = $warga->id;
        $validated['trash_manager_id'] = auth()->user()->id;
        $validated['type'] = 'income';
        $validated['transaction_type'] = 'farmer_transaction';
        $validated['total_amount'] = $validated['weight_in_kg'] * $validated['amount_per_kg'];
        $warga->balance += $validated['total_amount'];

        $transaction1 = Transaction::create($validated);
        $warga->save();

        //Transaction dari pengepul ke warga
        $pengepulTransaction['trash_manager_id'] = $warga->trash_manager->id;
        $pengepulTransaction['farmer_id'] = $warga->id;
        $pengepulTransaction['description'] = $validated['description'];
        $pengepulTransaction['weight_in_kg'] = $validated['weight_in_kg'];
        $pengepulTransaction['amount_per_kg'] = $validated['amount_per_kg'];
        $pengepulTransaction['type'] = 'expense';
        $pengepulTransaction['transaction_type'] = 'trash_manager_transaction';
        $pengepulTransaction['total_amount'] =
            $validated['weight_in_kg'] * $validated['amount_per_kg'];

        $transaction2 = Transaction::create($pengepulTransaction);

        return [
            'message' => 'Transaksi Berhasil',
            'transaction1' => $transaction1,
            'transaction2' => $transaction2,
        ];
    }
}
