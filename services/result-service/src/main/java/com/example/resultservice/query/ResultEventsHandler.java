package com.example.resultservice.query;

import com.example.resultservice.core.pojo.Result;
import com.example.resultservice.query.model.ResultSummaryModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultEventsHandler {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ResultEventsHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @QueryHandler
    public Result findResultByResultId (FindResultByResultId query) {
        return (Result) rabbitTemplate.convertSendAndReceive("Result", "find-result-resultId", query.get_id());
    }

    @QueryHandler
    public List<Result> findAllResultByUserId (FindAllResultByUserId query) {
        return (List<Result>) rabbitTemplate.convertSendAndReceive("Result", "find-all-result-userId", query.getUserId());
    }

    @QueryHandler
    public List<Result> findAllResultByQuizId (FindResultsByQuizId query) {
        return (List<Result>) rabbitTemplate.convertSendAndReceive("Result", "find-all-result-quizId", query.getQuizId());
    }

    @QueryHandler
    public ResultSummaryModel findResultSummary (FindResultsDetail query) {
        return (ResultSummaryModel) rabbitTemplate.convertSendAndReceive("Result", "find-detail", query.getResultList());
    }
}
