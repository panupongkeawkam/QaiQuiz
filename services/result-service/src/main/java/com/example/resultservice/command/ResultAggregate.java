package com.example.resultservice.command;

import com.example.resultservice.core.event.ResultCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Aggregate
public class ResultAggregate {

    @AggregateIdentifier
    private String _id;
    private String userId;
    private String quizId;
    private Date createDateTime;
    private int score;
    private int fullScore;
    private String category;
    private String quizName;

    public ResultAggregate() {
    }

    @CommandHandler
    public ResultAggregate(ResultCreateCommand command) {
        ResultCreatedEvent event = new ResultCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on (ResultCreatedEvent event) {
        this._id = event.get_id();
        this.quizId = event.getQuizId();
        this.userId = event.getUserId();
        this.createDateTime = event.getCreateDateTime();
        this.score = event.getScore();
        this.fullScore = event.getFullScore();
        this.category = event.getCategory();
        this.quizName = event.getQuizName();
    }

}
