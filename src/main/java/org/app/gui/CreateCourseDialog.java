package org.app.gui;

import org.app.model.Teacher;
import org.app.service.CourseService;

import javax.swing.*;
import java.awt.*;

/**
 * Create Course Dialog for Teachers
 */
public class CreateCourseDialog extends JDialog {
    private CourseService courseService;
    private Teacher teacher;
    private boolean success = false;

    private JTextField courseNameField;
    private JTextField courseCodeField;
    private JTextField departmentField;
    private JSpinner creditHoursSpinner;
    private JTextField semesterField;

    public CreateCourseDialog(JFrame parent, Teacher teacher, CourseService courseService) {
        super(parent, "Create New Course", true);
        this.teacher = teacher;
        this.courseService = courseService;

        initializeComponents();
        setupLayout();

        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        courseNameField = new JTextField(30);
        courseNameField.setPreferredSize(new Dimension(300, 25));
        courseCodeField = new JTextField(30);
        courseCodeField.setPreferredSize(new Dimension(300, 25));
        departmentField = new JTextField(30);
        departmentField.setPreferredSize(new Dimension(300, 25));
        departmentField.setText(teacher.getDepartment()); // Pre-fill with teacher's department
        creditHoursSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
        creditHoursSpinner.setPreferredSize(new Dimension(100, 25));
        semesterField = new JTextField(30);
        semesterField.setPreferredSize(new Dimension(300, 25));
        semesterField.setText("Fall 2024"); // Default semester
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        JLabel titleLabel = new JLabel("Create New Course");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Course Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseNameField, gbc);

        // Course Code
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseCodeField, gbc);

        // Department
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        formPanel.add(departmentField, gbc);

        // Credit Hours
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Credit Hours:"), gbc);
        gbc.gridx = 1;
        formPanel.add(creditHoursSpinner, gbc);

        // Semester
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        formPanel.add(semesterField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create Course");
        createButton.setBackground(new Color(33, 150, 243));
        createButton.setForeground(Color.WHITE);
        createButton.setPreferredSize(new Dimension(120, 35));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(80, 35));

        createButton.addActionListener(e -> handleCourseCreation());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleCourseCreation() {
        String courseName = courseNameField.getText().trim();
        String courseCode = courseCodeField.getText().trim();
        String department = departmentField.getText().trim();
        int creditHours = (Integer) creditHoursSpinner.getValue();
        String semester = semesterField.getText().trim();

        if (courseName.isEmpty() || courseCode.isEmpty() || department.isEmpty() || semester.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Course Creation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if course code already exists
        if (courseService.findCourseByCode(courseCode) != null) {
            JOptionPane.showMessageDialog(this,
                "Course code already exists. Please use a different code.",
                "Course Creation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            courseService.createCourse(courseName, courseCode, department, creditHours, semester, teacher);
            success = true;
            JOptionPane.showMessageDialog(this,
                "Course created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Course creation failed: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
