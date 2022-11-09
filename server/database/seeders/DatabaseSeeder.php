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
    //   "password" => bcrypt("testing12345")
    ]);

    TrashManager::create([
      "nama_pengelola" => "Rajeg Corps",
      "tempat" => "Kecamatan Rajeg",
      "email" => "rajeg@gmail.com",
      "super_admin_id" => 1,
    //   "password" => bcrypt("testing12345")
    ]);

    TrashManager::create([
      "nama_pengelola" => "Pluit Corps",
      "tempat" => "Villa Mutiara Pluit",
      "email" => "atrasshalhan@gmail.com",
      "super_admin_id" => 1,
    //   "password" => bcrypt("testing12345")
    ]);

    TrashManager::create([
      "nama_pengelola" => "Jonathan Corps",
      "tempat" => "Jakarta Barat",
      "email" => "jonathanputra134@gmail.com",
      "super_admin_id" => 1,
    //   "password" => bcrypt("testing12345")
    ]);

    // User::create([
    //   'full_name' => "Atras Shalhan",
    //   // "username" => "atras30",
    //   "email" => "atrasshalhan@gmail.com",
    //   // "password" => bcrypt("testing12345"),
    //   "role" => "shop",
    //   "trash_manager_id" => 1,
    //   "balance" => 500000
    // ]);

    User::create([
      'full_name' => "Reynard Matthew Yaputra",
      // "username" => "rey123",
      "email" => "reynard@gmail.com",
      // "password" => bcrypt("testing12345"),
      "role" => "farmer",
      "balance" => 500000,
      "address" => "VMP c5/14",
      "trash_manager_id" => 1,
    ]);

    User::create([
      'full_name' => "Jonathan Putra",
      // "username" => "berserker543",
      "email" => "jojo@gmail.com",
      // "password" => bcrypt("testing12345"),
      "role" => "farmer",
      "balance" => 50000,
      "address" => "Jakarta Barat, blablabla",
      "trash_manager_id" => 1,
    ]);

    User::create([
      'full_name' => "Bonifasius",
      // "username" => "boni123",
      "email" => "boni@gmail.com",
      // "password" => bcrypt("testing12345"),
      "role" => "shop",
      "balance" => 50000,
      "address" => "Jalan gatau bwang",
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
