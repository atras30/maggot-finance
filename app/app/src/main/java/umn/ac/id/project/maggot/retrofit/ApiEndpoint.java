package umn.ac.id.project.maggot.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import umn.ac.id.project.maggot.model.ApprovalRejectionModel;
import umn.ac.id.project.maggot.model.AuthenticationModel;
import umn.ac.id.project.maggot.model.PeternakModel;
import umn.ac.id.project.maggot.model.TransactionModel;
import umn.ac.id.project.maggot.model.TrashManagerModel;
import umn.ac.id.project.maggot.model.UserModel;
import umn.ac.id.project.maggot.model.WarungModel;

public interface ApiEndpoint {
    @GET("auth/user")
    Call<UserModel> getUser(@Header("Authorization") String authorization);

    @GET("user/role/farmer")
    Call<PeternakModel> getPeternak();

    @GET("user/role/farmer")
    Call<UserModel> getUsers();

    @GET("user/role/shop")
    Call<WarungModel> getWarung();

    @GET("trash-manager")
    Call<TrashManagerModel> getTrashManager();

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("transaction/farmer/buy/shop")
    Call<TransactionModel> farmerBuyFromShop(@Header("Authorization") String authorization, @Field("total_amount") double totalAmount, @Field("shop_email") String shopEmail, @Field("description") String description);

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthenticationModel> login(@Field("email") String email);

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthenticationModel> loginTrashManager(@Field("email") String email);

    @FormUrlEncoded
    @POST("register/user")
    Call<AuthenticationModel> registerUser(@Field("full_name") String fullName, @Field("email") String email, @Field("role") String role, @Field("trash_manager_id") int trashManagerId, @Field("address") String address, @Field("phone_number") String phoneNumber);

    @FormUrlEncoded
    @POST("register/user/approve")
    Call<ApprovalRejectionModel> approvalUserRegistration(@Field("email") String email);

    @FormUrlEncoded
    @POST("register/user/reject")
    Call<ApprovalRejectionModel> rejectionUserRegistration(@Field("email") String email);

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("transaction/trash-manager/buy/maggot")
    Call<TransactionModel> buyMaggot(@Header("Authorization") String authorizationToken,@Field("weight_in_kg") double weightInKg, @Field("amount_per_kg") double amountPerKg, @Field("farmer_email") String farmerEmail, @Field("description") String description);

    @GET("transactions")
    Call<TransactionModel> getTransactions(@Query("email") String email);
}
