package umn.ac.id.project.maggot.global;

import umn.ac.id.project.maggot.model.TrashManagerModel;

public class AuthenticatedTrashManager {
    private static String token = null;
    private static TrashManagerModel.TrashManagers authenticatedTrashManager = null;

    public static void setTrashManager(TrashManagerModel.TrashManagers trashManager, String serverToken) {
        authenticatedTrashManager = trashManager;
        token = serverToken;
    }

    public static String getToken() {
        return token;
    }

    public static TrashManagerModel.TrashManagers getTrashManager() {
        return authenticatedTrashManager;
    }

    public static void logout() {
        authenticatedTrashManager = null;
        token = null;
    }
}
