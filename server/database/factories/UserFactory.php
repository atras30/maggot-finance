<?php

namespace Database\Factories;

use App\Models\TrashManager;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\User>
 */
class UserFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition()
    {
        $trashManagers = TrashManager::all();
        $maxTrashManagerIndex = $trashManagers->count()-1;

        return [
            'email' => fake()->unique()->safeEmail(),
            'full_name' => fake()->name(),
            'role' => fake()->randomElement(['farmer', 'shop']),
            'balance' => fake()->randomNumber(5, true),
            'phone_number' => fake()->phoneNumber(),
            'address' => fake()->address(),
            'trash_manager_id' => $trashManagers[fake()->numberBetween(0, $maxTrashManagerIndex)]
        ];
    }

    /**
     * Indicate that the model's email address should be unverified.
     *
     * @return static
     */
    public function unverified()
    {
        return $this->state(fn (array $attributes) => [
            'email_verified_at' => null,
        ]);
    }
}
