package com.example.commentservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class CreateCommentCommand implements Serializable {
    @TargetAggregateIdentifier
    private final String _id;
    private final String quizId;
    private final String userId;
    private final String message;
    private final String username;
    private final Date createDateTime;
}
