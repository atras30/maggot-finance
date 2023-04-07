<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;

class SuperAdminController extends Controller {
  public function getAllData(Request $request) {
    return response()->json([
      "trash_managers" => auth()->user()
    ], Response::HTTP_OK);
  }
}
