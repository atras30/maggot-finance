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
    Schema::create('trash_managers', function (Blueprint $table) {
      $table->id();
      $table->string("nama_pengelola")->unique();
      $table->string("tempat");
      $table->string("email")->unique();
    //   $table->string("password");
      $table->string("role")->default("trash_manager");
      $table->foreignId("super_admin_id")->default(1)->constrained("super_admins", "id")->onUpdate("cascade")->onDelete("cascade");
      $table->timestamps();
    });
  }

  /**
   * Reverse the migrations.
   *
   * @return void
   */
  public function down() {
    Schema::dropIfExists('pengelola_bank_sampahs');
  }
};
