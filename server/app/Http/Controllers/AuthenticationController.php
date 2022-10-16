<?php

namespace App\Http\Controllers;

use App\Models\SuperAdmin;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Hash;

class AuthenticationController extends Controller {
  public function getUser() {
    return response()->json([
      "user" => auth()->user()
    ], Response::HTTP_OK);
  }

  public function loginUser(Request $request) {
    return $this->login($request, 'user');
  }

  public function loginTrashManager(Request $request) {
    return $this->login($request, 'trash manager');
  }

  public function loginSuperAdmin(Request $request) {
    return $this->login($request, 'super admin');
  }

  public function login($request, $role) {
    $validated = $request->validate([
      "email" => "string|required",
      "password" => "string|required"
    ]);

    $email = $username = $validated['email'];

    $user = User::where("email", $email)->orWhere("username", $username)->first();
    if(!$user) {
      $user = TrashManager::where("email", $email)->first();
    }
    if(!$user) {
      $user = SuperAdmin::where("email", $email)->first();
    }

    if (!$user || !Hash::check($validated['password'], $user->password)) {
      return response()->json([
        "message" => "Bad Credentials."
      ], Response::HTTP_OK);
    }

    $token = $user->createToken("login_token")->plainTextToken;

    return [
      'token' => $token,
      "user" => $user,
    ];
  }

  public function logout() {
    auth()->user()->tokens()->delete();

    return response()->json([
      'message' => 'Successfully logged out.'
    ], Response::HTTP_OK);
  }

  public function register() {
  }
}
