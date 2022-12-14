package com.example.resultservice4.command.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultRestModel implements Serializable {

    private String quizId;
    private String userId;
    private String category;
    private String quizName;
    private int score;
    private int fullScore;
}
