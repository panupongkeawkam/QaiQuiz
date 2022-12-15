package com.example.resultservice.core.service;

import com.example.resultservice.core.pojo.Result;
import com.example.resultservice.core.repository.ResultRepository;
import com.example.resultservice.query.model.ResultSummaryModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {
    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @RabbitListener(queues = "find-result-queue")
    public Result findResultByResultId (String _id) {
        return resultRepository.findResultBy_id(_id);
    }

    @RabbitListener(queues = "create-result-queue")
    public void createResult(Result result) {
        resultRepository.save(result);
    }

    @RabbitListener(queues = "find-results-userId-queue")
    public List<Result> findAllResultByUserId(String userId) {
        return resultRepository.findResultsByUserId(userId);
    }

    @RabbitListener(queues = "find-results-quizId-queue")
    public List<Result> findAllResultByQuizUd(String quizId) {
        return  resultRepository.findResultsByQuizId(quizId);
    }

    @RabbitListener(queues = "find-results-detail-queue")
    public ResultSummaryModel findQuizSummary(List<Result> resultList) {
        if(!resultList.isEmpty()) {
            int maxScore = 0, minScore = 0;
            float meanScore = 0, sumScore = 0;
            int fullScore = resultList.get(0).getFullScore();
            boolean isMin = true;
            List<Integer> scoreRange = new ArrayList<>();
            for(int i = 0; i <= fullScore; i++) {
                int countScore = resultRepository.countResultByScoreAndQuizId(i, resultList.get(0).getQuizId());
                if(isMin & countScore > 0) {
                    isMin = false;
                    minScore = i;
                }
                if(countScore > 0) {
                    maxScore = i;
                }
                sumScore += i*countScore;
                scoreRange.add(countScore);
            }
            meanScore = sumScore / resultList.size();
            return ResultSummaryModel.builder()
                    .quizId(resultList.get(0).getQuizId())
                    .minScore(minScore)
                    .meanScore(meanScore)
                    .fullScore(fullScore)
                    .maxScore(maxScore)
                    .scoreRange(scoreRange)
                    .build();
        }
        return null;
    }
}
