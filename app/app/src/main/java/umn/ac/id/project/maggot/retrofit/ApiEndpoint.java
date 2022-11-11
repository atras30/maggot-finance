package umn.ac.id.project.maggot.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import umn.ac.id.project.maggot.model.ApprovalRejectionModel;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.model.WarungModel;

public interface ApiEndpoint {
    @GET("user")
    Call<UserModel> getUser();

    @GET("user/role/farmer")
    Call<PeternakModel> getPeternak();

    @GET("user/role/farmer")
    Call<UserModel> getUsers();

    @GET("user/role/shop")
    Call<WarungModel> getWarung();

    @GET("trash-manager")
    Call<TrashManagerModel> getTrashManager();

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthenticationModel> login(@Field("email") String email);

    @FormUrlEncoded
    @POST("register/user")
    Call<AuthenticationModel> registerUser(@Field("full_name") String fullName, @Field("email") String email, @Field("role") String role, @Field("trash_manager_id") int trashManagerId, @Field("address") String address, @Field("phone_number") String phoneNumber);

    @FormUrlEncoded
    @POST("register/user/approve")
    Call<ApprovalRejectionModel> approvalUserRegistration(@Field("email") String email);

    @FormUrlEncoded
    @POST("register/user/reject")
    Call<ApprovalRejectionModel> rejectionUserRegistration(@Field("email") String email);
}
