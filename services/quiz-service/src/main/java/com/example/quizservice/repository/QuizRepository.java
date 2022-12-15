package com.example.quizservice.repository;

import com.example.quizservice.pojo.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {

    public Quiz findQuizBy_id(String _id);


    public List<Quiz> findQuizzesByTitle(String title);

}