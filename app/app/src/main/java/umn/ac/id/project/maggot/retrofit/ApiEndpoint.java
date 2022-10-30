package umn.ac.id.project.maggot.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.UserModel;

public interface ApiEndpoint {
    @GET("user")
    Call<UserModel> getUser();

    @GET("user/role/farmer")
    Call<PeternakModel> getPeternak();

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthenticationModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("user")
    Call<AuthenticationModel> register(@Field("email") String email, @Field("password") String password);
}
