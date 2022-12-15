package com.example.quizservice.rest;

import com.example.quizservice.model.Question;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class QuizModel implements Serializable {
    private String _id;
    private String title;
    private Date createDateTime;
    private List<Question> questions;
    private String category;
}
