package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersApi {

    @GET("internal/users/current")
    Call<UserJson> currentUser(@Path("username") String username);

    @GET("internal/users/all")
    Call<UserJson> allUsers(@Path("username") String username,
                            @Path("searchQuery") String searchQuery);

    @POST("internal/users/update")
    Call<UserJson> updateUserInfo(@Body UserJson user);

    @POST("internal/invitations/send")
    Call<UserJson> sendInvitation(@Body String username,
                                  @Body String targetUsername);

    @POST("internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Body String username,
                                    @Body String targetUsername);

    @POST("internal/invitations/decline")
    Call<UserJson> declineInvitation(@Body String username,
                                     @Body String targetUsername);
}
