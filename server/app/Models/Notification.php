<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    use HasFactory;

    protected $guarded = [
        "id",
        "created_at",
        "updated_at"
    ];

    public function farmer()
    {
        return $this->belongsTo(User::class);
    }

    public function trash_manager()
    {
        return $this->belongsTo(TrashManager::class);
    }
}
