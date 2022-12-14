package com.example.userservice.core.repository;

import com.example.userservice.core.pojo.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public User findUserBy_id(String userId);

}
