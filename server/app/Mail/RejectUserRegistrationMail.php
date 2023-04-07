<?php

namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;

class RejectUserRegistrationMail extends Mailable
{
    use Queueable, SerializesModels;

    private $full_name;

    /**
     * Create a new message instance.
     *
     * @return void
     */
    public function __construct($full_name)
    {
        $this->full_name = $full_name;
    }

    /**
     * Build the message.
     *
     * @return $this
     */
    public function build()
    {
        return $this->subject("Rejection account request for Maggot Finance's Account")->view('mail.reject_user_registration', [
            "full_name" => $this->full_name
        ]);
    }
}
