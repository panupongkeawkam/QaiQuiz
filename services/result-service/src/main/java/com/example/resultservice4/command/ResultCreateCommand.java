package com.example.resultservice4.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class ResultCreateCommand implements Serializable {

    @TargetAggregateIdentifier
    private final String _id;
    private final String quizId;
    private final String userId;
    private final Date createDateTime;
    private final String category;
    private final String quizName;
    private final int score;
    private final int fullScore;
}
