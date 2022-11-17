<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;

use App\Models\SuperAdmin;
use App\Models\TrashManager;
use Illuminate\Database\Seeder;
use App\Models\User;

class DatabaseSeeder extends Seeder {
  /**
   * Seed the application's database.
   *
   * @return void
   */
  public function run() {
    // \App\Models\User::factory(10)->create();

    // \App\Models\User::factory()->create([
    //     'name' => 'Test User',
    //     'email' => 'test@example.com',
    // ]);

    SuperAdmin::create([
      "nama_pengelola" => "Universitas Multimedia Nusantara",
      "tempat" => "Universitas Multimedia Nusantara",
      "email" => "admin@umn.ac.id",
      "password" => bcrypt("adminumn2022")
    ]);

    TrashManager::create([
      "nama_pengelola" => "Sepatan Timur",
      "tempat" => "Kecamatan Sepatan Timur",
      "email" => "sepatanTimur@gmail.com",
      "super_admin_id" => 1,
    ]);

    TrashManager::create([
      "nama_pengelola" => "Rajeg Corps",
      "tempat" => "Kecamatan Rajeg",
      "email" => "rajeg@gmail.com",
      "super_admin_id" => 1,
    ]);

    TrashManager::create([
      "nama_pengelola" => "Pluit Corps",
      "tempat" => "Villa Mutiara Pluit",
      "email" => "atrasshalhan@gmail.com",
      "super_admin_id" => 1,
    ]);

    TrashManager::create([
      "nama_pengelola" => "Jonathan Corps",
      "tempat" => "Jakarta Barat",
      "email" => "jonathanputra134@gmail.com",
      "super_admin_id" => 1,
    ]);

    User::create([
      'full_name' => "Reynard Matthew Yaputra",
      "email" => "reynard@gmail.com",
      "role" => "farmer",
      "balance" => 500000,
      "address" => "VMP c5/14",
      "trash_manager_id" => 1,
    ]);

    User::create([
      'full_name' => "Jonathan Putra",
      "email" => "jojo@gmail.com",
      "role" => "farmer",
      "balance" => 50000,
      "address" => "Jakarta Barat, blablabla",
      "trash_manager_id" => 1,
    ]);

    User::create([
      'full_name' => "Bonifasius",
      "email" => "boni@gmail.com",
      "role" => "shop",
      "balance" => 50000,
      "address" => "Jalan gatau bwang",
      "trash_manager_id" => 1,
    ]);

    User::create([
      'full_name' => "Maggot Finance",
      "email" => "magfin@umn.ac.id",
      "role" => "shop",
      "balance" => 50000,
      "address" => "Universitas Multimedia Nusantara",
      "trash_manager_id" => 1,
    ]);

    // Transaction::create([
    //   "user_id" => "2",
    //   "related_transaction_user_id" => "1",
    //   "type" => "expense",
    //   "weight_in_kg" => 10,
    //   "amount_per_kg" => 10000,
    //   "total_amount" => 100000,
    // ]);

    // Transaction::create([
    //   "user_id" => "1",
    //   "related_transaction_user_id" => "2",
    //   "type" => "income",
    //   "weight_in_kg" => 10,
    //   "amount_per_kg" => 10000,
    //   "total_amount" => 100000,
    // ]);
  }
}
