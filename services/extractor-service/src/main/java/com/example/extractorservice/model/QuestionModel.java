package com.example.extractorservice.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class QuestionModel implements Serializable {

    private String title;
    private int correct;
    private List<String> choices;

}
