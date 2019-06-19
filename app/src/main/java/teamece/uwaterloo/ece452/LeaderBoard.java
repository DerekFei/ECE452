package teamece.uwaterloo.ece452;

import com.google.gson.annotations.SerializedName;

public class LeaderBoard {
    @SerializedName("body")
    private String name;
    private int score;

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
