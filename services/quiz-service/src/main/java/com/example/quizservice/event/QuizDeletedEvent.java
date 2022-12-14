package com.example.quizservice.event;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class QuizDeletedEvent implements Serializable {
    private String aggregateId;
    private String _id;
}
