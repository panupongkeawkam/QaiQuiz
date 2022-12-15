package com.example.quizservice.rest;

import com.example.quizservice.command.AddQuestionCommand;
import com.example.quizservice.command.CreateQuizCommand;
import com.example.quizservice.command.DeleteQuestionCommand;
import com.example.quizservice.command.DeleteQuizCommand;
import com.example.quizservice.model.Question;
import com.example.quizservice.pojo.Quiz;
import com.example.quizservice.query.FindQuizByQuizIdQuery;
import com.example.quizservice.query.FindQuizByQuizTitleQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quiz")
@CrossOrigin
public class QuizCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    public QuizCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    @PostMapping(consumes = "application/json")
    public QuizModel createQuiz(@RequestBody CreateQuizRestModel quizRestModel) {
        String quizId = quizIdGenerator(UUID.randomUUID().toString());
        boolean isTitleExist = isQuizTitleExist(quizRestModel.getTitle());
        if(isTitleExist) {
            throw new IllegalArgumentException("Quiz title is used.");
        }
        List<Question> questions = new ArrayList<>();
        CreateQuizCommand createQuizCommand = CreateQuizCommand.builder()
                ._id(quizId)
                .title(quizRestModel.getTitle())
                .category(quizRestModel.getCategory())
                .createDateTime(new Date())
                .questions(questions)
                .build();

        try{
        String _id = commandGateway.sendAndWait(createQuizCommand);
        return QuizModel.builder()
                ._id(_id)
                .category(createQuizCommand.getCategory())
                .title(createQuizCommand.getTitle())
                .createDateTime(createQuizCommand.getCreateDateTime())
                .questions(createQuizCommand.getQuestions())
                .build();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PutMapping(consumes = "application/json")
    public QuizModel addQuestion(@RequestBody AddQuestionRestModel restModel) {
        Quiz quiz = findQuiz(restModel.getQuizId());
        Question question = Question.builder()
                .title(restModel.getQuestion().getTitle())
                .correct(restModel.getQuestion().getCorrect())
                .choices(restModel.getQuestion().getChoices())
                .build();
        quiz.getQuestions().add(question);
        QuizModel quizModel;
        try{
            AddQuestionCommand command = AddQuestionCommand.builder()
                    .quiz(quiz)
                    ._id(quiz.get_id())
                    .build();
            commandGateway.send(command);
            quizModel = QuizModel.builder()
                    ._id(quiz.get_id())
                    .title(quiz.getTitle())
                    .createDateTime(quiz.getCreateDateTime())
                    .category(quiz.getCategory())
                    .questions(quiz.getQuestions())
                    .build();
            return quizModel;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @DeleteMapping
    public Boolean deleteQuiz(@RequestBody DeleteQuizRestModel model) {
        DeleteQuizCommand command = DeleteQuizCommand.builder()
                .aggregateId(UUID.randomUUID().toString())
                .quizId(model.getQuizId())
                .build();
        try{
            commandGateway.send(command);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @DeleteMapping("/question")
    @CrossOrigin
    public Boolean deleteQuestion(@RequestBody DeleteQuestionRestModel model){
        FindQuizByQuizIdQuery query = FindQuizByQuizIdQuery.builder()
                ._id(model.getQuizId())
                .build();
        Quiz quiz = queryGateway.query(query, ResponseTypes.instanceOf(Quiz.class)).join();
        if(quiz != null) {
            DeleteQuestionCommand command = DeleteQuestionCommand.builder()
                    .aggregateId(UUID.randomUUID().toString())
                    .quiz(quiz)
                    .questionIndex(model.getQuestionIndex())
                    .build();
            try{
                commandGateway.send(command);
                return true;
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
        return false;
    }

    public String quizIdGenerator(String _id) {
        FindQuizByQuizIdQuery query = FindQuizByQuizIdQuery.builder()
                ._id(_id)
                .build();
        boolean isQuizExist = queryGateway.query(query, ResponseTypes.instanceOf(Quiz.class)).join() != null;

        if(isQuizExist) {
            quizIdGenerator(UUID.randomUUID().toString());
        }

        return _id;
    }

    public Quiz findQuiz(String _id) {
        FindQuizByQuizIdQuery query = FindQuizByQuizIdQuery.builder()
                ._id(_id)
                .build();
        return queryGateway.query(query, ResponseTypes.instanceOf(Quiz.class)).join();
    }

    public Boolean isQuizTitleExist(String title) {
        FindQuizByQuizTitleQuery query = FindQuizByQuizTitleQuery.builder()
                .title(title)
                .build();
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(Quiz.class)).join().size() != 0;
    }
}
