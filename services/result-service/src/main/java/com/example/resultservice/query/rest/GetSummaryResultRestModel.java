package com.example.resultservice.query.rest;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetSummaryResultRestModel implements Serializable {
    private String quizId;
}
