<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Laravel\Sanctum\HasApiTokens;
use App\Models\User;

class TrashManager extends Authenticatable {
  use HasFactory, HasApiTokens;

  protected $guarded = [
    "id",
    "created_at",
    "updated_at"
  ];

  public function users() {
    return $this->hasMany(User::class);
  }

  public function managed_by() {
    return $this->belongsTo(SuperAdmin::class, "super_admin_id");
  }
}
