package com.example.quizservice.rest;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteQuizRestModel implements Serializable {

    private String quizId;
}
