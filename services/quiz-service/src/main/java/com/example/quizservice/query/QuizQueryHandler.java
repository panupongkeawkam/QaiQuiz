package com.example.quizservice.query;

import com.example.quizservice.pojo.Quiz;
import com.example.quizservice.repository.QuizService;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizQueryHandler {
    private final QuizService quizService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public QuizQueryHandler(QuizService quizService, RabbitTemplate rabbitTemplate) {
        this.quizService = quizService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @QueryHandler
    public List<Quiz> findAllQuiz(FindAllQuizQuery query) {
        return (List<Quiz>) rabbitTemplate.convertSendAndReceive("Quiz", "find-quizzes", query);
    }

    @QueryHandler
    public Quiz findQuizByQuizId(FindQuizByQuizIdQuery query) {
        return (Quiz) rabbitTemplate.convertSendAndReceive("Quiz", "find-quiz-quizId", query.get_id());
    }

    @QueryHandler
    public List<Quiz> findQuizByQuizTitle(FindQuizByQuizTitleQuery query) {
        return (List<Quiz>) rabbitTemplate.convertSendAndReceive("Quiz", "find-quiz-title", query.getTitle());
    }
}
