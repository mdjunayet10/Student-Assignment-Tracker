package org.app.gui;

import org.app.model.*;
import org.app.service.AssignmentService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Create Assignment Dialog for Teachers
 */
public class CreateAssignmentDialog extends JDialog {
    private AssignmentService assignmentService;
    private Teacher teacher;
    private List<Course> courses;
    private boolean success = false;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Course> courseComboBox;
    private JComboBox<AssignmentType> typeComboBox;
    private JSpinner maxMarksSpinner;
    private JPanel dateTimePanel;
    private JSpinner dateSpinner;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;

    public CreateAssignmentDialog(JFrame parent, Teacher teacher, List<Course> courses,
                                 AssignmentService assignmentService) {
        super(parent, "Create New Assignment", true);
        this.teacher = teacher;
        this.courses = courses;
        this.assignmentService = assignmentService;

        initializeComponents();
        setupLayout();

        setSize(500, 450);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        titleField = new JTextField(40);
        titleField.setPreferredSize(new Dimension(400, 30));
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));

        descriptionArea = new JTextArea(6, 40);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));

        courseComboBox = new JComboBox<>(courses.toArray(new Course[0]));
        courseComboBox.setPreferredSize(new Dimension(400, 30));
        courseComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Course) {
                    Course course = (Course) value;
                    setText(course.getCourseCode() + " - " + course.getCourseName());
                }
                return this;
            }
        });

        typeComboBox = new JComboBox<>(AssignmentType.values());
        typeComboBox.setPreferredSize(new Dimension(400, 30));
        maxMarksSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 5));
        maxMarksSpinner.setPreferredSize(new Dimension(120, 30));

        // Create separate date and time selectors
        createDateTimeSelector();
    }

    private void createDateTimeSelector() {
        dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dateTimePanel.setPreferredSize(new Dimension(400, 35));

        // Date picker using JSpinner with date model
        LocalDateTime defaultDueDate = LocalDateTime.now().plusDays(7);

        // Date spinner
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateModel.setValue(java.sql.Timestamp.valueOf(defaultDueDate));
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(130, 30));

        // Hour spinner (0-23)
        hourSpinner = new JSpinner(new SpinnerNumberModel(defaultDueDate.getHour(), 0, 23, 1));
        hourSpinner.setPreferredSize(new Dimension(60, 30));

        // Minute spinner (0-59)
        minuteSpinner = new JSpinner(new SpinnerNumberModel(defaultDueDate.getMinute(), 0, 59, 1));
        minuteSpinner.setPreferredSize(new Dimension(60, 30));

        dateTimePanel.add(new JLabel("Date:"));
        dateTimePanel.add(dateSpinner);
        dateTimePanel.add(new JLabel("Hour:"));
        dateTimePanel.add(hourSpinner);
        dateTimePanel.add(new JLabel("Min:"));
        dateTimePanel.add(minuteSpinner);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        JLabel titleLabel = new JLabel("Create New Assignment");
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

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Assignment Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        // Course
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseComboBox, gbc);

        // Type
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Assignment Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeComboBox, gbc);

        // Max Marks
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Maximum Marks:"), gbc);
        gbc.gridx = 1;
        formPanel.add(maxMarksSpinner, gbc);

        // Due Date
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Due Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateTimePanel, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create Assignment");
        createButton.setBackground(new Color(33, 150, 243));
        createButton.setForeground(Color.WHITE);
        createButton.setPreferredSize(new Dimension(150, 35));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(80, 35));

        createButton.addActionListener(e -> handleAssignmentCreation());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleAssignmentCreation() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        AssignmentType selectedType = (AssignmentType) typeComboBox.getSelectedItem();
        int maxMarks = (Integer) maxMarksSpinner.getValue();

        LocalDateTime dueDate;
        try {
            // Get date, hour, and minute values from spinners
            java.util.Date dateValue = (java.util.Date) dateSpinner.getValue();
            int hourValue = (Integer) hourSpinner.getValue();
            int minuteValue = (Integer) minuteSpinner.getValue();

            // Combine date, hour, and minute into a single LocalDateTime object
            dueDate = LocalDateTime.ofInstant(dateValue.toInstant(), java.time.ZoneId.systemDefault())
                    .withHour(hourValue).withMinute(minuteValue);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date/time selection.",
                "Date/Time Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Assignment Creation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dueDate.isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(this,
                "Due date cannot be in the past.",
                "Invalid Due Date",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            assignmentService.createAssignment(title, description, selectedCourse, teacher,
                    selectedType, maxMarks, dueDate);
            success = true;
            JOptionPane.showMessageDialog(this,
                "Assignment created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Assignment creation failed: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
