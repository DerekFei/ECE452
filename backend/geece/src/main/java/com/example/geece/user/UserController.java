package com.example.geece.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    @ResponseBody
    public User getUser(@RequestParam(name="userId", required=true) String userId) {
        return userRepository.findByUserId(userId);
    }

    @GetMapping("/leaderboard")
    @ResponseBody
    public List<User> getLeaderboard(){
        return userRepository.findAllByOrderByScoreDesc();
    }


    @PostMapping("/user")
    @ResponseBody
    public void updateNewUser(
            @RequestParam(name="userId", required=true) String userId,
            @RequestParam(name="name", required=false, defaultValue="Stranger") String name,
            @RequestParam(name="score", required=false, defaultValue="0") int score
    ) {
        User newUser =  new User(userId, name, score);
        userRepository.save(newUser);
        return;
    }
}