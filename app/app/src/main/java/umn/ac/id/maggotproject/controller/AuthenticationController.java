package umn.ac.id.maggotproject.controller;

import android.util.Log;

import umn.ac.id.maggotproject.model.UserModel;

public class AuthenticationController {
    private String token;
    private String message;
    private UserModel.User user;

    public Result login() {
        return new Result(token, message, user);
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
}
