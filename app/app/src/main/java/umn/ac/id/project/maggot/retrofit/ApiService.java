package umn.ac.id.project.maggot.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    public static String localhost = "http://10.0.2.2:8000/api/";
    public static String sharedhosting = "https://atras.my.id/api/";
    public static String production = "https://magfin-api.lppmumn.id/public/api/";

    private static String BASE_URL = production;
    private static Retrofit retrofit;

    public static ApiEndpoint endpoint() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiEndpoint.class);
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}