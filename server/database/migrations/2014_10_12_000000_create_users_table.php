<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
  /**
   * Run the migrations.
   *
   * @return void
   */
  public function up() {
    Schema::create('users', function (Blueprint $table) {
      $table->id();
      $table->string('full_name');
      // $table->string('username')->unique();
      $table->string('email')->unique();
      // $table->string('password');
      $table->unsignedBigInteger("balance")->default(0);
      $table->string('role')->enum(["farmer", "shop"]);
      $table->string('phone_number')->nullable();
      $table->string('address');
      $table->foreignId("trash_manager_id")->constrained("trash_managers", "id")->onUpdate("cascade")->onDelete("cascade");
      $table->timestamps();
      $table->boolean('is_verified')->default(0);
      // $table->rememberToken();
    });
  }

  /**
   * Reverse the migrations.
   *
   * @return void
   */
  public function down() {
    Schema::dropIfExists('users');
  }
};
