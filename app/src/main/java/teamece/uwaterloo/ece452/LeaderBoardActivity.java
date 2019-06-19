package teamece.uwaterloo.ece452;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                .baseUrl("https://hotels-dev-api.tobyx.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LeaderBoardApi leaderBoardApi = retrofit.create(LeaderBoardApi.class);
        Call<List<LeaderBoard>> call = leaderBoardApi.getRecord();

        call.enqueue(new Callback<List<LeaderBoard>>() {
            @Override
            public void onResponse(Call<List<LeaderBoard>> call, Response<List<LeaderBoard>> response) {
                System.out.print("gggggggggggggggggggggggggggggggg");
                System.out.print(response);
                if (!response.isSuccessful()) {
                    leaderBoardResult.setText("Code: " + response.code());
                    return;
                }

                List<LeaderBoard> userRecords = response.body();
                for (LeaderBoard userRecord: userRecords) {
                    String content = "";
                    int score = userRecord.getScore();
                    content += "Name: " + userRecord.getName() + "\n";
                    content += "Score: " + score;

                    leaderBoardResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<LeaderBoard>> call, Throwable t) {
                System.out.print("gggggggggggggggggggggggggggggggg");
                System.out.print(t.getMessage());
                leaderBoardResult.setText(t.getMessage());
            }
        });
    }
}
