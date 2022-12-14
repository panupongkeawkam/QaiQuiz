package com.example.quizservice.event;

import com.example.quizservice.pojo.Quiz;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class QuestionAddedEvent implements Serializable {
    private String _id;
    private Quiz quiz;
}
