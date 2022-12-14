package com.example.commentservice.command;

import com.example.commentservice.core.event.CommentCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate
public class CommentAggregate {
    @AggregateIdentifier
    private String _id;
    private String message;
    private String quizId;
    private String username;
    private String userId;
    private Date createDateTime;

    public CommentAggregate() {
    }

    @CommandHandler
    public CommentAggregate(CreateCommentCommand command) {
        if(command.getMessage().isBlank() || command.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        CommentCreatedEvent event = CommentCreatedEvent.builder()
                ._id(command.get_id())
                .message(command.getMessage())
                .quizId(command.getQuizId())
                .userId(command.getUserId())
                .username(command.getUsername())
                .createDateTime(command.getCreateDateTime())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(CommentCreatedEvent event) {
        this._id = event.get_id();
        this.quizId = event.getQuizId();
        this.userId = event.getUserId();
        this.message = event.getMessage();
        this.username = event.getUsername();
        this.createDateTime = event.getCreateDateTime();
    }
}
