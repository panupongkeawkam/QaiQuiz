package com.example.resultservice4.core.repository;

import com.example.resultservice4.core.pojo.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends MongoRepository<Result, String> {

    public Result findResultBy_id(String id);
    public List<Result> findResultsByQuizId(String quizId);

    public List<Result> findResultsByUserId(String userId);

    public int countResultByScoreAndQuizId(int score, String quizId);



}
