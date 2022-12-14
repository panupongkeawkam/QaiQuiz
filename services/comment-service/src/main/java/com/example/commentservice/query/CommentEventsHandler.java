package com.example.commentservice.query;

import com.example.commentservice.core.pojo.Comment;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventsHandler {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CommentEventsHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @QueryHandler
    public Comment findCommentByCommentId(FindCommentByCommentId query) {
        return (Comment) rabbitTemplate.convertSendAndReceive("Comment", "find-comment-commentId", query.get_id());
    }

    @QueryHandler
    public List<Comment> findCommentsByQuizId(FindCommentsByQuizId query){
        return (List<Comment>) rabbitTemplate.convertSendAndReceive("Comment", "find-comment-quizId", query.getQuizId());
    }
}
