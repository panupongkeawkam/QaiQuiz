package com.example.quizservice.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CreateQuizRestModel implements Serializable {

    private String title;
    private String category;
}
