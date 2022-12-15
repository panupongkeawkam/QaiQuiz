package com.example.extractorservice.rest;

import com.example.extractorservice.model.*;
import com.proto.data.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class ExtractorController {

    @RequestMapping(value = "/filter", method = RequestMethod.PATCH)
    public ExtractedQuizModel getFilteredQuizzes(@RequestBody FilterQuizRestModel body) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        FilterServiceGrpc.FilterServiceBlockingStub syncClient = FilterServiceGrpc.newBlockingStub(channel);

        String category = body.getCategory();
        List<QuizModel> quizModels = body.getQuizzes();
        List<Quiz> quizzes = new ArrayList<>();

        Quiz quiz;

        for (QuizModel quizModel : quizModels) {

            List<QuestionModel> questionModels = quizModel.getQuestions();
            List<Question> questions = new ArrayList<>();

            for (QuestionModel questionModel : questionModels) {

                List<String> choiceModels = questionModel.getChoices();
                Iterator<String> choiceIterator = choiceModels.iterator();
                Question question = Question.newBuilder()
                        .setTitle(questionModel.getTitle())
                        .setCorrect(questionModel.getCorrect())
                        .addAllChoices(getIterableFromIterator(choiceIterator))
                        .build();
                questions.add(question);

            }

            Iterator<Question> questionIterator = questions.iterator();

            quiz = Quiz.newBuilder()
                    .setId(quizModel.get_id())
                    .setTitle(quizModel.getTitle())
                    .setCategory(quizModel.getCategory())
                    .setCreateDateTime(quizModel.getCreateDateTime())
                    .addAllQuestions(getIterableFromIterator(questionIterator))
                    .build();

            quizzes.add(quiz);
        }

        Iterator<Quiz> quizIterator = quizzes.iterator();

        RequestFilterQuiz requestFilterQuiz = RequestFilterQuiz.newBuilder()
                .setCategory(category)
                .addAllQuizzes(getIterableFromIterator(quizIterator))
                .build();


        List<QuizModel> quizModelList = new ArrayList<>();
        syncClient.filter(requestFilterQuiz).forEachRemaining(extractedQuiz -> {
            List<QuestionModel> questionList = new ArrayList<>();
            for (Question question : extractedQuiz.getQuiz().getQuestionsList()) {
                QuestionModel questionModel = QuestionModel.builder()
                        .title(question.getTitle())
                        .correct(question.getCorrect())
                        .choices(question.getChoicesList())
                        .build();
                questionList.add(questionModel);
            }
            QuizModel quizModel = QuizModel.builder()
                    ._id(extractedQuiz.getQuiz().getId())
                    .title(extractedQuiz.getQuiz().getTitle())
                    .category(extractedQuiz.getQuiz().getCategory())
                    .createDateTime(extractedQuiz.getQuiz().getCreateDateTime())
                    .questions(questionList)
                    .build();
            quizModelList.add(quizModel);
        });

        ExtractedQuizModel extractedQuizModel = ExtractedQuizModel.builder()
                .quizzes(quizModelList)
                .build();

        channel.shutdown();


        return extractedQuizModel;
    }

    @RequestMapping(value = "/search", method = RequestMethod.PATCH)
    public ExtractedQuizModel getSearchedQuizzes(@RequestBody SearchQuizRestModel body) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        SearchServiceGrpc.SearchServiceBlockingStub syncClient = SearchServiceGrpc.newBlockingStub(channel);

        String query = body.getQuery();
        List<QuizModel> quizModelList = body.getQuizzes();
        List<Quiz> quizList = new ArrayList<>();

        for (QuizModel quizModel : quizModelList) {
            List<Question> questionList = new ArrayList<>();
            for (QuestionModel questionModel : quizModel.getQuestions()) {
                Question question = Question.newBuilder()
                        .setTitle(questionModel.getTitle())
                        .setCorrect(questionModel.getCorrect())
                        .addAllChoices(questionModel.getChoices())
                        .build();
                questionList.add(question);
            }
            Quiz quiz = Quiz.newBuilder()
                    .setId(quizModel.get_id())
                    .setTitle(quizModel.getTitle())
                    .setCategory(quizModel.getCategory())
                    .setCreateDateTime(quizModel.getCreateDateTime())
                    .addAllQuestions(questionList)
                    .build();
            quizList.add(quiz);
        }

        RequestSearchQuiz searchQuiz = RequestSearchQuiz.newBuilder()
                .addAllQuizzes(quizList)
                .setSearchQuery(query)
                .build();

        List<QuizModel> quizzes = new ArrayList<>();
        syncClient.search(searchQuiz).forEachRemaining(extractedQuiz -> {
            List<QuestionModel> questionModelList = new ArrayList<>();
            for(Question question: extractedQuiz.getQuiz().getQuestionsList()) {
                QuestionModel questionModel = QuestionModel.builder()
                        .title(question.getTitle())
                        .correct(question.getCorrect())
                        .choices(question.getChoicesList())
                        .build();
                questionModelList.add(questionModel);
            }
            QuizModel quizModel = QuizModel.builder()
                    ._id(extractedQuiz.getQuiz().getId())
                    .title(extractedQuiz.getQuiz().getTitle())
                    .category(extractedQuiz.getQuiz().getCategory())
                    .createDateTime(extractedQuiz.getQuiz().getCreateDateTime())
                    .questions(questionModelList)
                    .build();
            quizzes.add(quizModel);
        });

        ExtractedQuizModel extractedQuizModel = ExtractedQuizModel.builder()
                .quizzes(quizzes)
                .build();

        channel.shutdown();

        return extractedQuizModel;
    }

    public static <T> Iterable<T>
    getIterableFromIterator(Iterator<T> iterator) {
        return () -> iterator;
    }

}
