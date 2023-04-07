<?php

namespace App\Http\Controllers;

use App\Mail\RequestUserRegistrationMail;
use App\Models\SuperAdmin;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Hash;
use PulkitJalan\Google\Client;

class AuthenticationController extends Controller
{
    public function refreshToken(Request $request) {
        $user = auth()->user();
        auth()->user()->currentAccessToken()->delete();

        $token = $user->createToken("login_token")->plainTextToken;

        return response()->json([
            'token' => $token,
            "user" => $user,
        ], Response::HTTP_OK);
    }

    public function getUser()
    {
        return response()->json([
            "user" => auth()->user()
        ], Response::HTTP_OK);
    }

    public function loginSuperAdmin(Request $request) {
        $validated = $request->validate([
            "email" => "string|required",
            "password" => "string|required"
        ]);

        $superAdmin = SuperAdmin::where("email", $validated['email'])->get()->first();

        if(!$superAdmin || !Hash::check($validated['password'], $superAdmin->password)) {
            return response()->json([
                "message" => "Incorrect email or password."
            ], Response::HTTP_NOT_FOUND);
        }

        $token = $superAdmin->createToken("login_token")->plainTextToken;

        return response()->json([
            "token" => $token,
            "super_admin" => $superAdmin
        ]);
    }

    public function login(Request $request)
    {
        $validated = $request->validate([
            "google_token" => "string|required"
        ]);

        $client = new Client(['client_id' => env("GOOGLE_CLIENT_ID")]);
        $googleClient = $client->getClient();
        $user = $googleClient->verifyIdToken($validated['google_token']);

        $email = $user['email'];

        $user = User::where("email", $email)->first();
        if (!$user) {
            $user = TrashManager::where("email", $email)->first();
        }

        if (!$user) {
            return response()->json([
                "message" => "User was not found."
            ], Response::HTTP_NOT_FOUND);
        }

        $token = $user->createToken("login_token")->plainTextToken;

        if($user->role == "trash_manager") {
            return response()->json([
                'token' => $token,
                "trash_manager" => $user,
            ]);
        } else {
            return response()->json([
                'token' => $token,
                "user" => $user,
            ]);
        }
    }

    public function logout()
    {
        auth()->user()->currentAccessToken()->delete();

        return response()->json([
            'message' => 'Successfully logged out.'
        ], Response::HTTP_OK);
    }
}
