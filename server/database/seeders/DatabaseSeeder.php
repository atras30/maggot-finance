<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;

use App\Models\Notification;
use App\Models\SuperAdmin;
use App\Models\Transaction;
use App\Models\TrashManager;
use Illuminate\Database\Seeder;
use Ramsey\Uuid\Uuid;
use App\Models\User;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
        // \App\Models\User::factory(10)->create();

        // \App\Models\User::factory()->create([
        //     'name' => 'Test User',
        //     'email' => 'test@example.com',
        // ]);

        SuperAdmin::create([
            'nama_pengelola' => 'Universitas Multimedia Nusantara',
            'tempat' => 'Universitas Multimedia Nusantara',
            'email' => 'admin@umn.ac.id',
            'password' => bcrypt('adminumn2022'),
        ]);

        TrashManager::create([
            'nama_pengelola' => 'Sepatan Timur',
            'tempat' => 'Kecamatan Sepatan Timur',
            'email' => 'sepatanTimur@gmail.com',
            'super_admin_id' => 1,
        ]);

        TrashManager::create([
            'nama_pengelola' => 'Reynard',
            'tempat' => fake()->address(),
            'email' => 'reynard.yaputra@student.umn.ac.id',
            'super_admin_id' => 1,
        ]);

        TrashManager::create([
            'nama_pengelola' => 'Rajeg Corps',
            'tempat' => 'Kecamatan Rajeg',
            'email' => 'rajeg@gmail.com',
            'super_admin_id' => 1,
        ]);

        TrashManager::create([
            'nama_pengelola' => 'Pluit Corps',
            'tempat' => 'Villa Mutiara Pluit',
            'email' => 'atrasshalhan@gmail.com',
            'super_admin_id' => 1,
        ]);

        TrashManager::create([
            'nama_pengelola' => 'Jonathan Corps',
            'tempat' => 'Jakarta Barat',
            'email' => 'jonathanputra134@gmail.com',
            'super_admin_id' => 1,
        ]);

        User::create([
            'full_name' => 'Atras Shalhan',
            'email' => 'atras.shalhan@student.umn.ac.id',
            'role' => 'farmer',
            'balance' => 0,
            'address' => 'VMP c5/14',
            'trash_manager_id' => 4,
            'is_verified' => 1,
        ]);
        User::create([
            'full_name' => 'Reynard Matthew Yaputra',
            'email' => 'reynard.rmy@gmail.com',
            'role' => 'farmer',
            'balance' => 500000,
            'address' => 'VMP c5/14',
            'trash_manager_id' => 4,
            'is_verified' => 1,
        ]);
        User::create([
            'full_name' => 'Reynard Matthew Yaputra',
            'shop_name' => fake()->company(),
            'email' => 'reynard7896@gmail.com',
            'role' => 'shop',
            'balance' => 15000000.234,
            'address' => 'VMP c5/14',
            'trash_manager_id' => 4,
            'is_verified' => 1,
        ]);

        User::create([
            'full_name' => 'Jonathan Putra',
            'email' => 'jonathanputra134@gmail.com',
            'role' => 'farmer',
            'balance' => 50000,
            'address' => fake()->address(),
            'trash_manager_id' => 4,
        ]);

        User::create([
            'full_name' => 'Bonifasius',
            'email' => 'boni@gmail.com',
            'role' => 'shop',
            'shop_name' => fake()->company(),
            'balance' => 50000,
            'address' => fake()->address(),
            'trash_manager_id' => 4,
        ]);

        User::create([
            'full_name' => 'Maggot Finance',
            'email' => 'magfin@umn.ac.id',
            'role' => 'shop',
            'shop_name' => fake()->company(),
            'balance' => 50000,
            'address' => 'Universitas Multimedia Nusantara',
            'trash_manager_id' => 4,
            'is_verified' => 1,
        ]);

        //Transaction Dummy Data
        for ($i = 0; $i < 100; $i++) {
            $weightInKg = fake()->randomFloat(2, 0.5, 10);
            $amountPerKg = fake()->numberBetween(3000, 10000);

            $users = User::all();
            $farmerId =
                $users[fake()->numberBetween(0, $users->count() - 1)]->id;

            $trashmanagers = TrashManager::all();
            $trashManagerId =
                $trashmanagers[fake()->numberBetween(0, $trashmanagers->count() - 1)]->id;

            $description = fake()->words(3, true);
            $createdAt = now()->subDay(fake()->numberBetween(0, 60));

            Transaction::create([
                'type' => 'income',
                'description' => $description,
                'weight_in_kg' => $weightInKg,
                'amount_per_kg' => $amountPerKg,
                'total_amount' => $weightInKg * $amountPerKg,
                'farmer_id' => $farmerId,
                'trash_manager_id' => $trashManagerId,
                'transaction_type' => 'farmer_transaction',
                'created_at' => $createdAt,
            ]);

            Transaction::create([
                'type' => 'expense',
                'description' => $description,
                'weight_in_kg' => $weightInKg,
                'amount_per_kg' => $amountPerKg,
                'total_amount' => $weightInKg * $amountPerKg,
                'farmer_id' => $farmerId,
                'trash_manager_id' => $trashManagerId,
                'transaction_type' => 'trash_manager_transaction',
                'created_at' => $createdAt,
            ]);
        }

        // User dummy data
        $trashManagers = TrashManager::all();
        $maxTrashManagerIndex = $trashManagers->count() - 1;

        for ($i = 0; $i < 50; $i++) {
            $role = fake()->randomElement(['farmer', 'shop']);

            if ($role == 'shop') {
                $validated = [
                    'email' => fake()
                        ->unique()
                        ->safeEmail(),
                    'full_name' => fake()->name(),
                    'role' => $role,
                    "shop_name" => fake()->company(),
                    'balance' => fake()->randomNumber(5, true),
                    'phone_number' => fake()->phoneNumber(),
                    'address' => fake()->address(),
                    'trash_manager_id' =>
                    $trashManagers[fake()->numberBetween(0, $maxTrashManagerIndex)]->id,
                    'is_verified' => 1
                ];
            } else {
                $validated = [
                    'email' => fake()
                        ->unique()
                        ->safeEmail(),
                    'full_name' => fake()->name(),
                    'role' => $role,
                    'balance' => fake()->randomNumber(5, true),
                    'phone_number' => fake()->phoneNumber(),
                    'address' => fake()->address(),
                    'trash_manager_id' =>
                    $trashManagers[fake()->numberBetween(0, $maxTrashManagerIndex)]->id,
                    'is_verified' => 1
                ];
            }

            User::create($validated);
        }
    }
}
