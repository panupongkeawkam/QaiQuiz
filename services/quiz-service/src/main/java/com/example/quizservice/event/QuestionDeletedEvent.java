package com.example.quizservice.event;

import com.example.quizservice.pojo.Quiz;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class QuestionDeletedEvent implements Serializable {

    private String aggregateId;
    private Quiz quiz;
    private int questionIndex;
}
