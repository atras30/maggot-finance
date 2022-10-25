<?php

namespace App\Http\Controllers;

use App\Mail\RegistrationMail;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;

class MailController extends Controller {
  public function sendRegistrationMail() {
    Mail::to("atrasshalhan@gmail.com")->send(new RegistrationMail("Atras Shalhan"));

    return response()->json([
      "data" => "Email sucecssfully sent"
    ]);
  }
}
