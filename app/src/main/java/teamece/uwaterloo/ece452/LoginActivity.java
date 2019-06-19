package teamece.uwaterloo.ece452;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginBtnOnClick(View v) {
        Intent homeScreenActivity = new Intent(this, HomeScreenActivity.class);
        startActivity(homeScreenActivity);
    }
}
