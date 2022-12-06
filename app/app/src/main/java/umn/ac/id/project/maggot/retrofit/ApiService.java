package umn.ac.id.project.maggot.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
//    Localhost : http://10.0.2.2:8000/api/
//    Internet : https://atras.my.id/api/
//    Production : https://magfin-api.lppmumn.id/public/api/
    private static String BASE_URL = "https://magfin-api.lppmumn.id/public/api/";
    private static Retrofit retrofit;

    public static ApiEndpoint endpoint() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiEndpoint.class);
    }
}