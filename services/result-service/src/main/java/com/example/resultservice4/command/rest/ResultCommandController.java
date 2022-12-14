package com.example.resultservice4.command.rest;

import com.example.resultservice4.command.ResultCreateCommand;
import com.example.resultservice4.core.pojo.Result;
import com.example.resultservice4.query.FindResultByResultId;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("result")
public class ResultCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    public ResultCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResultModel createNewResult(@RequestBody ResultRestModel resultRestModel) {
        String resultIdModel = resultIdGenerate(UUID.randomUUID().toString());
        ResultCreateCommand command = ResultCreateCommand.builder()
                ._id(resultIdModel)
                .userId(resultRestModel.getUserId())
                .quizId(resultRestModel.getQuizId())
                .createDateTime(new Date())
                .quizName(resultRestModel.getQuizName())
                .category(resultRestModel.getCategory())
                .score(resultRestModel.getScore())
                .fullScore(resultRestModel.getFullScore())
                .build();
        ResultModel result;
        try{
            String resultId = commandGateway.sendAndWait(command);
            result = ResultModel.builder()
                    ._id(resultId)
                    .userId(command.getUserId())
                    .quizId(command.getQuizId())
                    .createDateTime(command.getCreateDateTime())
                    .quizName(command.getQuizName())
                    .category(command.getCategory())
                    .score(command.getScore())
                    .fullScore(command.getFullScore())
                    .build();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return result;
    }

    public String resultIdGenerate(String resultId) {
        FindResultByResultId query = FindResultByResultId.builder()
                ._id(resultId)
                .build();
        Result resultStored = queryGateway.query(query, ResponseTypes.instanceOf(Result.class)).join();

        if(resultStored != null) {
            resultIdGenerate(UUID.randomUUID().toString());
        }

        return resultId;
    }

}
