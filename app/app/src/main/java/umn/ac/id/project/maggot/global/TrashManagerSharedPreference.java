package umn.ac.id.project.maggot.global;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.model.UserModel;

public class TrashManagerSharedPreference {
    private final String USER_SETTINGS_KEY = "umn.ac.id.project.maggot.USER_SETTINGS_KEY";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public TrashManagerSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_SETTINGS_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public TrashManagerModel.TrashManagers getTrashManager() {
        return (TrashManagerModel.TrashManagers) new Gson().fromJson(this.sharedPreferences.getString("authenticated_trash_manager", null), TrashManagerModel.TrashManagers.class);
    }

    public String getToken() {
        return sharedPreferences.getString("authenticated_trash_manager_token", null);
    }

    public void setTrashManager(AuthenticationModel.ResultTrashManager result) {
        editor.putString("authenticated_trash_manager", new Gson().toJson(result.getTrashManager()));
        editor.putString("authenticated_trash_manager_token", result.getToken());
        editor.commit();
    }

    public void logout() {
        editor.remove("authenticated_trash_manager");
        editor.remove("authenticated_trash_manager_token");
        editor.commit();
    }
}