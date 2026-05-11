package org.app.service;

import org.app.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Course Management Service for BUP UCAM Assignment Tracker
 * Simplified to avoid persistence issues - courses will be created fresh each session
 */
public class CourseService {
    private Map<String, Course> courses;
    private int courseCounter;

    public CourseService() {
        this.courses = new HashMap<>();
        this.courseCounter = 1;
        System.out.println("✓ CourseService initialized (in-memory storage)");
    }

    /**
     * Create a new course
     */
    public Course createCourse(String courseName, String courseCode, String department,
                              int creditHours, String semester, Teacher instructor) {
        String courseId = "CRS-" + String.format("%04d", courseCounter++);
        Course course = new Course(courseId, courseName, courseCode, department,
                                 creditHours, semester, instructor);

        courses.put(courseId, course);
        instructor.addCourse(course);

        System.out.println("✓ Course created successfully: " + courseName);
        return course;
    }

    /**
     * Enroll student in course
     */
    public void enrollStudent(String courseId, Student student) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found");
        }

        course.enrollStudent(student);
        System.out.println("✓ Student enrolled: " + student.getName() + " in " + course.getCourseName());
    }

    /**
     * Get courses by department
     */
    public List<Course> getCoursesByDepartment(String department) {
        return courses.values().stream()
                .filter(course -> course.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    /**
     * Get courses by semester
     */
    public List<Course> getCoursesBySemester(String semester) {
        return courses.values().stream()
                .filter(course -> course.getSemester().equalsIgnoreCase(semester))
                .collect(Collectors.toList());
    }

    /**
     * Get courses taught by teacher
     */
    public List<Course> getCoursesByTeacher(Teacher teacher) {
        return courses.values().stream()
                .filter(course -> course.getInstructor().equals(teacher))
                .collect(Collectors.toList());
    }

    /**
     * Get courses for student
     */
    public List<Course> getCoursesForStudent(Student student) {
        return courses.values().stream()
                .filter(course -> course.getEnrolledStudents().contains(student))
                .collect(Collectors.toList());
    }

    /**
     * Display course statistics
     */
    public void displayCourseStatistics(Course course) {
        System.out.println("=== Course Statistics ===");
        System.out.println("Course: " + course.getCourseName());
        System.out.println("Enrolled Students: " + course.getEnrolledStudents().size());
        System.out.println("Total Assignments: " + course.getAssignments().size());
        System.out.println("Instructor: " + course.getInstructor().getName());
    }

    // Getters
    public Course getCourse(String courseId) {
        return courses.get(courseId);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public Course findCourseByCode(String courseCode) {
        return courses.values().stream()
                .filter(course -> course.getCourseCode().equalsIgnoreCase(courseCode))
                .findFirst()
                .orElse(null);
    }
}
