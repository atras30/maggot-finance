package umn.ac.id.project.maggot.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import umn.ac.id.project.maggot.LoginActivity;

public class GoogleAccount {
    private Context context;
    private GoogleSignInClient gsc;

    public GoogleAccount(Context context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();;
        gsc = GoogleSignIn.getClient(context, gso);
    }

    public void signOut() {
        this.gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                new UserSharedPreference(context).logout();
                Toast.makeText(context, "Anda telah keluar!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity)context).finish();
            }
        });
    }
}
