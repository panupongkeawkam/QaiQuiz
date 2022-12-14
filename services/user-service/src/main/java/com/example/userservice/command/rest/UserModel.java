package com.example.userservice.command.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class UserModel implements Serializable {

    private String _id;
    private String name;
    private Date createDateTime;
}
