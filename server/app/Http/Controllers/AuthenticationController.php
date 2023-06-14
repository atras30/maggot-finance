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
    /**
     * Refresh token
     *
     * @OA\Post(
     *   path="/api/auth/token/refresh",
     *  tags={"Authentication"},
     * summary="Refresh token",
     * description="Refresh token",
     * operationId="RefreshToken",
     * security={{"sanctum":{}}},
     * @OA\Response(
     * response=200,
     * description="Success",
     * @OA\JsonContent(
     * @OA\Property(property="token", type="string", example="token"),
     * @OA\Property(property="user", type="object", ref="#/components/schemas/User"),
     * ),
     * ),
     * @OA\Response(
     * response=401,
     * description="Unauthenticated.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated."),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Server Error"),
     * ),
     * ),
     * ),
     *
     */
    public function refreshToken(Request $request)
    {
        $user = auth()->user();
        auth()
            ->user()
            ->currentAccessToken()
            ->delete();

        $token = $user->createToken("login_token")->plainTextToken;

        return response()->json(
            [
                "token" => $token,
                "user" => $user,
            ],
            Response::HTTP_OK
        );
    }

    /**
     * Get logged in user data
     *
     * @OA\Get(
     *  path="/api/auth/user",
     * tags={"Authentication"},
     * summary="Get logged in user data",
     * description="Get logged in user data",
     * operationId="GetLoggedInUserData",
     * security={{"sanctum":{}}},
     * @OA\Response(
     * response=200,
     * description="Success",
     * @OA\JsonContent(
     * @OA\Property(property="user", type="object", ref="#/components/schemas/User"),
     * ),
     * ),
     * @OA\Response(
     * response=401,
     * description="Unauthenticated.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated."),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Server Error",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Server Error"),
     * ),
     * ),
     * )
     */
    public function getUser()
    {
        return response()->json(
            [
                "user" => auth()->user(),
            ],
            Response::HTTP_OK
        );
    }

    /**
     * Super admin login
     *
     * @OA\Post(
     *    path="/api/auth/login/super-admin",
     *  tags={"Authentication"},
     *  summary="Super admin login",
     * description="Super admin login",
     * operationId="SuperAdminLogin",
     * @OA\RequestBody(
     *  required=true,
     * description="Pass super admin's email and password",
     * @OA\JsonContent(
     * required={"email", "password"},
     * @OA\Property(property="email", type="string", example="admin@mail.com"),
     * @OA\Property(property="password", type="string", example="password"),
     * ),
     * ),
     * @OA\Response(
     * response=200,
     * description="Success",
     * @OA\JsonContent(
     * @OA\Property(property="token", type="string", example="token"),
     * @OA\Property(property="super_admin", type="object", ref="#/components/schemas/SuperAdmin"),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="Incorrect email or password.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Incorrect email or password."),
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="Validation error.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="The given data was invalid."),
     * @OA\Property(property="errors", type="object", example={
     *  "email": { "The email must be a valid email address." },
     * "password": { "The password field is required." },
     * }),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Server error.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Server error."),
     * ),
     * ),
     * ),
     *
     */
    public function loginSuperAdmin(Request $request)
    {
        $validated = $request->validate([
            "email" => "string|required",
            "password" => "string|required",
        ]);

        $superAdmin = SuperAdmin::where("email", $validated["email"])
            ->get()
            ->first();

        if (
            !$superAdmin ||
            !Hash::check($validated["password"], $superAdmin->password)
        ) {
            return response()->json(
                [
                    "message" => "Incorrect email or password.",
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $token = $superAdmin->createToken("login_token")->plainTextToken;

        return response()->json([
            "token" => $token,
            "super_admin" => $superAdmin,
        ]);
    }

    /**
     * Verifying the google token and login the user.
     *
     * @OA\Post(
     *     path="/api/auth/login",
     *    tags={"Authentication"},
     *   summary="Verifying the google token and login the user.",
     *  description="Verifying the google token and login the user.",
     * operationId="VerifyGoogleTokenAndLoginUser",
     *  @OA\RequestBody(
     *   required=true,
     *  description="Pass user's google token",
     * @OA\JsonContent(
     *  required={"google_token"},
     * @OA\Property(property="google_token", type="string", example="google_token"),
     * ),
     * ),
     * @OA\Response(
     * response=200,
     * description="Success",
     * @OA\JsonContent(
     * @OA\Property(property="token", type="string", example="token"),
     * @OA\Property(property="user", type="object", ref="#/components/schemas/User"),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="User was not found.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="User was not found."),
     * ),
     * ),
     * ),
     * )
     */
    public function login(Request $request)
    {
        $validated = $request->validate([
            "google_token" => "string|required",
        ]);

        $client = new Client(["client_id" => env("GOOGLE_CLIENT_ID")]);
        $googleClient = $client->getClient();
        $user = $googleClient->verifyIdToken($validated["google_token"]);

        $email = $user["email"];

        $user = User::where("email", $email)->first();
        if (!$user) {
            $user = TrashManager::where("email", $email)->first();
        }

        if (!$user) {
            return response()->json(
                [
                    "message" => "User was not found.",
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $token = $user->createToken("login_token")->plainTextToken;

        if ($user->role == "trash_manager") {
            return response()->json([
                "token" => $token,
                "trash_manager" => $user,
            ]);
        } else {
            return response()->json([
                "token" => $token,
                "user" => $user,
            ]);
        }
    }

    /**
     * Logout the user
     *
     * @OA\Post(
     *    path="/api/auth/logout",
     * tags={"Authentication"},
     * summary="Logout the user",
     * description="Remove the user's token from the database",
     * operationId="LogoutUser",
     * security={{"sanctum":{}}},
     * @OA\Response(
     * response=200,
     * description="Success",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Successfully logged out."),
     * ),
     * ),
     * @OA\Response(
     * response=401,
     * description="Unauthenticated.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Unauthenticated."),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Server error.",
     * @OA\JsonContent(
     * @OA\Property(property="message", type="string", example="Server error."),
     * ),
     * ),
     * ),
     * )
     */
    public function logout()
    {
        auth()
            ->user()
            ->currentAccessToken()
            ->delete();

        return response()->json(
            [
                "message" => "Successfully logged out.",
            ],
            Response::HTTP_OK
        );
    }
}
