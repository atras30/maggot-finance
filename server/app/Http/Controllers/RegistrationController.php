<?php

namespace App\Http\Controllers;

use App\Mail\ApprovalUserRegistrationMail;
use App\Mail\RejectUserRegistrationMail;
use App\Mail\RequestUserRegistrationMail;
use App\Models\TrashManager;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;
use Illuminate\Http\Response;

class RegistrationController extends Controller
{
    public function registerUser(Request $request)
    {
        $validated = $request->validate([
            "full_name" => "string|required",
            // "username" => "string|required|unique:users,username|not_in:pengepul,peternak,warung|alpha_dash",
            "email" => "string|required|email:rfc,dns|unique:users,email",
            // "password" => "string|required",
            "role" => "string|required|in:farmer,shop",
            "trash_manager_id" => "numeric|required"
        ], [
            "role.in" => "Role must be either 'farmer' or 'shop'"
        ]);

        // $validated['password'] = bcrypt($validated['password']);

        try {
            $createdUser = User::create($validated);
            $trashManager = TrashManager::findOrFail($validated['trash_manager_id']);
            Mail::to($trashManager->email)->send(new RequestUserRegistrationMail($createdUser->full_name, $createdUser->role));
        } catch (\Exception $e) {
            return response()->json([
                "error" => $e->getMessage()
            ], Response::HTTP_INTERNAL_SERVER_ERROR);
        }

        return response()->json([
            "message" => "User was successfully created."
        ], Response::HTTP_CREATED);
    }

    public function approveUserRequest(Request $request)
    {
        $validated = $request->validate([
            "email" => "string|required"
        ]);

        try {
            $user = User::where("email", $validated['email'])->get()->first();
            $user->email_verified_at = now();
            $user->save();
            Mail::to($user->email)->send(new ApprovalUserRegistrationMail($user->full_name));
            return response()->json([
                "message" => "Approval request success."
            ]);
        } catch (\Exception $e) {
            return response()->json([
                "message" => $e->getMessage()
            ], Response::HTTP_INTERNAL_SERVER_ERROR);
        }
    }

    public function rejectUserRequest(Request $request)
    {
        $validated = $request->validate([
            "email" => "string|required"
        ]);

        try {
            $user = User::where("email", $validated['email'])->get()->first();
            $user->delete();
            Mail::to($user->email)->send(new RejectUserRegistrationMail($user->full_name));
            return response()->json([
                "message" => "Reject request success."
            ]);
        } catch (\Exception $e) {
            return response()->json([
                "message" => $e->getMessage()
            ], Response::HTTP_INTERNAL_SERVER_ERROR);
        }
    }

    public function approveTrashManagerRequest()
    {
    }

    public function rejectTrashManagerRequest()
    {
    }
}
