package org.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Assignment class for BUP UCAM Assignment Tracker
 */
public class Assignment {
    private String assignmentId;
    private String title;
    private String description;
    private Course course;
    private Teacher creator;
    private AssignmentType type;
    private int maxMarks;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private boolean isActive;
    private List<String> attachmentPaths;
    private List<Submission> submissions;
    private AssignmentStatus status;

    public Assignment(String assignmentId, String title, String description,
                     Course course, Teacher creator, AssignmentType type,
                     int maxMarks, LocalDateTime dueDate) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
        this.course = course;
        this.creator = creator;
        this.type = type;
        this.maxMarks = maxMarks;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.attachmentPaths = new ArrayList<>();
        this.submissions = new ArrayList<>();
        this.status = AssignmentStatus.ACTIVE;
    }

    public void addSubmission(Submission submission) {
        if (!submissions.contains(submission)) {
            submissions.add(submission);
        }
    }

    public void addAttachment(String filePath) {
        if (!attachmentPaths.contains(filePath)) {
            attachmentPaths.add(filePath);
        }
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && status == AssignmentStatus.ACTIVE;
    }

    public int getSubmissionCount() {
        return submissions.size();
    }

    public void displayAssignmentInfo() {
        System.out.println("=== Assignment Details ===");
        System.out.println("Title: " + title);
        System.out.println("Course: " + course.getCourseName());
        System.out.println("Type: " + type);
        System.out.println("Max Marks: " + maxMarks);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Status: " + status);
        System.out.println("Submissions: " + submissions.size());
        System.out.println("Created by: " + creator.getName());
    }

    // Getters and Setters
    public String getAssignmentId() { return assignmentId; }
    public void setAssignmentId(String assignmentId) { this.assignmentId = assignmentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Teacher getCreator() { return creator; }
    public void setCreator(Teacher creator) { this.creator = creator; }

    public AssignmentType getType() { return type; }
    public void setType(AssignmentType type) { this.type = type; }

    public int getMaxMarks() { return maxMarks; }
    public void setMaxMarks(int maxMarks) { this.maxMarks = maxMarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    public List<String> getAttachmentPaths() { return new ArrayList<>(attachmentPaths); }

    public List<Submission> getSubmissions() { return new ArrayList<>(submissions); }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment)) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(assignmentId, that.assignmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentId);
    }

    @Override
    public String toString() {
        return String.format("Assignment{id='%s', title='%s', course='%s', dueDate=%s}",
                assignmentId, title, course.getCourseCode(), dueDate);
    }
}
