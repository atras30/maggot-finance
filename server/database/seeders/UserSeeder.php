<?php

namespace Database\Seeders;

use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        // User dummy data
        $trashManagers = TrashManager::all();
        $maxTrashManagerIndex = $trashManagers->count() - 1;

        for ($i = 0; $i < 200; $i++) {
            $role = fake()->randomElement(['farmer', 'shop']);

            if ($role == 'shop') {
                $validated = [
                    'email' => fake()
                        ->unique()
                        ->safeEmail(),
                    'full_name' => fake()->name(),
                    'role' => $role,
                    'shop_name' => fake()->company(),
                    'balance' => fake()->randomNumber(5, true),
                    'phone_number' => fake()->phoneNumber(),
                    'address' => fake()->address(),
                    'trash_manager_id' => TrashManager::where("email", "reynard.yaputra@student.umn.ac.id")->get()->first()->id,
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
                    'trash_manager_id' => TrashManager::where("email", "reynard.yaputra@student.umn.ac.id")->get()->first()->id,
                ];
            }

            User::create($validated);
        }
    }
}
