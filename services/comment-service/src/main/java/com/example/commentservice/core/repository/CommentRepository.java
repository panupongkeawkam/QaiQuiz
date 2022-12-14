package com.example.commentservice.core.repository;

import com.example.commentservice.core.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    public Comment findCommentBy_id(String commentId);

    public List<Comment> findCommentsByQuizId(String quizId);
}
