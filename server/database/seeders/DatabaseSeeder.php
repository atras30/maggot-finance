<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;

use App\Models\Transaction;
use Illuminate\Database\Seeder;
use App\Models\User;
use Ramsey\Uuid\Uuid;

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

    User::create([
      'full_name' => "Atras Shalhan",
      "username" => "atras30",
      "email" => "atrasshalhan@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "pengepul",
      "balance" => 500000
    ]);

    User::create([
      'full_name' => "Reynard Matthew Yaputra",
      "username" => "rey123",
      "email" => "reynard@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "peternak",
      "balance" => 500000
    ]);

    User::create([
      'full_name' => "Jonathan Putra",
      "username" => "berserker543",
      "email" => "jojo@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "peternak",
      "balance" => 50000
    ]);

    User::create([
      'full_name' => "Bonifasius",
      "username" => "boni123",
      "email" => "boni@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "warung",
      "balance" => 50000
    ]);

    Transaction::create([
      "user_id" => "2",
      "related_transaction_user_id" => "1",
      "type" => "income",
      "weight" => 10,
      "amount" => 200000,
      "description" => "Jual Maggot 10kg"
    ]);

    Transaction::create([
      "user_id" => "1",
      "related_transaction_user_id" => "2",
      "type" => "expense",
      "weight" => 10,
      "amount" => 200000,
      "description" => "Jual Maggot 10kg"
    ]);
  }
}
