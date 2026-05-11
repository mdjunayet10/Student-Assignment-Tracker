package org.app.controller;

import org.app.model.*;
import org.app.service.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application Controller for BUP UCAM Assignment Tracker
 */
public class AssignmentTrackerController {
    private UserService userService;
    private CourseService courseService;
    private AssignmentService assignmentService;
    private Scanner scanner;
    private User currentUser;

    public AssignmentTrackerController() {
        this.userService = new UserService();
        this.courseService = new CourseService();
        this.assignmentService = new AssignmentService();
        this.scanner = new Scanner(System.in);
        initializeDefaultData();
    }

    /**
     * Initialize some default data for demonstration
     */
    private void initializeDefaultData() {
        // Create sample teachers
        Teacher teacher1 = userService.registerTeacher("Dr. Ahmed Rahman", "ahmed@bup.edu.bd",
                "password123", "Computer Science", "EMP001");
        Teacher teacher2 = userService.registerTeacher("Prof. Sarah Khan", "sarah@bup.edu.bd",
                "password123", "Business Administration", "EMP002");

        // Create sample students
        Student student1 = userService.registerStudent("Mohammad Ali", "ali@student.bup.edu.bd",
                "student123", "201901001", "CSE", 7);
        Student student2 = userService.registerStudent("Fatima Hassan", "fatima@student.bup.edu.bd",
                "student123", "201901002", "CSE", 7);

        // Create sample courses
        Course course1 = courseService.createCourse("Object Oriented Programming", "CSE-202",
                "Computer Science", 3, "Fall 2024", teacher1);
        Course course2 = courseService.createCourse("Business Management", "BBA-101",
                "Business Administration", 3, "Fall 2024", teacher2);

        // Enroll students in courses
        courseService.enrollStudent(course1.getCourseId(), student1);
        courseService.enrollStudent(course1.getCourseId(), student2);
        courseService.enrollStudent(course2.getCourseId(), student1);

        System.out.println("✓ Sample data initialized successfully!");
    }

