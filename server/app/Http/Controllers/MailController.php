<?php

namespace App\Http\Controllers;

use App\Mail\RegistrationMail;
use App\Mail\TransactionMail;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;

class MailController extends Controller {
  public function sendRegistrationMail() {
    Mail::to("atrasshalhan@gmail.com")->send(new RegistrationMail("Atras Shalhan"));

    return response()->json([
      "data" => "Email sucecssfully sent"
    ]);
  }

  public function sendTransactionBill() {
    Mail::to("atrasshalhan@gmail.com")->send(new TransactionMail());

    return response()->json([
      "data" => "Email successfully sent"
    ]);
  }
}
