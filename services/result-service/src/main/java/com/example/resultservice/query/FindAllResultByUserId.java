package com.example.resultservice.query;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FindAllResultByUserId implements Serializable {
    private String userId;
}
