package com.example.userservice.command.rest;

import com.example.userservice.command.UserCreateCommand;
import com.example.userservice.core.pojo.User;
import com.example.userservice.query.model.FindUserById;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    public UserCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    @GetMapping
    public Date getTime() {
        return new Date();
    }

    @PostMapping
    public UserModel createUser() {
        String userIdModel = userIdGenerate(UUID.randomUUID().toString());
        String nameModel = userNameGenerate();
        UserCreateCommand command = UserCreateCommand.builder()
                ._id(userIdModel)
                .name(nameModel)
                .createDateTime(new Date())
                .build();
        UserModel user;
        try{
            String userId = commandGateway.sendAndWait(command);
            user = UserModel.builder()
                    ._id(userId)
                    .name(command.getName())
                    .createDateTime(command.getCreateDateTime())
                    .build();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return user;
    }

    public String userIdGenerate(String userId) {
        FindUserById query = FindUserById.builder()
                ._id(userId)
                .build();
        User userStored = queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();

        if(userStored != null) {
            userIdGenerate(UUID.randomUUID().toString());
        }
        return userId;
    }

    public String userNameGenerate() {
        String fullName = new RestTemplate().getForObject("https://names.drycodes.com/1?nameOptions=objects&format=text", String.class);
        String[] partOfNames = new String[0];
        if (fullName != null) {
            partOfNames = fullName.split("_");
        }
        return partOfNames[0] + " " + partOfNames[1];
    }

}
