package com.example.resultservice4.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Document("Result")
@NoArgsConstructor
@AllArgsConstructor

public class Result implements Serializable {

    @Id
    private String _id;
    private String quizId;
    private String userId;
    private String quizName;
    private String category;
    private Date createDateTime;
    private int score;
    private int fullScore;
}
