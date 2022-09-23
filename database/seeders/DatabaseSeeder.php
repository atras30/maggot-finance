<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
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

    User::create([
      'full_name' => "Atras Shalhan",
      "username" => "atras30",
      "email" => "atrasshalhan@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "pengepul"
    ]);

    User::create([
      'full_name' => "Reynard Matthew Yaputra",
      "username" => "rey123",
      "email" => "reynard@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "peternak"
    ]);

    User::create([
      'full_name' => "Jonathan Putra",
      "username" => "berserker543",
      "email" => "jojo@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "peternak"
    ]);

    User::create([
      'full_name' => "Bonifasius",
      "username" => "boni123",
      "email" => "boni@gmail.com",
      "password" => bcrypt("testing12345"),
      "role" => "warung"
    ]);
  }
}
