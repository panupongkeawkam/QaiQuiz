package com.example.commentservice.query.rest;

import com.example.commentservice.core.pojo.Comment;
import com.example.commentservice.query.FindCommentsByQuizId;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentQueryController {
    private final QueryGateway queryGateway;

    @Autowired
    public CommentQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @PatchMapping
    public List<Comment> getCommentsByQuizId(@RequestBody GetCommentByQuizIdRestModel restModel) {
        FindCommentsByQuizId query = FindCommentsByQuizId.builder()
                .quizId(restModel.getQuizId())
                .build();
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(Comment.class)).join();
    }

}
