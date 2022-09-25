package umn.ac.id.maggotproject.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import umn.ac.id.maggotproject.model.PeternakModel;
import umn.ac.id.maggotproject.model.UserModel;

public interface ApiEndpoint {
    @GET("user")
    Call<UserModel> getUser();

    @GET("user/role/peternak")
    Call<PeternakModel> getPeternak();
}
