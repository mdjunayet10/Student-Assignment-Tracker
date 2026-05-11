package org.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Submission class for BUP UCAM Assignment Tracker
 */
public class Submission {
    private String submissionId;
    private Assignment assignment;
    private Student student;
    private String content;
    private List<String> attachmentPaths;
    private LocalDateTime submittedAt;
    private SubmissionStatus status;
    private Integer marks;
    private String feedback;
    private LocalDateTime gradedAt;
    private Teacher gradedBy;

    public Submission(String submissionId, Assignment assignment, Student student, String content) {
        this.submissionId = submissionId;
        this.assignment = assignment;
        this.student = student;
        this.content = content;
        this.attachmentPaths = new ArrayList<>();
        this.submittedAt = LocalDateTime.now();
        this.status = SubmissionStatus.SUBMITTED;
    }

    public void addAttachment(String filePath) {
        if (!attachmentPaths.contains(filePath)) {
            attachmentPaths.add(filePath);
        }
    }

    public void grade(int marks, String feedback, Teacher gradedBy) {
        this.marks = marks;
        this.feedback = feedback;
        this.gradedBy = gradedBy;
        this.gradedAt = LocalDateTime.now();
        this.status = SubmissionStatus.GRADED;
    }

    public boolean isLateSubmission() {
        return submittedAt.isAfter(assignment.getDueDate());
    }

    public void displaySubmissionInfo() {
        System.out.println("=== Submission Details ===");
        System.out.println("Assignment: " + assignment.getTitle());
        System.out.println("Student: " + student.getName());
        System.out.println("Submitted At: " + submittedAt);
        System.out.println("Status: " + status);
        System.out.println("Late Submission: " + (isLateSubmission() ? "Yes" : "No"));
        if (marks != null) {
            System.out.println("Marks: " + marks + "/" + assignment.getMaxMarks());
        }
        if (feedback != null) {
            System.out.println("Feedback: " + feedback);
        }
    }

    // Getters and Setters
    public String getSubmissionId() { return submissionId; }
    public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getAttachmentPaths() { return new ArrayList<>(attachmentPaths); }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }

    public Teacher getGradedBy() { return gradedBy; }
    public void setGradedBy(Teacher gradedBy) { this.gradedBy = gradedBy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Submission)) return false;
        Submission that = (Submission) o;
        return Objects.equals(submissionId, that.submissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(submissionId);
    }

    @Override
    public String toString() {
        return String.format("Submission{id='%s', assignment='%s', student='%s', status=%s}",
                submissionId, assignment.getTitle(), student.getName(), status);
    }
}
