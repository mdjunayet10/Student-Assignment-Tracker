package org.app.gui;

import com.formdev.flatlaf.FlatLightLaf;
import org.app.service.*;
import org.app.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main GUI Application for BUP UCAM Assignment Tracker
 */
public class AssignmentTrackerGUI extends JFrame {
    private UserService userService;
    private CourseService courseService;
    private AssignmentService assignmentService;
    private User currentUser;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Login components
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerTeacherButton;
    private JButton registerStudentButton;

    public AssignmentTrackerGUI() {
        // Initialize services
        userService = new UserService();
        courseService = new CourseService();
        assignmentService = new AssignmentService();

        // Initialize sample data
        initializeSampleData();

        // Setup GUI
        setupLookAndFeel();
        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Assignment Tracker");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupLookAndFeel() {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create login panel
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "LOGIN");

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Assignment Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Center login form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));

        JPanel loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setBackground(Color.WHITE);
        loginFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Login form title
        JLabel formTitle = new JLabel("Login to Your Account");
        formTitle.setFont(new Font("Arial", Font.BOLD, 20));
        formTitle.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginFormPanel.add(formTitle, gbc);

        // Email field
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        loginFormPanel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        loginFormPanel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 2;
        loginFormPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        loginFormPanel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        loginFormPanel.add(loginButton, gbc);

        // Register buttons
        JPanel registerPanel = new JPanel(new FlowLayout());
        registerPanel.setBackground(Color.WHITE);

        registerTeacherButton = new JButton("Register as Teacher");
        registerTeacherButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerTeacherButton.setBackground(new Color(76, 175, 80));
        registerTeacherButton.setForeground(Color.WHITE);

        registerStudentButton = new JButton("Register as Student");
        registerStudentButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerStudentButton.setBackground(new Color(255, 152, 0));
        registerStudentButton.setForeground(Color.WHITE);

        registerPanel.add(registerTeacherButton);
        registerPanel.add(registerStudentButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        loginFormPanel.add(registerPanel, gbc);

        centerPanel.add(loginFormPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void setupLayout() {
        // Layout is already set up in createLoginPanel
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(e -> handleLogin());
        registerTeacherButton.addActionListener(e -> showTeacherRegistration());
        registerStudentButton.addActionListener(e -> showStudentRegistration());

        // Enter key support for login
        emailField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both email and password.",
                "Login Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentUser = userService.authenticateUser(email, password);

        if (currentUser != null) {
            JOptionPane.showMessageDialog(this,
                "Welcome, " + currentUser.getName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);

            showDashboard();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid email or password. Please try again.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void showDashboard() {
        if (currentUser.getRole() == UserRole.TEACHER) {
            TeacherDashboard teacherDashboard = new TeacherDashboard(
                (Teacher) currentUser, userService, courseService, assignmentService, this);
            mainPanel.add(teacherDashboard, "TEACHER_DASHBOARD");
            cardLayout.show(mainPanel, "TEACHER_DASHBOARD");
        } else if (currentUser.getRole() == UserRole.STUDENT) {
            StudentDashboard studentDashboard = new StudentDashboard(
                (Student) currentUser, userService, courseService, assignmentService, this);
            mainPanel.add(studentDashboard, "STUDENT_DASHBOARD");
            cardLayout.show(mainPanel, "STUDENT_DASHBOARD");
        }
    }

    public void showLogin() {
        currentUser = null;
        emailField.setText("");
        passwordField.setText("");
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void showTeacherRegistration() {
        TeacherRegistrationDialog dialog = new TeacherRegistrationDialog(this, userService);
        dialog.setVisible(true);
    }

    private void showStudentRegistration() {
        StudentRegistrationDialog dialog = new StudentRegistrationDialog(this, userService);
        dialog.setVisible(true);
    }

    private void initializeSampleData() {
        // Only create sample data if this is the first run
        if (userService.isFirstRun()) {
            System.out.println("First run detected - creating sample data...");

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
        } else {
            System.out.println("✓ Existing data loaded successfully!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AssignmentTrackerGUI());
    }
}
