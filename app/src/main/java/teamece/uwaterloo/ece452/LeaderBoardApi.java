package teamece.uwaterloo.ece452;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LeaderBoardApi {

    @GET("leaderboard")
    Call<List<LeaderBoard>> getLeaderBoard();
}
