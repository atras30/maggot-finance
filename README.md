## Maggot Project

### Cloning Projects
1. git clone https://github.com/atras30/maggot-project-server.git
2. Copy .env.example in /server folder and rename it to .env
3. Go to /server folder (cd ./maggot-project-server)
4. in terminal, type 'composer install' to install all laravel dependencies
5. in terminal, type 'php artisan key:generate' to install key for .env file
6. in terminal, type 'php artisan migrate:fresh --seed' to migrate all database schemas
7. in terminal, type 'php artisan serve' to run the server