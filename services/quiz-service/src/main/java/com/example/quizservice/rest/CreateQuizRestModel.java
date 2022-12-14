package com.example.quizservice.rest;

import com.example.quizservice.model.Question;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CreateQuizRestModel implements Serializable {

    private String title;
    private String category;
}
