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
      $table->string("description")->default("");
      $table->decimal("weight_in_kg");
      $table->decimal("amount_per_kg");
      $table->decimal("total_amount");
      $table->foreignId("user_id")->constrained("users")->onUpdate("cascade")->onDelete("cascade");
      $table->foreignId("related_transaction_user_id")->constrained("users")->onUpdate("cascade")->onDelete("cascade");
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
