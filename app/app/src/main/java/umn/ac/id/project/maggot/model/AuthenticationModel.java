package umn.ac.id.project.maggot.model;

import android.util.Log;

import umn.ac.id.project.maggot.model.UserModel;

public class AuthenticationModel {
    private String token;
    private String message;
    private UserModel.User user;
    private TrashManagerModel.TrashManagers trash_manager;

    public Result login() {
        return new Result(token, message, user);
    }

    public ResultTrashManager loginTrashManager() {
        return new ResultTrashManager(token, message, trash_manager);
    }

    public String registerUser() {
        return message;
    }

    public class Result {
        private String token;
        private UserModel.User user;
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

        public Result(String token, String message, UserModel.User user) {
            this.token = token;
            this.message = message;
            this.user = user;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "token='" + token + '\'' +
                    ", user=" + user +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    //for trash manager login
    public class ResultTrashManager {
        private String token;
        private TrashManagerModel.TrashManagers trashManager;
        private String message;

        public String getToken() {
            return token;
        }

        public TrashManagerModel.TrashManagers getTrashManager() {
            return trashManager;
        }

        public String getMessage() {
            return message;
        }

        public ResultTrashManager(String token, String message, TrashManagerModel.TrashManagers trashManager) {
            this.token = token;
            this.message = message;
            this.trashManager = trashManager;
        }

        @Override
        public String toString() {
            return "ResultTrashManager{" +
                    "token='" + token + '\'' +
                    ", trashManager=" + trashManager +
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
