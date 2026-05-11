package org.app.model;

import com.fasterxml.jackson.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Student class for BUP UCAM Assignment Tracker
 */
@JsonTypeName("student")
public class Student extends User {
    @JsonProperty("studentId")
    private String studentId;
    @JsonProperty("program")
    private String program;
    @JsonProperty("semester")
    private int semester;
    @JsonIgnore
    private List<Course> enrolledCourses;
    @JsonIgnore
    private List<Submission> submissions;

    // Default constructor for Jackson
    public Student() {
        super();
        this.enrolledCourses = new ArrayList<>();
        this.submissions = new ArrayList<>();
    }

    public Student(String userId, String name, String email, String password,
                   String studentId, String program, int semester) {
        super(userId, name, email, password, UserRole.STUDENT);
        this.studentId = studentId;
        this.program = program;
        this.semester = semester;
        this.enrolledCourses = new ArrayList<>();
        this.submissions = new ArrayList<>();
    }

    @Override
    public void displayDashboard() {
        System.out.println("=== Student Dashboard - " + name + " ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Program: " + program);
        System.out.println("Semester: " + semester);
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
        System.out.println("Total Submissions: " + submissions.size());
    }

    @Override
    public String getDisplayName() {
        return name + " (" + studentId + ")";
    }

    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    public void addSubmission(Submission submission) {
        if (!submissions.contains(submission)) {
            submissions.add(submission);
        }
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public List<Course> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }

    public List<Submission> getSubmissions() { return new ArrayList<>(submissions); }
}
