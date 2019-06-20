package teamece.uwaterloo.ece452;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApi {

    @GET("user")
    Call<User> getUser(@Query("userId") String userId);
}
