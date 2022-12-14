package com.example.quizservice.pojo;

import com.example.quizservice.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document("Quiz")
@NoArgsConstructor
@AllArgsConstructor
public class Quiz implements Serializable {

    @Id
    private String _id;
    private String title;
    private Date createDateTime;
    private List<Question> questions;
    private String category;
}
