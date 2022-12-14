package com.example.quizservice.command;

import com.example.quizservice.model.Question;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class CreateQuizCommand implements Serializable {

    @TargetAggregateIdentifier
    private final String _id;
    private final String title;
    private final Date createDateTime;
    private final String category;
    private final List<Question> questions;

}
