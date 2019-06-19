package teamece.uwaterloo.ece452;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void startNewGame(View v) {
        Intent newGameActivity = new Intent(this, GameActivity.class);
        startActivity(newGameActivity);
    }

    public void leaderBoard(View v) {
        Intent newleaderBoard = new Intent(this, LeaderBoardActivity.class);
        startActivity(newleaderBoard);
    }
}
