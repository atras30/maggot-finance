<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;
use App\Models\User;

class UserController extends Controller {
  public function index() {
    $users = User::all();

    return response()->json([
      "users" => $users
    ], Response::HTTP_OK);
  }

  public function getPengepul() {
    $users = User::where("role", "pengepul")->get();

    return response()->json([
      "users" => $users
    ], Response::HTTP_OK);
  }

  public function getWarung() {
    $users = User::where("role", "warung")->get();

    return response()->json([
      "users" => $users
    ], Response::HTTP_OK);
  }
  public function getPeternak() {
    $users = User::where("role", "peternak")->get();

    return response()->json([
      "users" => $users
    ], Response::HTTP_OK);
  }

  public function store(Request $request) {
    $validated = $request->validate([
      "full_name" => "string|required",
      "username" => "string|required|unique:users,username|not_in:pengepul,peternak,warung|alpha_dash",
      "email" => "string|required|email:rfc,dns|unique:users,email",
      "password" => "string|required",
      "role" => "string|required|in:pengepul,peternak,warung"
    ]);

    try {
      User::create($validated);
    } catch(\Exception $e) {
      return response()->json([
        "error" => $e->getMessage()
      ], Response::HTTP_INTERNAL_SERVER_ERROR);
    }

    return response()->json([
        "message" => "User was successfully created."
      ], Response::HTTP_CREATED);
  }

  public function edit(Request $request, $id) {
    $user = User::findOrFail($id);

    $validated = $request->validate([
      "full_name" => "string",
      "username" => "string|unique:users,username,{$id}",
      "email" => "string|unique:users,email,{$id}",
      "password" => "string",
      "role" => "string|in:pengepul,peternak,warung"
    ]);

    if(isset($validated['password'])) {
      $validated['password'] = bcrypt($request->password);
    }

    try {
      $user->update($validated);
    } catch(\Exception $e) {
      return response()->json([
        "error" => $e->getMessage()
      ], Response::HTTP_INTERNAL_SERVER_ERROR);
    }

    return response()->json([
        "message" => "User was successfully updated."
      ], Response::HTTP_INTERNAL_SERVER_ERROR);
  }
}
