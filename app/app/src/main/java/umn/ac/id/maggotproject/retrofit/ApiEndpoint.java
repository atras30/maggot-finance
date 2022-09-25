package umn.ac.id.maggotproject.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import umn.ac.id.maggotproject.model.AuthenticationModel;
import umn.ac.id.maggotproject.model.PeternakModel;
import umn.ac.id.maggotproject.model.UserModel;

public interface ApiEndpoint {
    @GET("user")
    Call<UserModel> getUser();

    @GET("user/role/peternak")
    Call<PeternakModel> getPeternak();

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthenticationModel> login(@Field("email") String email, @Field("password") String password);
}
