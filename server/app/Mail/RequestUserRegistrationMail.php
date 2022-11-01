<?php

namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;

class RequestUserRegistrationMail extends Mailable
{
    use Queueable, SerializesModels;

    private $name;
    private $role;

    /**
     * Create a new message instance.
     *
     * @return void
     */
    public function __construct($name, $role)
    {
        $this->name = $name;
        $this->role = $role;
    }

    /**
     * Build the message.
     *
     * @return $this
     */
    public function build()
    {
        return $this->view('mail.request_user_registration_mail', [
            "name" => $this->name,
            "role" => $this->role
        ]);
    }
}
