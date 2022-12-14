package com.example.commentservice.core.event;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class CommentCreatedEvent implements Serializable {
    private String _id;
    private String userId;
    private String quizId;
    private String username;
    private String message;
    private Date createDateTime;
}
