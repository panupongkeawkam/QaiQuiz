package com.example.resultservice.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ResultCreatedEvent implements Serializable {

    private String _id;
    private String quizId;
    private String userId;
    private Date createDateTime;
    private String quizName;
    private String category;
    private int score;
    private int fullScore;

}
