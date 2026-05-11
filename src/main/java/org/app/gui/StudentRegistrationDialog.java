package org.app.gui;

import org.app.service.UserService;

import javax.swing.*;
import java.awt.*;

/**
 * Student Registration Dialog for BUP UCAM Assignment Tracker
 */
public class StudentRegistrationDialog extends JDialog {
    private UserService userService;
    private boolean success = false;

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField studentIdField;
    private JTextField programField;
    private JSpinner semesterSpinner;

    public StudentRegistrationDialog(JFrame parent, UserService userService) {
        super(parent, "Register as Student", true);
        this.userService = userService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setSize(400, 400);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        studentIdField = new JTextField(20);
        programField = new JTextField(20);
        semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 152, 0));
        JLabel titleLabel = new JLabel("Student Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Student ID
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(studentIdField, gbc);

        // Program
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Program:"), gbc);
        gbc.gridx = 1;
        formPanel.add(programField, gbc);

        // Semester
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        formPanel.add(semesterSpinner, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(255, 152, 0));
        registerButton.setForeground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);

        registerButton.addActionListener(e -> handleRegistration());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        // Enter key support for navigation
        nameField.addActionListener(e -> emailField.requestFocus());
        emailField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> studentIdField.requestFocus());
        studentIdField.addActionListener(e -> programField.requestFocus());
        programField.addActionListener(e -> semesterSpinner.requestFocus());
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String studentId = studentIdField.getText().trim();
        String program = programField.getText().trim();
        int semester = (Integer) semesterSpinner.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
            studentId.isEmpty() || program.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Registration Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userService.findUserByEmail(email) != null) {
            JOptionPane.showMessageDialog(this,
                "Email already exists. Please use a different email.",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            userService.registerStudent(name, email, password, studentId, program, semester);
            success = true;
            JOptionPane.showMessageDialog(this,
                "Student registration successful!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Registration failed: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
