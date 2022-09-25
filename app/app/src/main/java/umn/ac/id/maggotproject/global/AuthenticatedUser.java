package umn.ac.id.maggotproject.global;

import umn.ac.id.maggotproject.model.UserModel;

public class AuthenticatedUser {
    private static UserModel.User authenticatedUser = null;

    public static void setUser(UserModel.User user) {
        authenticatedUser = user;
    }

    public static UserModel.User getUser() {
        return authenticatedUser;
    }

    public static void logout() {
        authenticatedUser = null;
    }
}
