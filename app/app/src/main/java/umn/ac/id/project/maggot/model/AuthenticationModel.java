package umn.ac.id.project.maggot.model;

import android.util.Log;

public class AuthenticationModel {
    private String token;
    private String message;
    private UserModel.User user;
    private TrashManagerModel.TrashManagers trash_manager;

    public Result login() {
        Log.i("Login", "LOGIN");
        return new Result(token, message, user, trash_manager);
    }

    public String logout() {
        return message;
    }

    public Result refreshToken() {
        return new Result(token, message, user, trash_manager);
    }

    public String registerUser() {
        return message;
    }

    public class Result {
        private String token;
        private UserModel.User user;
        private TrashManagerModel.TrashManagers trash_manager;
        private String message;

        public String getToken() {
            return token;
        }

        public UserModel.User getUser() {
            return user;
        }

        public String getMessage() {
            return message;
        }

        public Result(String token, String message, UserModel.User user, TrashManagerModel.TrashManagers trash_manager) {
            this.token = token;
            this.message = message;
            this.user = user;
            this.trash_manager = trash_manager;
        }

        public TrashManagerModel.TrashManagers getTrash_manager() {
            return trash_manager;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "token='" + token + '\'' +
                    ", user=" + user +
                    ", trash_manager=" + trash_manager +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    public class ErrorHandler {
        private String message;

        public ErrorHandler(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
