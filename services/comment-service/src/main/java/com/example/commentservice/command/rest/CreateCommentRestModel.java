package com.example.commentservice.command.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CreateCommentRestModel implements Serializable {
    private String userId;
    private String quizId;
    private String username;
    private String message;
}
