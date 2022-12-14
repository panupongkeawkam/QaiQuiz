package com.example.quizservice.repository;

import com.example.quizservice.command.CreateQuizCommand;
import com.example.quizservice.query.FindAllQuizQuery;
import com.example.quizservice.rest.CreateQuizRestModel;
import com.example.quizservice.pojo.Quiz;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @RabbitListener(queues = "find-quizzes-queue")
    public List<Quiz> getQuizzes(FindAllQuizQuery query) {
        return quizRepository.findAll();
    }

    @RabbitListener(queues = "create-quiz-queue")
    public void createQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    @RabbitListener(queues = "find-quiz-quizId-queue")
    public Quiz getQuizByQuizId(String _id) {
        return quizRepository.findQuizBy_id(_id);
    }

    @RabbitListener(queues = "find-quiz-title-queue")
    public List<Quiz> findQuizByQuizTitle(String title) {
        return quizRepository.findQuizzesByTitle(title);
    }

    @RabbitListener(queues = "add-question-queue")
    public void addQuestion(Quiz quiz) {
        quizRepository.save(quiz);
    }

    @RabbitListener(queues = "delete-quiz-queue")
    public void deleteQuiz(String _id){
        quizRepository.deleteById(_id);
    }

    @RabbitListener(queues = "delete-question-queue")
    public void deleteQuestion(Quiz quiz) {
        quizRepository.save(quiz);
    }
}
