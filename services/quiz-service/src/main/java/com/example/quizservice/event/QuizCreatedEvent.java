package com.example.quizservice.event;

import com.example.quizservice.model.Question;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuizCreatedEvent {

    private String _id;
    private String title;
    private Date createDateTime;
    private String category;
    private List<Question> questions;

}
