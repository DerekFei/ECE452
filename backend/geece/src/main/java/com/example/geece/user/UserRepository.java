package com.example.geece.user;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
      public User findByUserId(String userId);
      public List<User> findAllByOrderByScoreDesc();

    //  public User updateByI(String userId, String name, int score);
}
