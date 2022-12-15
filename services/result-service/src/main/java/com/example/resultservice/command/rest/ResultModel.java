package com.example.resultservice.command.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultModel implements Serializable {

    private String _id;
    private String quizId;
    private String userId;
    private String quizName;
    private String category;
    private Date createDateTime;
    private int score;
    private int fullScore;

}
