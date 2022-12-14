package com.example.commentservice.core.service;

import com.example.commentservice.core.pojo.Comment;
import com.example.commentservice.core.repository.CommentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @RabbitListener(queues = "find-comment-commentId-queue")
    public Comment findCommentByCommentId(String commentId) {
        return commentRepository.findCommentBy_id(commentId);
    }

    @RabbitListener(queues = "create-comment-queue")
    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    @RabbitListener(queues = "find-comment-quizId-queue")
    public List<Comment> findCommentsByQuizId(String quizId) {
        return commentRepository.findCommentsByQuizId(quizId);
    }
}
