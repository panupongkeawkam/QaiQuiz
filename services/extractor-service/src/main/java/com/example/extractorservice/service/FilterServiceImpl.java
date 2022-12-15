package com.example.extractorservice.service;

import com.proto.data.*;
import io.grpc.stub.StreamObserver;

public class FilterServiceImpl extends FilterServiceGrpc.FilterServiceImplBase {

    @Override
    public void filter(RequestFilterQuiz request, StreamObserver<ExtractedQuiz> responseObserver) {
        for (Quiz quiz : request.getQuizzesList()){
            if(quiz.getCategory().equals(request.getCategory())) {
                ExtractedQuiz extractedQuiz = ExtractedQuiz.newBuilder()
                        .setQuiz(Quiz.newBuilder()
                                .setId(quiz.getId())
                                .setTitle(quiz.getTitle())
                                .setCategory(quiz.getCategory())
                                .setCreateDateTime(quiz.getCreateDateTime())
                                .addAllQuestions(quiz.getQuestionsList())
                                .build())
                        .build();
            responseObserver.onNext(extractedQuiz);
            }
        }
        responseObserver.onCompleted();
    }


}
