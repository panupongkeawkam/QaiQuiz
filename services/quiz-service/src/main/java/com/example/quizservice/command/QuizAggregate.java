package com.example.quizservice.command;

import com.example.quizservice.event.QuizCreatedEvent;
import com.example.quizservice.model.Question;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

@Aggregate
public class QuizAggregate {

    @AggregateIdentifier
    private String _id;
    private String title;
    private Date createDateTime;
    private List<Question> questions;
    private String category;


    public QuizAggregate() {
    }

    @CommandHandler
    public QuizAggregate(CreateQuizCommand createQuizCommand) {
        QuizCreatedEvent quizCreatedEvent = new QuizCreatedEvent();
        BeanUtils.copyProperties(createQuizCommand, quizCreatedEvent);
        AggregateLifecycle.apply(quizCreatedEvent);
    }


    @EventSourcingHandler
    public void on(QuizCreatedEvent quizCreatedEvent) {
        this._id = quizCreatedEvent.get_id();
        this.title = quizCreatedEvent.getTitle();
        this.createDateTime = quizCreatedEvent.getCreateDateTime();
        this.questions = quizCreatedEvent.getQuestions();
        this.category = quizCreatedEvent.getCategory();
    }

}
