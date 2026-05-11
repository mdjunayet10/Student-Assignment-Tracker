package org.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.app.model.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Data persistence utility for BUP UCAM Assignment Tracker
 * Handles saving and loading application data to/from JSON files
 */
public class DataPersistence {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.json";
    private static final String COURSES_FILE = DATA_DIR + "/courses.json";
    private static final String ASSIGNMENTS_FILE = DATA_DIR + "/assignments.json";
    private static final String SUBMISSIONS_FILE = DATA_DIR + "/submissions.json";
    private static final String COUNTERS_FILE = DATA_DIR + "/counters.json";

    private ObjectMapper objectMapper;

    public DataPersistence() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Create data directory if it doesn't exist
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    /**
     * Save users data to JSON file
     */
    public void saveUsers(Map<String, User> users, Map<String, Teacher> teachers,
                         Map<String, Student> students, int userCounter) {
        try {
            UserData userData = new UserData();
            userData.users = users;
            userData.teachers = teachers;
            userData.students = students;
            userData.userCounter = userCounter;

            objectMapper.writeValue(new File(USERS_FILE), userData);
        } catch (IOException e) {
            System.err.println("Error saving users data: " + e.getMessage());
        }
    }

    /**
     * Load users data from JSON file
     */
    public UserData loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                System.out.println("Loading users from: " + file.getAbsolutePath());
                UserData userData = objectMapper.readValue(file, UserData.class);
                System.out.println("✓ Loaded " + userData.users.size() + " users successfully");
                return userData;
            } else {
                System.out.println("No existing users file found - starting fresh");
            }
        } catch (IOException e) {
            System.err.println("Error loading users data: " + e.getMessage());
            e.printStackTrace();
        }
        return new UserData(); // Return empty data if file doesn't exist or error occurs
    }

    /**
     * Save courses data to JSON file
     */
    public void saveCourses(Map<String, Course> courses, int courseCounter) {
        try {
            CourseData courseData = new CourseData();
            courseData.courses = courses;
            courseData.courseCounter = courseCounter;

            objectMapper.writeValue(new File(COURSES_FILE), courseData);
        } catch (IOException e) {
            System.err.println("Error saving courses data: " + e.getMessage());
        }
    }

    /**
     * Load courses data from JSON file
     */
    public CourseData loadCourses() {
        try {
            File file = new File(COURSES_FILE);
            if (file.exists()) {
                return objectMapper.readValue(file, CourseData.class);
            }
        } catch (IOException e) {
            System.err.println("Error loading courses data: " + e.getMessage());
        }
        return new CourseData();
    }

    /**
     * Save assignments data to JSON file
     */
    public void saveAssignments(Map<String, Assignment> assignments, Map<String, Submission> submissions,
                               int assignmentCounter, int submissionCounter) {
        try {
            AssignmentData assignmentData = new AssignmentData();
            assignmentData.assignments = assignments;
            assignmentData.submissions = submissions;
            assignmentData.assignmentCounter = assignmentCounter;
            assignmentData.submissionCounter = submissionCounter;

            objectMapper.writeValue(new File(ASSIGNMENTS_FILE), assignmentData);
        } catch (IOException e) {
            System.err.println("Error saving assignments data: " + e.getMessage());
        }
    }

    /**
     * Load assignments data from JSON file
     */
    public AssignmentData loadAssignments() {
        try {
            File file = new File(ASSIGNMENTS_FILE);
            if (file.exists()) {
                return objectMapper.readValue(file, AssignmentData.class);
            }
        } catch (IOException e) {
            System.err.println("Error loading assignments data: " + e.getMessage());
        }
        return new AssignmentData();
    }

    /**
     * Check if data files exist (indicates if this is first run)
     */
    public boolean dataExists() {
        return new File(USERS_FILE).exists();
    }

    /**
     * Data holder classes for JSON serialization
     */
    public static class UserData {
        public Map<String, User> users = new HashMap<>();
        public Map<String, Teacher> teachers = new HashMap<>();
        public Map<String, Student> students = new HashMap<>();
        public int userCounter = 1;
    }

    public static class CourseData {
        public Map<String, Course> courses = new HashMap<>();
        public int courseCounter = 1;
    }

    public static class AssignmentData {
        public Map<String, Assignment> assignments = new HashMap<>();
        public Map<String, Submission> submissions = new HashMap<>();
        public int assignmentCounter = 1;
        public int submissionCounter = 1;
    }
}