    /**
     * Start the application
     */
    public void start() {
        System.out.println("==============================================");
        System.out.println("    Welcome to BUP UCAM Assignment Tracker   ");
        System.out.println("==============================================");
        System.out.println("A comprehensive assignment management system");
        System.out.println("for Bangladesh University of Professionals");
        System.out.println("==============================================\n");

        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register Teacher");
        System.out.println("3. Register Student");
        System.out.println("4. Exit");
        System.out.print("Choose option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> login();
            case 2 -> registerTeacher();
            case 3 -> registerStudent();
            case 4 -> {
                System.out.println("Thank you for using BUP UCAM Assignment Tracker!");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = userService.authenticateUser(email, password);
        if (currentUser != null) {
            System.out.println("✓ Login successful! Welcome, " + currentUser.getName());
            currentUser.displayDashboard();
        } else {
            System.out.println("✗ Invalid credentials. Please try again.");
        }
    }

    private void registerTeacher() {
        System.out.println("\n=== Teacher Registration ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Employee ID: ");
        String employeeId = scanner.nextLine();

        userService.registerTeacher(name, email, password, department, employeeId);
    }

    private void registerStudent() {
        System.out.println("\n=== Student Registration ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Program: ");
        String program = scanner.nextLine();
        System.out.print("Semester: ");
        int semester = getIntInput();

        userService.registerStudent(name, email, password, studentId, program, semester);
    }

    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        if (currentUser.getRole() == UserRole.TEACHER) {
            showTeacherMenu();
        } else if (currentUser.getRole() == UserRole.STUDENT) {
            showStudentMenu();
        }
        System.out.println("0. Logout");
        System.out.print("Choose option: ");

        int choice = getIntInput();
        if (choice == 0) {
            currentUser = null;
            System.out.println("✓ Logged out successfully");
            return;
        }

        if (currentUser.getRole() == UserRole.TEACHER) {
            handleTeacherMenu(choice);
        } else if (currentUser.getRole() == UserRole.STUDENT) {
            handleStudentMenu(choice);
        }
    }

    private void showTeacherMenu() {
        System.out.println("1. Create Course");
        System.out.println("2. Create Assignment");
        System.out.println("3. View My Assignments");
        System.out.println("4. Grade Submissions");
        System.out.println("5. View Course Statistics");
        System.out.println("6. View My Courses");
    }

    private void showStudentMenu() {
        System.out.println("1. View My Courses");
        System.out.println("2. View Assignments");
        System.out.println("3. Submit Assignment");
        System.out.println("4. View My Submissions");
        System.out.println("5. View Grades");
    }

    private void handleTeacherMenu(int choice) {
        Teacher teacher = (Teacher) currentUser;
        switch (choice) {
            case 1 -> createCourse(teacher);
            case 2 -> createAssignment(teacher);
            case 3 -> viewTeacherAssignments(teacher);
            case 4 -> gradeSubmissions(teacher);
            case 5 -> viewCourseStatistics(teacher);
            case 6 -> viewTeacherCourses(teacher);
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleStudentMenu(int choice) {
        Student student = (Student) currentUser;
        switch (choice) {
            case 1 -> viewStudentCourses(student);
            case 2 -> viewStudentAssignments(student);
            case 3 -> submitAssignment(student);
            case 4 -> viewStudentSubmissions(student);
            case 5 -> viewStudentGrades(student);
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    // Teacher functionality methods
    private void createCourse(Teacher teacher) {
        System.out.println("\n=== Create Course ===");
        System.out.print("Course Name: ");
        String courseName = scanner.nextLine();
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Credit Hours: ");
        int creditHours = getIntInput();
        System.out.print("Semester: ");
        String semester = scanner.nextLine();

        courseService.createCourse(courseName, courseCode, department, creditHours, semester, teacher);
    }

    private void createAssignment(Teacher teacher) {
        List<Course> teacherCourses = courseService.getCoursesByTeacher(teacher);
        if (teacherCourses.isEmpty()) {
            System.out.println("You don't have any courses. Please create a course first.");
            return;
        }

        System.out.println("\n=== Create Assignment ===");
        System.out.println("Select Course:");
        for (int i = 0; i < teacherCourses.size(); i++) {
            Course course = teacherCourses.get(i);
            System.out.println((i + 1) + ". " + course.getCourseName() + " (" + course.getCourseCode() + ")");
        }
        System.out.print("Choose course: ");
        int courseChoice = getIntInput() - 1;

        if (courseChoice < 0 || courseChoice >= teacherCourses.size()) {
            System.out.println("Invalid course selection.");
            return;
        }

        Course selectedCourse = teacherCourses.get(courseChoice);

        System.out.print("Assignment Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Maximum Marks: ");
        int maxMarks = getIntInput();

        System.out.println("Assignment Types:");
        AssignmentType[] types = AssignmentType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i].getDisplayName());
        }
        System.out.print("Choose type: ");
        int typeChoice = getIntInput() - 1;

        if (typeChoice < 0 || typeChoice >= types.length) {
            System.out.println("Invalid assignment type.");
            return;
        }

        AssignmentType selectedType = types[typeChoice];

        System.out.print("Due Date (yyyy-MM-dd HH:mm): ");
        String dueDateStr = scanner.nextLine();
        LocalDateTime dueDate;
        try {
            dueDate = LocalDateTime.parse(dueDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Assignment not created.");
            return;
        }

        assignmentService.createAssignment(title, description, selectedCourse, teacher,
                selectedType, maxMarks, dueDate);
    }

    private void viewTeacherAssignments(Teacher teacher) {
        List<Assignment> assignments = assignmentService.getAssignmentsByTeacher(teacher);
        if (assignments.isEmpty()) {
            System.out.println("You haven't created any assignments yet.");
            return;
        }

        System.out.println("\n=== Your Assignments ===");
        for (Assignment assignment : assignments) {
            assignment.displayAssignmentInfo();
            assignmentService.displayAssignmentStatistics(assignment);
            System.out.println("----------------------------");
        }
    }

    private void gradeSubmissions(Teacher teacher) {
        List<Assignment> assignments = assignmentService.getAssignmentsByTeacher(teacher);
        if (assignments.isEmpty()) {
            System.out.println("You don't have any assignments to grade.");
            return;
        }

        System.out.println("\n=== Grade Submissions ===");
        System.out.println("Select Assignment:");
        for (int i = 0; i < assignments.size(); i++) {
            Assignment assignment = assignments.get(i);
            int submissionCount = assignment.getSubmissionCount();
            System.out.println((i + 1) + ". " + assignment.getTitle() + " (" + submissionCount + " submissions)");
        }
        System.out.print("Choose assignment: ");
        int assignmentChoice = getIntInput() - 1;

        if (assignmentChoice < 0 || assignmentChoice >= assignments.size()) {
            System.out.println("Invalid assignment selection.");
            return;
        }

        Assignment selectedAssignment = assignments.get(assignmentChoice);
        List<Submission> submissions = assignmentService.getSubmissionsForAssignment(selectedAssignment);

        if (submissions.isEmpty()) {
            System.out.println("No submissions found for this assignment.");
            return;
        }

        System.out.println("\nSubmissions to grade:");
        for (int i = 0; i < submissions.size(); i++) {
            Submission submission = submissions.get(i);
            System.out.println((i + 1) + ". " + submission.getStudent().getName() +
                    " - Status: " + submission.getStatus());
        }

        System.out.print("Choose submission to grade: ");
        int submissionChoice = getIntInput() - 1;

        if (submissionChoice < 0 || submissionChoice >= submissions.size()) {
            System.out.println("Invalid submission selection.");
            return;
        }

        Submission selectedSubmission = submissions.get(submissionChoice);
        selectedSubmission.displaySubmissionInfo();

        System.out.print("Enter marks (0-" + selectedAssignment.getMaxMarks() + "): ");
        int marks = getIntInput();
        System.out.print("Enter feedback: ");
        String feedback = scanner.nextLine();

        try {
            assignmentService.gradeSubmission(selectedSubmission.getSubmissionId(), marks, feedback, teacher);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewCourseStatistics(Teacher teacher) {
        List<Course> courses = courseService.getCoursesByTeacher(teacher);
        if (courses.isEmpty()) {
            System.out.println("You don't have any courses.");
            return;
        }

        System.out.println("\n=== Course Statistics ===");
        for (Course course : courses) {
            courseService.displayCourseStatistics(course);
            System.out.println("----------------------------");
        }
    }

    private void viewTeacherCourses(Teacher teacher) {
        List<Course> courses = courseService.getCoursesByTeacher(teacher);
        if (courses.isEmpty()) {
            System.out.println("You don't have any courses.");
            return;
        }

        System.out.println("\n=== Your Courses ===");
        for (Course course : courses) {
            course.displayCourseInfo();
            System.out.println("----------------------------");
        }
    }

    // Student functionality methods
    private void viewStudentCourses(Student student) {
        List<Course> courses = courseService.getCoursesForStudent(student);
        if (courses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            return;
        }

        System.out.println("\n=== Your Courses ===");
        for (Course course : courses) {
            course.displayCourseInfo();
            System.out.println("----------------------------");
        }
    }

    private void viewStudentAssignments(Student student) {
        List<Course> courses = courseService.getCoursesForStudent(student);
        if (courses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            return;
        }

        System.out.println("\n=== Available Assignments ===");
        for (Course course : courses) {
            List<Assignment> assignments = assignmentService.getAssignmentsByCourse(course);
            if (!assignments.isEmpty()) {
                System.out.println("Course: " + course.getCourseName());
                for (Assignment assignment : assignments) {
                    assignment.displayAssignmentInfo();
                    System.out.println("----------------------------");
                }
            }
        }
    }

    private void submitAssignment(Student student) {
        List<Course> courses = courseService.getCoursesForStudent(student);
        if (courses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            return;
        }

        // Collect all available assignments
        List<Assignment> availableAssignments = courses.stream()
                .flatMap(course -> assignmentService.getAssignmentsByCourse(course).stream())
                .filter(assignment -> assignment.getStatus() == AssignmentStatus.ACTIVE)
                .toList();

        if (availableAssignments.isEmpty()) {
            System.out.println("No active assignments available for submission.");
            return;
        }

        System.out.println("\n=== Submit Assignment ===");
        System.out.println("Select Assignment:");
        for (int i = 0; i < availableAssignments.size(); i++) {
            Assignment assignment = availableAssignments.get(i);
            System.out.println((i + 1) + ". " + assignment.getTitle() +
                    " (" + assignment.getCourse().getCourseCode() + ") - Due: " + assignment.getDueDate());
        }
        System.out.print("Choose assignment: ");
        int assignmentChoice = getIntInput() - 1;

        if (assignmentChoice < 0 || assignmentChoice >= availableAssignments.size()) {
            System.out.println("Invalid assignment selection.");
            return;
        }

        Assignment selectedAssignment = availableAssignments.get(assignmentChoice);
        System.out.print("Enter your submission content: ");
        String content = scanner.nextLine();

        try {
            assignmentService.submitAssignment(selectedAssignment, student, content);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewStudentSubmissions(Student student) {
        List<Submission> submissions = assignmentService.getSubmissionsByStudent(student);
        if (submissions.isEmpty()) {
            System.out.println("You haven't submitted any assignments yet.");
            return;
        }

        System.out.println("\n=== Your Submissions ===");
        for (Submission submission : submissions) {
            submission.displaySubmissionInfo();
            System.out.println("----------------------------");
        }
    }

    private void viewStudentGrades(Student student) {
        List<Submission> submissions = assignmentService.getSubmissionsByStudent(student);
        List<Submission> gradedSubmissions = submissions.stream()
                .filter(submission -> submission.getStatus() == SubmissionStatus.GRADED)
                .toList();

        if (gradedSubmissions.isEmpty()) {
            System.out.println("You don't have any graded assignments yet.");
            return;
        }

        System.out.println("\n=== Your Grades ===");
        double totalMarks = 0;
        double totalPossible = 0;

        for (Submission submission : gradedSubmissions) {
            Assignment assignment = submission.getAssignment();
            System.out.println("Assignment: " + assignment.getTitle());
            System.out.println("Course: " + assignment.getCourse().getCourseName());
            System.out.println("Marks: " + submission.getMarks() + "/" + assignment.getMaxMarks());
            System.out.println("Percentage: " + String.format("%.1f%%",
                    (submission.getMarks() * 100.0) / assignment.getMaxMarks()));
            if (submission.getFeedback() != null) {
                System.out.println("Feedback: " + submission.getFeedback());
            }
            System.out.println("----------------------------");

            totalMarks += submission.getMarks();
            totalPossible += assignment.getMaxMarks();
        }

        if (totalPossible > 0) {
            System.out.println("Overall Performance: " + String.format("%.1f%%",
                    (totalMarks * 100.0) / totalPossible));
        }
    }

    private int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
