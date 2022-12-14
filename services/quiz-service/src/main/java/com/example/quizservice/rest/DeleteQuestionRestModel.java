package com.example.quizservice.rest;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteQuestionRestModel implements Serializable {
    private String quizId;
    private int questionIndex;
}
