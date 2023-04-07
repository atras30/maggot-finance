<?php

namespace Database\Seeders;

use App\Models\Notification;
use Ramsey\Uuid\Uuid;
use Illuminate\Database\Seeder;

class HelperSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Notification::create([
            "type" => "payment_confirmation",
            "farmer_id" => 2,
            "weight_in_kg" => 3.14,
            "amount_per_kg" => 3000,
            "description" => "Description testing doang...",
            "trash_manager_id" => 4,
            "token" => Uuid::uuid4()->toString(),
            "expired_at" => now()->addMinutes(15)
        ]);
    }
}
