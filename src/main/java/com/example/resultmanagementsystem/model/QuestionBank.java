package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "question_banks")
public class QuestionBank {
    @Id
    private String id = UUID.randomUUID().toString();
    private String courseId;
    private String teacherId;
    private String question;
    private List<String> options;
    private String correctAnswer;
    private QuestionType questionType;
    private Difficulty difficulty;
    private int marks;
    private String topic;
    private String explanation;
    private boolean isActive;
    private LocalDateTime createdAt;

    public enum QuestionType {
        MCQ, TRUE_FALSE, SHORT_ANSWER, ESSAY, FILL_IN_BLANK, MATCHING
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
