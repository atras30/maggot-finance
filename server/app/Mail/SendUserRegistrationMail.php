<?php

namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;

class SendUserRegistrationMail extends Mailable
{
    use Queueable, SerializesModels;

    private $createdUser;
    private $trashManager;

    /**
     * Create a new message instance.
     *
     * @return void
     */
    public function __construct($createdUser, $trashManager)
    {
        $this->createdUser = $createdUser;
        $this->trashManager = $trashManager;
    }

    /**
     * Build the message.
     *
     * @return $this
     */
    public function build()
    {
        return $this->view('mail.user_registration', [
            "createdUser" => $this->createdUser,
            "trashManager" => $this->trashManager
        ]);
    }
}
