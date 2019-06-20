package com.example.geece.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("User")
public class User {
    @Id
    public String userId;
    public String name;
    public int score;

    public User() {
    }
    public User(String userId, String name, int score) {
        this.userId = userId;
        this.name = name;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
