package org.app.model;

import com.fasterxml.jackson.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Teacher class for BUP UCAM Assignment Tracker
 */
@JsonTypeName("teacher")
public class Teacher extends User {
    @JsonProperty("department")
    private String department;
    @JsonProperty("employeeId")
    private String employeeId;
    @JsonIgnore
    private List<Course> coursesTaught;
    @JsonIgnore
    private List<Assignment> assignmentsCreated;

    // Default constructor for Jackson
    public Teacher() {
        super();
        this.coursesTaught = new ArrayList<>();
        this.assignmentsCreated = new ArrayList<>();
    }

    public Teacher(String userId, String name, String email, String password,
                   String department, String employeeId) {
        super(userId, name, email, password, UserRole.TEACHER);
        this.department = department;
        this.employeeId = employeeId;
        this.coursesTaught = new ArrayList<>();
        this.assignmentsCreated = new ArrayList<>();
    }

    @Override
    public void displayDashboard() {
        System.out.println("=== Teacher Dashboard - " + name + " ===");
        System.out.println("Department: " + department);
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Courses Teaching: " + coursesTaught.size());
        System.out.println("Assignments Created: " + assignmentsCreated.size());
    }

    @Override
    public String getDisplayName() {
        return "Prof. " + name + " (" + department + ")";
    }

    public void addCourse(Course course) {
        if (!coursesTaught.contains(course)) {
            coursesTaught.add(course);
        }
    }

    public void addAssignment(Assignment assignment) {
        if (!assignmentsCreated.contains(assignment)) {
            assignmentsCreated.add(assignment);
        }
    }

    // Getters and Setters
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public List<Course> getCoursesTaught() { return new ArrayList<>(coursesTaught); }

    public List<Assignment> getAssignmentsCreated() { return new ArrayList<>(assignmentsCreated); }
}
