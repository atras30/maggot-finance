<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;

class SuperAdminController extends Controller {
  public function getTrashManagers(Request $request) {
    return response()->json([
      "message" => "successfully fetched all trash managers.",
      "trash_managers" => auth()->user()->trash_managers
    ], Response::HTTP_OK);
  }
}
