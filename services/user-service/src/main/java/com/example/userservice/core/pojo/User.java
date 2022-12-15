package com.example.userservice.core.pojo;

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
@Document("User")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    private String _id;
    private String name;
    private Date createDateTime;

}
