package com.example.userservice.query.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FindUserById implements Serializable {

    private String _id;

}
