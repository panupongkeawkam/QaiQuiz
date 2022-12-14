package com.example.userservice.command;

import com.example.userservice.core.event.UserCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Aggregate
public class UserAggregate {

    @AggregateIdentifier
    private String _id;
    private String name;
    private Date createDateTime;

    public UserAggregate() {
    }

    @CommandHandler
    public UserAggregate(UserCreateCommand command) {
        UserCreatedEvent event = new UserCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        this._id = event.get_id();
        this.name = event.getName();
        this.createDateTime = event.getCreateDateTime();
    }
}
