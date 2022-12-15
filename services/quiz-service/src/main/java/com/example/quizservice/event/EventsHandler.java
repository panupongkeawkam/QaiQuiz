package com.example.quizservice.event;

import com.example.quizservice.pojo.Quiz;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EventsHandler {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EventsHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventHandler
    public void on(QuizCreatedEvent quizCreatedEvent) {
        Quiz quiz = Quiz.builder()
                ._id(quizCreatedEvent.get_id())
                .title(quizCreatedEvent.getTitle())
                .createDateTime(quizCreatedEvent.getCreateDateTime())
                .category(quizCreatedEvent.getCategory())
                .questions(quizCreatedEvent.getQuestions())
                .build();
        rabbitTemplate.convertAndSend("Quiz", "create-quiz", quiz);
    }

    @EventHandler
    public void on(QuestionAddedEvent event) {
        Quiz quiz = Quiz.builder()
                ._id(event.get_id())
                .title(event.getQuiz().getTitle())
                .category(event.getQuiz().getCategory())
                .createDateTime(event.getQuiz().getCreateDateTime())
                .questions(event.getQuiz().getQuestions())
                .build();
        rabbitTemplate.convertAndSend("Quiz", "add-question", quiz);
    }

    @EventHandler
    public void on(QuizDeletedEvent event) {
        rabbitTemplate.convertAndSend("Quiz", "delete-quiz", event.get_id());
    }

    @EventHandler
    public void on(QuestionDeletedEvent event) {
        event.getQuiz().getQuestions().remove(event.getQuestionIndex());
        rabbitTemplate.convertAndSend("Quiz", "delete-question", event.getQuiz());
    }
}
