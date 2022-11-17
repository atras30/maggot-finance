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
      $table->enum('transaction_type', ['trash_manager_transaction', 'shop_transaction', 'farmer_transaction']);
      $table->string("description")->default("");
      $table->unsignedDouble("weight_in_kg")->default(0)->nullable();
      $table->unsignedDouble("amount_per_kg")->default(0)->nullable();
      $table->unsignedDouble("total_amount")->default(0);
      $table->foreignId("farmer_id")->nullable()->default(null)->constrained("users")->onUpdate("cascade")->onDelete("cascade");
      $table->foreignId("trash_manager_id")->nullable()->default(null)->constrained("trash_managers")->onUpdate("cascade")->onDelete("cascade");
      $table->foreignId("shop_id")->nullable()->default(null)->constrained("users")->onUpdate("cascade")->onDelete("cascade");
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
