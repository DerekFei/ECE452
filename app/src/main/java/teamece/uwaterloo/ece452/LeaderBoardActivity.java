package teamece.uwaterloo.ece452;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderBoardActivity extends AppCompatActivity {
    private TextView userName;
    private TextView userScore;
    private TextView myName;
    private TextView myScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_leaderboard_screen);

        userName = findViewById(R.id.userName);
        userScore = findViewById(R.id.userScore);
        myName = findViewById(R.id.myName);
        myScore = findViewById(R.id.myScore);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://54.147.208.46/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String userId = "2";
        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> userCall = userApi.getUser(userId);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                User currentUser = response.body();
                String name = "";
                String score = "";
                name += currentUser.getName() + "\n";
                score +=  currentUser.getScore() + "\n";

                myName.append(name);
                myScore.append(score);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("User Tag", t.getMessage());
            }
        });

        LeaderBoardApi leaderBoardApi = retrofit.create(LeaderBoardApi.class);
        Call<List<LeaderBoard>> call = leaderBoardApi.getLeaderBoard();

        call.enqueue(new Callback<List<LeaderBoard>>() {
            @Override
            public void onResponse(Call<List<LeaderBoard>> call, Response<List<LeaderBoard>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                List<LeaderBoard> userRecords = response.body();
                for (LeaderBoard userRecord: userRecords) {
                    String name = "";
                    String score = "";
                    name += userRecord.getName() + "\n";
                    score +=  userRecord.getScore() + "\n";

                    userName.append(name);
                    userScore.append(score);
                }
            }

            @Override
            public void onFailure(Call<List<LeaderBoard>> call, Throwable t) {
                Log.d("Leader Board Tag", t.getMessage());
            }
        });
    }
}
