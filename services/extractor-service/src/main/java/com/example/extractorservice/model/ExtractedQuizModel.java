package com.example.extractorservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExtractedQuizModel {

    private List<QuizModel> quizzes;

}
