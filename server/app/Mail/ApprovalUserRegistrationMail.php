<?php

namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;

class ApprovalUserRegistrationMail extends Mailable {
  use Queueable, SerializesModels;

  private $full_name;

  /**
   * Create a new message instance.
   *
   * @return void
   */
  public function __construct($full_name) {
    $this->full_name = $full_name;
  }

  /**
   * Build the message.
   *
   * @return $this
   */
  public function build() {
    return $this->subject("Maggot Finance's Account has been activated!")->view('mail.approval_user_registration', [
      "full_name" => $this->full_name
    ]);
  }
}
