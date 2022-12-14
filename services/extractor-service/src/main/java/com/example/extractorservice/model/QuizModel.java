package com.example.extractorservice.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class QuizModel implements Serializable {

    private String _id;
    private String title;
    private String category;
    private String createDateTime;
    private List<QuestionModel> questions;

}