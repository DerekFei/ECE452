package teamece.uwaterloo.ece452;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostScoreApi {
    @Multipart
    @POST("/user")
    Call<PostScore> postScore(@Part("userId") String userId,
                              @Part("name") String name,
                              @Part("score") int score);
}
