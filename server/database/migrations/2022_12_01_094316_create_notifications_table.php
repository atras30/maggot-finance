<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('notifications', function (Blueprint $table) {
            $table->id();
            $table->enum('type', ["payment_confirmation"]);
            $table->string('token');
            $table->string('description')->default("");
            $table->double('weight_in_kg')->default(0);
            $table->double('amount_per_kg')->default(0);
            $table->foreignId("farmer_id")->constrained("users", "id")->onUpdate("cascade")->onDelete("cascade");
            $table->foreignId("trash_manager_id")->constrained()->onUpdate("cascade")->onDelete("cascade");
            $table->timestamp('expired_at');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('notifications');
    }
};
