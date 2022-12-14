package com.example.resultservice4.query.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ResultSummaryModel implements Serializable {
    private String quizId;
    private int fullScore;
    private int maxScore;
    private float meanScore;
    private int minScore;
    private List<Integer> scoreRange;
}
