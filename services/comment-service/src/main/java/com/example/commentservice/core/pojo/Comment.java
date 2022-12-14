package com.example.commentservice.core.pojo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Document("Comment")
@Builder
public class Comment implements Serializable {
    @Id
    private String _id;
    private String quizId;
    private String userId;
    private String username;
    private String message;
    private Date createDateTime;
}
