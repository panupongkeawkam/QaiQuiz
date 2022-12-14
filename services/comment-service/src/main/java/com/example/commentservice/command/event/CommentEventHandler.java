package com.example.commentservice.command.event;

import com.example.commentservice.core.event.CommentCreatedEvent;
import com.example.commentservice.core.pojo.Comment;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentEventHandler {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CommentEventHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventHandler
    public void on(CommentCreatedEvent event) {
        Comment comment = Comment.builder()
                ._id(event.get_id())
                .quizId(event.getQuizId())
                .userId(event.getUserId())
                .message(event.getMessage())
                .username(event.getUsername())
                .createDateTime(event.getCreateDateTime())
                .build();

        rabbitTemplate.convertSendAndReceive("Comment", "create-comment", comment);
    }
}
