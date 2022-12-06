package umn.ac.id.project.maggot.global;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.model.UserModel;
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

        ApiService.endpoint().refreshToken(authorizationToken).enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if(response.isSuccessful()) {
                    AuthenticationModel.Result result = response.body().refreshToken();
                    userSharedPreference.setUser(result);

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
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
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
