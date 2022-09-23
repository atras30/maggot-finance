<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;
use App\Models\User;

class UserController extends Controller {
  public function index() {
    return response()->json([
      "users" => User::all()
    ], Response::HTTP_OK);
  }

  public function getUserByUsername($username) {
    $user = User::where("username", $username)->get();

    if (!$user->count()) {
      return response()->json([
        "message" => "User {$username} was not found."
      ], Response::HTTP_OK);
    }

    return response()->json([
      "user" => $user
    ], Response::HTTP_OK);
  }

  public function getUserByRole($role) {
    $validRoles = ["pengepul", "warung", "peternak"];

    if (!in_array($role, $validRoles)) {
      return response()->json([
        "message" => "Invalid Role! Role must be either 'pengepul', 'peternak', or 'warung'"
      ], Response::HTTP_NOT_ACCEPTABLE);
    }

    $users = User::where("role", $role)->get();

    return response()->json([
      "total_record" => $users->count(),
      "users" => $users,
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
    } catch (\Exception $e) {
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

    if (isset($validated['password'])) {
      $validated['password'] = bcrypt($request->password);
    }

    try {
      $user->update($validated);
    } catch (\Exception $e) {
      return response()->json([
        "error" => $e->getMessage()
      ], Response::HTTP_INTERNAL_SERVER_ERROR);
    }

    return response()->json([
      "message" => "User was successfully updated."
    ], Response::HTTP_INTERNAL_SERVER_ERROR);
  }
}
