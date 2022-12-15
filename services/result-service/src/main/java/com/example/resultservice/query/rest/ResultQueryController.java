package com.example.resultservice.query.rest;

import com.example.resultservice.core.pojo.Result;
import com.example.resultservice.query.FindAllResultByUserId;
import com.example.resultservice.query.FindResultsByQuizId;
import com.example.resultservice.query.FindResultsDetail;
import com.example.resultservice.query.model.AllResultModel;
import com.example.resultservice.query.model.ResultSummaryModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/result")
public class ResultQueryController {

    private final QueryGateway queryGateway;

    @Autowired
    public ResultQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @RequestMapping(value = "/user", method = RequestMethod.PATCH)
    public AllResultModel getAllResult(@RequestBody GetUserResultsRestModel model) {
        FindAllResultByUserId query = FindAllResultByUserId.builder()
                .userId(model.getUserId())
                .build();
        AllResultModel allResult;
        try{
            List<Result> resultList = queryGateway.query(query, ResponseTypes.multipleInstancesOf(Result.class)).join();
            allResult = AllResultModel.builder()
                    .resultList(resultList)
                    .build();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return allResult;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.PATCH)
    public ResultSummaryModel getResultSummary(@RequestBody GetSummaryResultRestModel model) {
        FindResultsByQuizId query = FindResultsByQuizId.builder()
                .quizId(model.getQuizId())
                .build();
        ResultSummaryModel detail;
        try{
            List<Result> resultList = queryGateway.query(query, ResponseTypes.multipleInstancesOf(Result.class)).join();
            FindResultsDetail resultsDetailQuery = FindResultsDetail.builder()
                    .resultList(resultList)
                    .build();
            detail = queryGateway.query(resultsDetailQuery, ResponseTypes.instanceOf(ResultSummaryModel.class)).join();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return detail;
    }
}
