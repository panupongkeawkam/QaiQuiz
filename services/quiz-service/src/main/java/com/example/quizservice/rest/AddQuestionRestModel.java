package com.example.quizservice.rest;

import com.example.quizservice.model.Question;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AddQuestionRestModel implements Serializable {

    private String quizId;
    private Question question;

}
