package com.example.resultservice4.query;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FindResultsByQuizId implements Serializable {
    private String quizId;
}
