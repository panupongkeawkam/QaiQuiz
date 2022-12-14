package com.example.quizservice.query;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FindQuizByQuizTitleQuery implements Serializable {
    private String title;
}
