<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Laravel\Sanctum\HasApiTokens;

class SuperAdmin extends Authenticatable {
  use HasFactory, HasApiTokens;

  protected $guarded = [
    "id",
    "created_at",
    "updated_at"
  ];

  protected $with = [
    "trash_managers"
  ];

  public function trash_managers() {
    return $this->hasMany(TrashManager::class);
  }
}
