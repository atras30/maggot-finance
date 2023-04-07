<?php

namespace App\Models;

// use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;
use App\Models\Transaction;
use Illuminate\Database\Eloquent\SoftDeletes;

class User extends Authenticatable {
  use HasApiTokens, HasFactory, Notifiable, SoftDeletes;

  /**
   * The attributes that are mass assignable.
   *
   * @var array<int, string>
   */
  protected $guarded = [
    "id",
    "created_at",
    "updated_at"
  ];

  function transactions() {
    return $this->hasMany(Transaction::class);
  }

  function shop_transactions() {
    return $this->hasMany(Transaction::class);
  }

  public function debts() {
    return $this->hasMany(Debt::class);
  }

  public function trash_manager() {
    return $this->belongsTo(TrashManager::class);
  }

  public function getTrashManagerNameAttribute() {
    return $this->trash_manager->nama_pengelola;
  }
}
