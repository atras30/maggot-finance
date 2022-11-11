package umn.ac.id.project.maggot.global;

import umn.ac.id.project.maggot.model.UserModel;

public class AuthenticatedUser {
//    private static String token = null;
    private static UserModel.User authenticatedUser = null;

    public static void setUser(UserModel.User user) {
        authenticatedUser = user;
//        token = serverToken;
    }

//    public static String getToken() {
//        return token;
//    }

    public static UserModel.User getUser() {
        return authenticatedUser;
    }

    public static void logout() {
        authenticatedUser = null;
//        token = null;
    }
}
