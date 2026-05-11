package org.app.gui;

import org.app.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Teacher Registration Dialog for BUP UCAM Assignment Tracker
 */
public class TeacherRegistrationDialog extends JDialog {
    private UserService userService;
    private boolean success = false;

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField departmentField;
    private JTextField employeeIdField;

    public TeacherRegistrationDialog(JFrame parent, UserService userService) {
        super(parent, "Register as Teacher", true);
        this.userService = userService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setSize(400, 350);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        departmentField = new JTextField(20);
        employeeIdField = new JTextField(20);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(76, 175, 80));
        JLabel titleLabel = new JLabel("Teacher Registration");
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

        // Department
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        formPanel.add(departmentField, gbc);

        // Employee ID
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(employeeIdField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(76, 175, 80));
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
        // Enter key support
        nameField.addActionListener(e -> emailField.requestFocus());
        emailField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> departmentField.requestFocus());
        departmentField.addActionListener(e -> employeeIdField.requestFocus());
        employeeIdField.addActionListener(e -> handleRegistration());
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String department = departmentField.getText().trim();
        String employeeId = employeeIdField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
            department.isEmpty() || employeeId.isEmpty()) {
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
            userService.registerTeacher(name, email, password, department, employeeId);
            success = true;
            JOptionPane.showMessageDialog(this,
                "Teacher registration successful!",
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
