package com.example.commentservice.query;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FindCommentByCommentId implements Serializable {
    private String _id;
}
