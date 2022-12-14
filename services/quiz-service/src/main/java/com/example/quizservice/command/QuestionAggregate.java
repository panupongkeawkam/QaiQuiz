package com.example.quizservice.command;

import com.example.quizservice.event.QuestionAddedEvent;
import com.example.quizservice.pojo.Quiz;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Aggregate
public class QuestionAggregate {

    @AggregateIdentifier
    private final String aggregateId = UUID.randomUUID().toString();
    private String _id;
    private Quiz quiz;

    public QuestionAggregate() {
    }

    @CommandHandler
    public QuestionAggregate(AddQuestionCommand command) {
        QuestionAddedEvent event = new QuestionAddedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuestionAddedEvent event) {
        this._id = event.get_id();
        this.quiz = event.getQuiz();
    }
}
