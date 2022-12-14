package com.example.extractorservice.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class SearchQuizRestModel implements Serializable {

    private List<QuizModel> quizzes;
    private String query;

}
