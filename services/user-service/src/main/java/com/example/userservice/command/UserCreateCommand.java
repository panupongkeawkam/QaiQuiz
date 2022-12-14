package com.example.userservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class UserCreateCommand implements Serializable {

    @TargetAggregateIdentifier
    private final String _id;
    private final String name;
    private final Date createDateTime;
}
