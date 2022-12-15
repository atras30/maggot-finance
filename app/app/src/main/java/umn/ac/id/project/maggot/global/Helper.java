package umn.ac.id.project.maggot.global;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import umn.ac.id.project.maggot.R;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.retrofit.ApiErrorHandler;
import umn.ac.id.project.maggot.retrofit.ApiService;

public class Helper {
    public static String parseDate(Date time) {
        String pattern = "dd MMM yyyy, hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(time);

        return date;
    }

    public static void updateTrashManagerData(Context context) {

    }

    public static void refreshToken(Context context, View view) {
        UserSharedPreference userSharedPreference = new UserSharedPreference(context);
        String authorizationToken = "Bearer " + userSharedPreference.getToken();

        ApiService.endpoint().getUser(authorizationToken).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    UserModel.User result = response.body().getUser();
                    userSharedPreference.setUser(new AuthenticationModel.Result(userSharedPreference.getToken(), "", result, null));

                    //Update Data
                    TextView name = view.findViewById(R.id.name);
                    TextView address = view.findViewById(R.id.address);
                    TextView balance = view.findViewById(R.id.angkaSaldo);
                    UserSharedPreference userSharedPreference = new UserSharedPreference(context);
                    if (userSharedPreference.getUser() != null) {
                        name.setText(userSharedPreference.getUser().getFull_name());
                        address.setText(userSharedPreference.getUser().getAddress());
                        balance.setText("**********");
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.i("error 11", errorMessage);

                        if(ApiErrorHandler.getErrorMessage(errorMessage).equalsIgnoreCase("Unauthenticated.")) {
                            Toast.makeText(context, "Masalah: " + ApiErrorHandler.getErrorMessage(errorMessage), Toast.LENGTH_SHORT).show();
                            Log.i("User shared pref", new UserSharedPreference(context).toString());
                            new GoogleAccount(context).signOut();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(context, "Sedang ada masalah di jaringan kami. Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String formatRupiah(double number) {
        DecimalFormatSymbols formatid = new DecimalFormatSymbols();

        formatid.setMonetaryDecimalSeparator(',');
        formatid.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,##0.00", formatid);

        df.setDecimalFormatSymbols(formatid);

        return df.format(number);
    }
}
