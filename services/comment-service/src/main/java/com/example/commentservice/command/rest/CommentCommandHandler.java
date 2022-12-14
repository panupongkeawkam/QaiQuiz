package com.example.commentservice.command.rest;

import com.example.commentservice.command.CreateCommentCommand;
import com.example.commentservice.command.model.CreateCommentModel;
import com.example.commentservice.core.pojo.Comment;
import com.example.commentservice.query.FindCommentByCommentId;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentCommandHandler {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    public CommentCommandHandler(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public CreateCommentModel createComment(@RequestBody CreateCommentRestModel commentRestModel) {
        String commentId = generateCommentId(UUID.randomUUID().toString());
        CreateCommentCommand command = CreateCommentCommand.builder()
                ._id(commentId)
                .createDateTime(new Date())
                .message(commentRestModel.getMessage())
                .username(commentRestModel.getUsername())
                .quizId(commentRestModel.getQuizId())
                .userId(commentRestModel.getUserId())
                .build();
        CreateCommentModel comment;
        try {
            String _id = commandGateway.sendAndWait(command);
            comment = CreateCommentModel.builder()
                    ._id(_id)
                    .quizId(command.getQuizId())
                    .userId(command.getUserId())
                    .username(command.getUsername())
                    .createDateTime(command.getCreateDateTime())
                    .message(command.getMessage())
                    .build();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return comment;
    }

    public String generateCommentId(String _id) {
        FindCommentByCommentId query = FindCommentByCommentId.builder()
                ._id(_id)
                .build();
        boolean isCommentExist = queryGateway.query(query, ResponseTypes.instanceOf(Comment.class)).join() != null;

        if(isCommentExist)
            generateCommentId(UUID.randomUUID().toString());

        return _id;
    }
}
