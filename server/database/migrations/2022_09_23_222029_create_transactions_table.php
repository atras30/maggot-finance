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
    Schema::create('transactions', function (Blueprint $table) {
      $table->id();
      $table->enum('type', ['expense', 'income']);
      $table->enum('transaction_type', ['transaksi_pengelola', 'transaksi_warga', 'transaksi_warung']);
      $table->string("description")->default("");
      $table->unsignedDouble("weight_in_kg")->default(0);
      $table->unsignedDouble("amount_per_kg")->default(0);
      $table->unsignedDouble("total_amount")->default(0);
      $table->foreignId("pengelola_bank_sampah_id")->constrained("trash_managers")->onUpdate("cascade")->onDelete("cascade")->nullable();
      // $table->foreignId("warung_id")->constrained("shops")->onUpdate("cascade")->onDelete("cascade")->nullable();
      $table->foreignId("warga_id")->constrained("users")->onUpdate("cascade")->onDelete("cascade");
      $table->timestamps();
    });
  }

  /**
   * Reverse the migrations.
   *
   * @return void
   */
  public function down() {
    Schema::dropIfExists('transactions');
  }
};
