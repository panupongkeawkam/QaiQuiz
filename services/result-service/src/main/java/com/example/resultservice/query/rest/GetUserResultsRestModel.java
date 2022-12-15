package com.example.resultservice.query.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResultsRestModel implements Serializable {

    private String userId;

}
