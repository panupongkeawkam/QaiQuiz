package com.example.resultservice4.command.event;

import com.example.resultservice4.core.event.ResultCreatedEvent;
import com.example.resultservice4.core.pojo.Result;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResultCommandEventHandler {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ResultCommandEventHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventHandler
    public void on(ResultCreatedEvent event) {
        Result result = Result.builder()
                ._id(event.get_id())
                .quizId(event.getQuizId())
                .userId(event.getUserId())
                .createDateTime(event.getCreateDateTime())
                .quizName(event.getQuizName())
                .category(event.getCategory())
                .score(event.getScore())
                .fullScore(event.getFullScore())
                .build();
        rabbitTemplate.convertAndSend("Result", "create-result", result);
    }
}
