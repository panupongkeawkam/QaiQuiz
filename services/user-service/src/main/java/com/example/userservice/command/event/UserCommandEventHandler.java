package com.example.userservice.command.event;

import com.example.userservice.core.event.UserCreatedEvent;
import com.example.userservice.core.pojo.User;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCommandEventHandler {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserCommandEventHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventHandler
    public void on(UserCreatedEvent event) {
        User user = User.builder()
                ._id(event.get_id())
                .name(event.getName())
                .createDateTime(event.getCreateDateTime())
                .build();
        rabbitTemplate.convertAndSend("User", "create-user", user);
    }
}
