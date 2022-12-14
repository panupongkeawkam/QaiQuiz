package com.example.quizservice.command;

import com.example.quizservice.pojo.Quiz;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class DeleteQuestionCommand implements Serializable {
    @TargetAggregateIdentifier
    private final String aggregateId;
    private final Quiz quiz;
    private final int questionIndex;

}
