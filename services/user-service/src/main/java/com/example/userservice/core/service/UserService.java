package com.example.userservice.core.service;

import com.example.userservice.core.pojo.User;
import com.example.userservice.core.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserService implements Serializable {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "find-user-queue")
    public User findUserByUserId(String _id) {
        return repository.findUserBy_id(_id);
    }


    @RabbitListener(queues = "create-user-queue")
    public void createNewUser(User newUser) {
        repository.save(newUser);
    }
}
