package teamece.uwaterloo.ece452;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    private String name;

    @SerializedName("score")
    private int score;

    @SerializedName("userId")
    private String userId;

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getUserId() {
        return userId;
    }
}
