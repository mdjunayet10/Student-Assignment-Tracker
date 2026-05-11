package org.app.service;

import org.app.model.*;
import org.app.util.DataPersistence;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Assignment Management Service for BUP UCAM Assignment Tracker
 */
public class AssignmentService {
    private Map<String, Assignment> assignments;
    private Map<String, Submission> submissions;
    private int assignmentCounter;
    private int submissionCounter;
    private DataPersistence dataPersistence;

    public AssignmentService() {
        this.dataPersistence = new DataPersistence();
        loadData();
    }

    private void loadData() {
        DataPersistence.AssignmentData assignmentData = dataPersistence.loadAssignments();
        this.assignments = assignmentData.assignments;
        this.submissions = assignmentData.submissions;
        this.assignmentCounter = assignmentData.assignmentCounter;
        this.submissionCounter = assignmentData.submissionCounter;
    }

    private void saveData() {
        dataPersistence.saveAssignments(assignments, submissions, assignmentCounter, submissionCounter);
    }

    /**
     * Create a new assignment
     */
    public Assignment createAssignment(String title, String description, Course course,
                                     Teacher creator, AssignmentType type, int maxMarks,
                                     LocalDateTime dueDate) {
        String assignmentId = "ASSIGN-" + String.format("%04d", assignmentCounter++);
        Assignment assignment = new Assignment(assignmentId, title, description, course,
                                             creator, type, maxMarks, dueDate);

        assignments.put(assignmentId, assignment);
        course.addAssignment(assignment);
        creator.addAssignment(assignment);

        saveData(); // Save after creating assignment
        System.out.println("✓ Assignment created successfully: " + title);
        return assignment;
    }

    /**
     * Submit assignment by student
     */
    public Submission submitAssignment(Assignment assignment, Student student, String content) {
        if (assignment.getStatus() != AssignmentStatus.ACTIVE) {
            throw new IllegalStateException("Assignment is not active for submissions");
        }

        String submissionId = "SUB-" + String.format("%04d", submissionCounter++);
        Submission submission = new Submission(submissionId, assignment, student, content);

        submissions.put(submissionId, submission);
        assignment.addSubmission(submission);
        student.addSubmission(submission);

        saveData(); // Save after submission
        System.out.println("✓ Assignment submitted successfully by " + student.getName());
        return submission;
    }

    /**
     * Grade a submission
     */
    public void gradeSubmission(String submissionId, int marks, String feedback, Teacher teacher) {
        Submission submission = submissions.get(submissionId);
        if (submission == null) {
            throw new IllegalArgumentException("Submission not found");
        }

        if (marks > submission.getAssignment().getMaxMarks()) {
            throw new IllegalArgumentException("Marks cannot exceed maximum marks");
        }

        submission.grade(marks, feedback, teacher);
        saveData(); // Save after grading
        System.out.println("✓ Submission graded successfully");
    }

    /**
     * Get assignments by course
     */
    public List<Assignment> getAssignmentsByCourse(Course course) {
        return assignments.values().stream()
                .filter(assignment -> assignment.getCourse().equals(course))
                .collect(Collectors.toList());
    }

    /**
     * Get assignments by teacher
     */
    public List<Assignment> getAssignmentsByTeacher(Teacher teacher) {
        return assignments.values().stream()
                .filter(assignment -> assignment.getCreator().equals(teacher))
                .collect(Collectors.toList());
    }

    /**
     * Get submissions for an assignment
     */
    public List<Submission> getSubmissionsForAssignment(Assignment assignment) {
        return submissions.values().stream()
                .filter(submission -> submission.getAssignment().equals(assignment))
                .collect(Collectors.toList());
    }

    /**
     * Get submissions by student
     */
    public List<Submission> getSubmissionsByStudent(Student student) {
        return submissions.values().stream()
                .filter(submission -> submission.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    /**
     * Get overdue assignments
     */
    public List<Assignment> getOverdueAssignments() {
        return assignments.values().stream()
                .filter(Assignment::isOverdue)
                .collect(Collectors.toList());
    }

    /**
     * Close assignment for submissions
     */
    public void closeAssignment(String assignmentId) {
        Assignment assignment = assignments.get(assignmentId);
        if (assignment != null) {
            assignment.setStatus(AssignmentStatus.CLOSED);
            saveData(); // Save after status change
            System.out.println("✓ Assignment closed: " + assignment.getTitle());
        }
    }

    /**
     * Get assignment statistics
     */
    public void displayAssignmentStatistics(Assignment assignment) {
        List<Submission> assignmentSubmissions = getSubmissionsForAssignment(assignment);
        int totalStudents = assignment.getCourse().getEnrolledStudents().size();
        int submittedCount = assignmentSubmissions.size();
        int gradedCount = (int) assignmentSubmissions.stream()
                .filter(sub -> sub.getStatus() == SubmissionStatus.GRADED)
                .count();

        System.out.println("=== Assignment Statistics ===");
        System.out.println("Assignment: " + assignment.getTitle());
        System.out.println("Total Students: " + totalStudents);
        System.out.println("Submissions: " + submittedCount + "/" + totalStudents);
        System.out.println("Graded: " + gradedCount + "/" + submittedCount);
        System.out.println("Submission Rate: " + String.format("%.1f%%",
                (submittedCount * 100.0) / totalStudents));
    }

    // Getters
    public Assignment getAssignment(String assignmentId) {
        return assignments.get(assignmentId);
    }

    public Submission getSubmission(String submissionId) {
        return submissions.get(submissionId);
    }

    public List<Assignment> getAllAssignments() {
        return new ArrayList<>(assignments.values());
    }

    public List<Submission> getAllSubmissions() {
        return new ArrayList<>(submissions.values());
    }
}
