package com.example.quizservice.command;


import com.example.quizservice.event.QuestionDeletedEvent;
import com.example.quizservice.event.QuizDeletedEvent;
import com.example.quizservice.pojo.Quiz;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class DeleteQuizAggregate {

    @AggregateIdentifier
    private String aggregateId;
    private String _id;
    private Quiz quiz;
    private int questionIndex;

    public DeleteQuizAggregate() {
    }

    @CommandHandler
    public DeleteQuizAggregate(DeleteQuizCommand command){
        QuizDeletedEvent event = QuizDeletedEvent.builder()
                .aggregateId(command.getAggregateId())
                ._id(command.getQuizId())
                .build();
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public DeleteQuizAggregate(DeleteQuestionCommand command) {
        QuestionDeletedEvent event = QuestionDeletedEvent.builder()
                .aggregateId(command.getAggregateId())
                .quiz(command.getQuiz())
                .questionIndex(command.getQuestionIndex())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuizDeletedEvent event) {
     this.aggregateId = event.getAggregateId();
     this._id = event.get_id();
    }

    @EventSourcingHandler
    public void on(QuestionDeletedEvent event) {
        this.aggregateId = event.getAggregateId();
        this.quiz = event.getQuiz();
        this.questionIndex = event.getQuestionIndex();;
    }
}
