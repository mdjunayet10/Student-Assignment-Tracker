package org.app.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.app.model.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simplified Data persistence utility for BUP UCAM Assignment Tracker
 */
public class SimpleDataPersistence {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users_simple.json";
    private static final String SETTINGS_FILE = DATA_DIR + "/settings.json";

    private ObjectMapper objectMapper;

    public SimpleDataPersistence() {
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
     * Save simple user data
     */
    public void saveUserCredentials(Map<String, SimpleUserData> userData) {
        try {
            objectMapper.writeValue(new File(USERS_FILE), userData);
            System.out.println("✓ Saved " + userData.size() + " user credentials");
        } catch (IOException e) {
            System.err.println("Error saving user credentials: " + e.getMessage());
        }
    }

    /**
     * Load simple user data
     */
    public Map<String, SimpleUserData> loadUserCredentials() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                TypeReference<Map<String, SimpleUserData>> typeRef = new TypeReference<Map<String, SimpleUserData>>() {};
                Map<String, SimpleUserData> userData = objectMapper.readValue(file, typeRef);
                System.out.println("✓ Loaded " + userData.size() + " user credentials");
                return userData;
            }
        } catch (IOException e) {
            System.err.println("Error loading user credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * Save application settings
     */
    public void saveSettings(AppSettings settings) {
        try {
            objectMapper.writeValue(new File(SETTINGS_FILE), settings);
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    /**
     * Load application settings
     */
    public AppSettings loadSettings() {
        try {
            File file = new File(SETTINGS_FILE);
            if (file.exists()) {
                return objectMapper.readValue(file, AppSettings.class);
            }
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
        }
        return new AppSettings();
    }

    public boolean dataExists() {
        return new File(USERS_FILE).exists();
    }

    /**
     * Simple user data structure for persistence
     */
    public static class SimpleUserData {
        public String userId;
        public String name;
        public String email;
        public String password;
        public String role; // "TEACHER" or "STUDENT"
        public String department; // for teachers
        public String employeeId; // for teachers
        public String studentId; // for students
        public String program; // for students
        public int semester; // for students
        public boolean isActive = true;

        public SimpleUserData() {}

        public SimpleUserData(User user) {
            this.userId = user.getUserId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.role = user.getRole().toString();
            this.isActive = user.isActive();

            if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                this.department = teacher.getDepartment();
                this.employeeId = teacher.getEmployeeId();
            } else if (user instanceof Student) {
                Student student = (Student) user;
                this.studentId = student.getStudentId();
                this.program = student.getProgram();
                this.semester = student.getSemester();
            }
        }

        public User toUser() {
            // Handle both uppercase and proper case role names
            String roleUpper = role != null ? role.toUpperCase() : "";

            if ("TEACHER".equals(roleUpper) || "Teacher".equals(role)) {
                if (department != null && employeeId != null) {
                    Teacher teacher = new Teacher(userId, name, email, password, department, employeeId);
                    teacher.setActive(isActive);
                    return teacher;
                } else {
                    System.err.println("Warning: Teacher data incomplete for " + name);
                }
            } else if ("STUDENT".equals(roleUpper) || "Student".equals(role)) {
                if (studentId != null && program != null) {
                    Student student = new Student(userId, name, email, password, studentId, program, semester);
                    student.setActive(isActive);
                    return student;
                } else {
                    System.err.println("Warning: Student data incomplete for " + name);
                }
            }
            System.err.println("Warning: Could not convert user data for " + name + " (role: " + role + ")");
            return null;
        }
    }

    /**
     * Application settings
     */
    public static class AppSettings {
        public int userCounter = 1;
        public int courseCounter = 1;
        public int assignmentCounter = 1;
        public int submissionCounter = 1;
        public boolean firstRun = true;

        public AppSettings() {}
    }
}
