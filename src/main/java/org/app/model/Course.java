package org.app.model;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Course class for BUP UCAM Assignment Tracker
 */
public class Course {
    @JsonProperty("courseId")
    private String courseId;
    @JsonProperty("courseName")
    private String courseName;
    @JsonProperty("courseCode")
    private String courseCode;
    @JsonProperty("department")
    private String department;
    @JsonProperty("creditHours")
    private int creditHours;
    @JsonProperty("semester")
    private String semester;
    @JsonProperty("instructor")
    private Teacher instructor;
    @JsonIgnore
    private List<Student> enrolledStudents;
    @JsonIgnore
    private List<Assignment> assignments;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    @JsonProperty("isActive")
    private boolean isActive;

    // Default constructor for Jackson
    public Course() {
        this.enrolledStudents = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    public Course(String courseId, String courseName, String courseCode,
                  String department, int creditHours, String semester, Teacher instructor) {
        this();
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.department = department;
        this.creditHours = creditHours;
        this.semester = semester;
        this.instructor = instructor;
    }

    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            student.enrollInCourse(this);
        }
    }

    public void addAssignment(Assignment assignment) {
        if (!assignments.contains(assignment)) {
            assignments.add(assignment);
        }
    }

    public void displayCourseInfo() {
        System.out.println("=== Course Information ===");
        System.out.println("Course: " + courseName + " (" + courseCode + ")");
        System.out.println("Department: " + department);
        System.out.println("Credit Hours: " + creditHours);
        System.out.println("Semester: " + semester);
        System.out.println("Instructor: " + instructor.getName());
        System.out.println("Enrolled Students: " + enrolledStudents.size());
        System.out.println("Total Assignments: " + assignments.size());
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Teacher getInstructor() { return instructor; }
    public void setInstructor(Teacher instructor) { this.instructor = instructor; }

    public List<Student> getEnrolledStudents() { return new ArrayList<>(enrolledStudents); }

    public List<Assignment> getAssignments() { return new ArrayList<>(assignments); }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return String.format("Course{id='%s', name='%s', code='%s', instructor='%s'}",
                courseId, courseName, courseCode, instructor.getName());
    }
}
