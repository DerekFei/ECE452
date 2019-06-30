package teamece.uwaterloo.ece452;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderBoardActivity extends AppCompatActivity {
    private ListView userName;
    private ListView userScore;
    private ListView myName;
    private ListView myScore;

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
        myScore = findViewById(R.id.myScore);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://54.147.208.46/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String userId = PreferenceManager.getDefaultSharedPreferences(this).getString("userId", "defaultStringIfNothingFound");
        userId = "\"" + userId + "\"";

        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> userCall = userApi.getUser(userId);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                User currentUser = response.body();
                List<Integer> myScoreList = new ArrayList<Integer>();
                if (currentUser != null) {
                    myScoreList.add(currentUser.getScore());
                    ArrayAdapter<Integer> myScoreArrayAdapter = new ArrayAdapter <Integer>(LeaderBoardActivity.this, android.R.layout.simple_list_item_1, myScoreList) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView textView=(TextView) super.getView(position, convertView, parent);
                            textView.setTextColor(Color.WHITE);

                            return textView;
                        }
                    };
                    myScore.setAdapter(myScoreArrayAdapter);
                } else {
                    myScoreList.add(0);
                    ArrayAdapter<Integer> myScoreArrayAdapter = new ArrayAdapter <Integer>(LeaderBoardActivity.this, android.R.layout.simple_list_item_1, myScoreList) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView textView=(TextView) super.getView(position, convertView, parent);
                            textView.setTextColor(Color.WHITE);

                            return textView;
                        }
                    };
                    myScore.setAdapter(myScoreArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("User Tag", t.getMessage());
            }
        });

        //background
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video_bg;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
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

                List<String> userNameList = new ArrayList<String>();
                List<Integer> userScoreList = new ArrayList<Integer>();
                List<LeaderBoard> userRecords = response.body();
                for (LeaderBoard userRecord: userRecords) {
                    String tempName = userRecord.getName();
                    tempName = tempName.replace("\"", "");
                    userNameList.add(tempName);
                    userScoreList.add(userRecord.getScore());
                }

                ArrayAdapter<Integer> userScoreArrayAdapter = new ArrayAdapter <Integer>(LeaderBoardActivity.this, android.R.layout.simple_list_item_1, userScoreList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView=(TextView) super.getView(position, convertView, parent);
                        textView.setTextColor(Color.WHITE);

                        return textView;
                    }
                };
                ArrayAdapter<String> userNameArrayAdapter = new ArrayAdapter <String>(LeaderBoardActivity.this, android.R.layout.simple_list_item_1, userNameList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView=(TextView) super.getView(position, convertView, parent);
                        textView.setTextColor(Color.WHITE);

                        return textView;
                    }
                };
                userName.setAdapter(userNameArrayAdapter);
                userScore.setAdapter(userScoreArrayAdapter);
            }

            @Override
            public void onFailure(Call<List<LeaderBoard>> call, Throwable t) {
                Log.d("Leader Board Tag", t.getMessage());
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video_bg;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}
