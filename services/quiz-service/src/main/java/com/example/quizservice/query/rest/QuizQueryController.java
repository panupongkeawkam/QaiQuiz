package com.example.quizservice.query.rest;

import com.example.quizservice.model.Question;
import com.example.quizservice.pojo.Quiz;
import com.example.quizservice.query.FindAllQuizQuery;
import com.example.quizservice.query.FindQuizByQuizIdQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/quiz")
public class QuizQueryController {

    private final QueryGateway queryGateway;

    @Autowired
    public QuizQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<Quiz> adminGetQuizzes(@RequestParam HashMap<String, String> params) {
        FindAllQuizQuery query = new FindAllQuizQuery();
        if(params.get("role") == null) {
            List<Quiz> quizzes = new ArrayList<>();
            for(Quiz quiz : queryGateway.query(query, ResponseTypes.multipleInstancesOf(Quiz.class)).join()){
                if(quiz.getQuestions().size() > 0){
                    quizzes.add(quiz);
                }
            };
            return quizzes;
        }
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(Quiz.class)).join();
    }

    @PatchMapping
    public Quiz getQuizByQuizId(@RequestBody GetQuizByQuizIdRestModel model){
        FindQuizByQuizIdQuery query = FindQuizByQuizIdQuery.builder()
                ._id(model.getQuizId())
                .build();

        return queryGateway.query(query, ResponseTypes.instanceOf(Quiz.class)).join();
    }
}
