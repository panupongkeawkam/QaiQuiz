package com.example.extractorservice.service;

import com.proto.data.ExtractedQuiz;
import com.proto.data.Quiz;
import com.proto.data.RequestSearchQuiz;
import com.proto.data.SearchServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SearchServiceImpl extends SearchServiceGrpc.SearchServiceImplBase {

    @Override
    public void search(RequestSearchQuiz request, StreamObserver<ExtractedQuiz> responseObserver) {
        for(Quiz quiz : request.getQuizzesList()) {
            if(quiz.getTitle().toLowerCase().contains(request.getSearchQuery().toLowerCase())) {
                ExtractedQuiz extractedQuiz =  ExtractedQuiz.newBuilder()
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
