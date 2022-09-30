<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;

class SuperAdmin extends Authenticatable {
  use HasFactory;

  protected $guarded = [
    "id",
    "created_at",
    "updated_at"
  ];

  public function trash_managers() {
    return $this->hasMany(TrashManager::class);
  }
}
