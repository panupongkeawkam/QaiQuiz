package com.example.quizservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class DeleteQuizCommand implements Serializable {

    @TargetAggregateIdentifier
    private final String aggregateId;
    private final String quizId;

}
