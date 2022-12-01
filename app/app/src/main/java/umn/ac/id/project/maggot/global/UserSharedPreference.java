package umn.ac.id.project.maggot.global;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.UserModel;

public class UserSharedPreference {
    private final String USER_SETTINGS_KEY = "umn.ac.id.project.maggot.USER_SETTINGS_KEY";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_SETTINGS_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

//    getSharedPreferences("foodro", Context.MODE_PRIVATE);
//    sharedPreferences.getString("authenticated_user", null);
//    editor = sharedPreferences.edit();
//    editor.putString("authenticated_user", new Gson().toJson(result.getUser()));
//    editor.commit();

    public UserModel.User getUser() {
        return (UserModel.User) new Gson().fromJson(this.sharedPreferences.getString("authenticated_user", null), UserModel.User.class);
    }

    public String getToken() {
        return sharedPreferences.getString("authenticated_user_token", null);
    }

    public void setUser(AuthenticationModel.Result result) {
        editor.putString("authenticated_user", new Gson().toJson(result.getUser()));
        editor.putString("authenticated_user_token", result.getToken());
        editor.commit();
    }

    public void logout() {
        editor.remove("authenticated_user");
        editor.remove("authenticated_user_token");
        editor.commit();
    }
}
