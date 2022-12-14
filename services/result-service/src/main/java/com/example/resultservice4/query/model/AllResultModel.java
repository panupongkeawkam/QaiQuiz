package com.example.resultservice4.query.model;

import com.example.resultservice4.core.pojo.Result;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class AllResultModel implements Serializable {
    private List<Result> resultList;
}
