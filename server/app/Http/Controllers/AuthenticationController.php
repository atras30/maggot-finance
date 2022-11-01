<?php

namespace App\Http\Controllers;

use App\Mail\RequestUserRegistrationMail;
use App\Models\SuperAdmin;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class AuthenticationController extends Controller
{
    public function getUser()
    {
        return response()->json([
            "user" => auth()->user()
        ], Response::HTTP_OK);
    }

    public function login(Request $request)
    {
        $validated = $request->validate([
            "email" => "string|required",
            "password" => "string|required"
        ]);

        $email = $validated['email'];

        $user = User::where("email", $email)->first();
        if (!$user) {
            $user = TrashManager::where("email", $email)->first();
        }
        if (!$user) {
            $user = SuperAdmin::where("email", $email)->first();
        }

        // if (!$user || !Hash::check($validated['password'], $user->password)) {
        //   return response()->json([
        //     "message" => "Bad Credentials."
        //   ], Response::HTTP_OK);
        // }

        $token = $user->createToken("login_token")->plainTextToken;

        return [
            'token' => $token,
            "user" => $user,
        ];
    }

    public function logout()
    {
        auth()->user()->tokens()->delete();

        return response()->json([
            'message' => 'Successfully logged out.'
        ], Response::HTTP_OK);
    }
}
