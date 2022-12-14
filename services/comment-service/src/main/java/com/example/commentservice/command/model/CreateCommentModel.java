package com.example.commentservice.command.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class CreateCommentModel implements Serializable {
    private String _id;
    private String quizId;
    private String userId;
    private Date createDateTime;
    private String username;
    private String message;
}
