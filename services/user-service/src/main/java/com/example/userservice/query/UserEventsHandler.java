package com.example.userservice.query;

import com.example.userservice.core.pojo.User;
import com.example.userservice.query.model.FindUserById;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserEventsHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @QueryHandler
    public User findUserById(FindUserById query) {
        return (User) rabbitTemplate.convertSendAndReceive("User", "find-by-id", query.get_id());
    }
}
