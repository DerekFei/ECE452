package teamece.uwaterloo.ece452;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderBoardActivity extends AppCompatActivity {
    private TextView leaderBoardResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_screen);

        leaderBoardResult = findViewById(R.id.leader_board_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String userId = "2";
        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> userCall = userApi.getUser(userId);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    leaderBoardResult.setText("Code: " + response.code());
                    return;
                }

                User currentUser = response.body();
                String content = "";
                content += "Name: " + currentUser.getName() + " ";
                content += "Score: " + currentUser.getScore() + "\n";

                leaderBoardResult.append(content);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("User Tag", t.getMessage());
                leaderBoardResult.setText(t.getMessage());
            }
        });

        LeaderBoardApi leaderBoardApi = retrofit.create(LeaderBoardApi.class);
        Call<List<LeaderBoard>> call = leaderBoardApi.getLeaderBoard();

        call.enqueue(new Callback<List<LeaderBoard>>() {
            @Override
            public void onResponse(Call<List<LeaderBoard>> call, Response<List<LeaderBoard>> response) {
                if (!response.isSuccessful()) {
                    leaderBoardResult.setText("Code: " + response.code());
                    return;
                }

                List<LeaderBoard> userRecords = response.body();
                for (LeaderBoard userRecord: userRecords) {
                    String content = "";
                    String userID = userRecord.getUserId();
                    int score = userRecord.getScore();

                    content += "Name: " + userRecord.getName() + " ";
                    content += "Score: " + score + "\n";

                    leaderBoardResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<LeaderBoard>> call, Throwable t) {
                Log.d("Leader Board Tag", t.getMessage());
                leaderBoardResult.setText(t.getMessage());
            }
        });
    }
}
