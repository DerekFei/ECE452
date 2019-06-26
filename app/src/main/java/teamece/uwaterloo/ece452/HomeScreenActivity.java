package teamece.uwaterloo.ece452;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class HomeScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_screen);
    }

    public void startNewGame(View v) {
        Intent newGameActivity = new Intent(this, GameActivity.class);
        startActivity(newGameActivity);
    }

    public void leaderBoardOnClick(View v) {
        Intent leaderBoardActivity = new Intent(this, LeaderBoardActivity.class);
        startActivity(leaderBoardActivity);
    }
}
