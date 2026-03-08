package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.ExamDTO;
import com.example.resultmanagementsystem.Dto.ExamResultDTO;
import com.example.resultmanagementsystem.Dto.ExamScheduleDTO;
import com.example.resultmanagementsystem.Dto.QuestionBankDTO;
import com.example.resultmanagementsystem.Dto.Repository.ExamRepository;
import com.example.resultmanagementsystem.Dto.Repository.ExamResultRepository;
import com.example.resultmanagementsystem.Dto.Repository.ExamScheduleRepository;
import com.example.resultmanagementsystem.Dto.Repository.QuestionBankRepository;
import com.example.resultmanagementsystem.model.Exam;
import com.example.resultmanagementsystem.model.ExamResult;
import com.example.resultmanagementsystem.model.ExamSchedule;
import com.example.resultmanagementsystem.model.QuestionBank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionBankRepository questionBankRepository;
    private final ExamResultRepository examResultRepository;
    private final ExamScheduleRepository examScheduleRepository;

    // ===== Exam Methods =====

    public Exam createExam(ExamDTO examDTO) {
        Exam exam = new Exam();
        exam.setExamId(UUID.randomUUID().toString());
        exam.setTitle(examDTO.getTitle());
        exam.setDescription(examDTO.getDescription());
        exam.setCourseId(examDTO.getCourseId());
        exam.setDepartmentId(examDTO.getDepartmentId());
        exam.setTeacherId(examDTO.getTeacherId());
        exam.setExamType(examDTO.getExamType());
        exam.setTotalMarks(examDTO.getTotalMarks());
        exam.setPassingMarks(examDTO.getPassingMarks());
        exam.setDuration(examDTO.getDuration());
        exam.setStartDateTime(examDTO.getStartDateTime());
        exam.setEndDateTime(examDTO.getEndDateTime());
        exam.setVenue(examDTO.getVenue());
        exam.setAcademicYear(examDTO.getAcademicYear());
        exam.setSemester(examDTO.getSemester());
        exam.setInstructions(examDTO.getInstructions());
        exam.setStatus(Exam.ExamStatus.SCHEDULED);
        exam.setCreatedAt(LocalDateTime.now());
        exam.setUpdatedAt(LocalDateTime.now());
        return examRepository.save(exam);
    }

    public Exam getExamById(String examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));
    }

    public List<Exam> getExamsByCourse(String courseId) {
        return examRepository.findByCourseId(courseId);
    }

    public List<Exam> getExamsByDepartment(String departmentId) {
        return examRepository.findByDepartmentIdContaining(departmentId);
    }

    public List<Exam> getExamsByTeacher(String teacherId) {
        return examRepository.findByTeacherId(teacherId);
    }

    public Exam updateExamStatus(String examId, Exam.ExamStatus status) {
        Exam exam = getExamById(examId);
        exam.setStatus(status);
        exam.setUpdatedAt(LocalDateTime.now());
        return examRepository.save(exam);
    }

    public List<Exam> getUpcomingExams() {
        return examRepository.findByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime.now());
    }

    // ===== Question Bank Methods =====

    public QuestionBank addQuestion(QuestionBankDTO dto) {
        QuestionBank question = new QuestionBank();
        question.setId(UUID.randomUUID().toString());
        question.setCourseId(dto.getCourseId());
        question.setTeacherId(dto.getTeacherId());
        question.setQuestion(dto.getQuestion());
        question.setOptions(dto.getOptions());
        question.setCorrectAnswer(dto.getCorrectAnswer());
        question.setQuestionType(dto.getQuestionType());
        question.setDifficulty(dto.getDifficulty());
        question.setMarks(dto.getMarks());
        question.setTopic(dto.getTopic());
        question.setExplanation(dto.getExplanation());
        question.setActive(true);
        question.setCreatedAt(LocalDateTime.now());
        return questionBankRepository.save(question);
    }

    public List<QuestionBank> getQuestionsByCourse(String courseId) {
        return questionBankRepository.findByCourseId(courseId);
    }

    public List<QuestionBank> getQuestionsByTopic(String topic) {
        return questionBankRepository.findByTopic(topic);
    }

    public List<QuestionBank> generateRandomQuestions(String courseId, int count, QuestionBank.Difficulty difficulty) {
        List<QuestionBank> questions;
        if (difficulty != null) {
            questions = questionBankRepository.findByCourseIdAndDifficultyAndIsActiveTrue(courseId, difficulty);
        } else {
            questions = questionBankRepository.findByCourseIdAndIsActiveTrue(courseId);
        }

        if (questions.size() <= count) {
            return questions;
        }

        List<QuestionBank> shuffled = new ArrayList<>(questions);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }

    // ===== Exam Result Methods =====

    public ExamResult submitExamResult(ExamResultDTO dto) {
        ExamResult result = new ExamResult();
        result.setId(UUID.randomUUID().toString());
        result.setExamId(dto.getExamId());
        result.setStudentId(dto.getStudentId());
        result.setAnswers(dto.getAnswers());
        result.setTotalScore(dto.getTotalScore());
        result.setPercentage(dto.getPercentage());
        result.setGrade(dto.getGrade());
        result.setStatus(dto.getStatus());
        result.setStartedAt(dto.getStartedAt());
        result.setSubmittedAt(dto.getSubmittedAt() != null ? dto.getSubmittedAt() : LocalDateTime.now());
        result.setFeedback(dto.getFeedback());

        // Auto-calculate percentage if total score is provided
        Exam exam = getExamById(dto.getExamId());
        if (exam.getTotalMarks() > 0 && dto.getTotalScore() > 0) {
            double percentage = (dto.getTotalScore() / exam.getTotalMarks()) * 100;
            result.setPercentage(percentage);
            result.setGrade(calculateGrade(percentage));
            result.setStatus(dto.getTotalScore() >= exam.getPassingMarks()
                    ? ExamResult.ExamResultStatus.PASSED
                    : ExamResult.ExamResultStatus.FAILED);
        }

        return examResultRepository.save(result);
    }

    public List<ExamResult> getExamResults(String examId) {
        return examResultRepository.findByExamId(examId);
    }

    public List<ExamResult> getStudentExamResults(String studentId) {
        return examResultRepository.findByStudentId(studentId);
    }

    public ExamResult gradeExam(String examResultId, String gradedBy, String feedback) {
        ExamResult result = examResultRepository.findById(examResultId)
                .orElseThrow(() -> new RuntimeException("Exam result not found with ID: " + examResultId));
        result.setGradedBy(gradedBy);
        result.setGradedAt(LocalDateTime.now());
        result.setFeedback(feedback);

        // Recalculate total score from answers
        if (result.getAnswers() != null && !result.getAnswers().isEmpty()) {
            double totalScore = result.getAnswers().stream()
                    .mapToDouble(ExamResult.StudentAnswer::getMarksObtained)
                    .sum();
            result.setTotalScore(totalScore);

            Exam exam = getExamById(result.getExamId());
            if (exam.getTotalMarks() > 0) {
                double percentage = (totalScore / exam.getTotalMarks()) * 100;
                result.setPercentage(percentage);
                result.setGrade(calculateGrade(percentage));
                result.setStatus(totalScore >= exam.getPassingMarks()
                        ? ExamResult.ExamResultStatus.PASSED
                        : ExamResult.ExamResultStatus.FAILED);
            }
        }

        return examResultRepository.save(result);
    }

    // ===== Exam Schedule Methods =====

    public ExamSchedule createExamSchedule(ExamScheduleDTO dto) {
        ExamSchedule schedule = new ExamSchedule();
        schedule.setId(UUID.randomUUID().toString());
        schedule.setExamId(dto.getExamId());
        schedule.setDepartmentId(dto.getDepartmentId());
        schedule.setVenue(dto.getVenue());
        schedule.setInvigilatorId(dto.getInvigilatorId());
        schedule.setDate(dto.getDate());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setAcademicYear(dto.getAcademicYear());
        schedule.setSemester(dto.getSemester());
        schedule.setStatus(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED");
        return examScheduleRepository.save(schedule);
    }

    public List<ExamSchedule> getScheduleByDepartment(String departmentId) {
        return examScheduleRepository.findByDepartmentId(departmentId);
    }

    public List<ExamSchedule> getScheduleByDate(LocalDate date) {
        return examScheduleRepository.findByDate(date);
    }

    // ===== Helper Methods =====

    private String calculateGrade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 60) return "B";
        if (percentage >= 50) return "C";
        if (percentage >= 40) return "D";
        return "F";
    }
}
