package org.app.gui;

import org.app.model.*;
import org.app.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Teacher Dashboard GUI for BUP UCAM Assignment Tracker
 */
public class TeacherDashboard extends JPanel {
    private Teacher teacher;
    private UserService userService;
    private CourseService courseService;
    private AssignmentService assignmentService;
    private AssignmentTrackerGUI mainFrame;

    private JTabbedPane tabbedPane;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel assignmentsTableModel;
    private DefaultTableModel submissionsTableModel;

    public TeacherDashboard(Teacher teacher, UserService userService,
                           CourseService courseService, AssignmentService assignmentService,
                           AssignmentTrackerGUI mainFrame) {
        this.teacher = teacher;
        this.userService = userService;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
        this.mainFrame = mainFrame;

        initializeComponents();
        refreshData();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content with tabs
        tabbedPane = new JTabbedPane();

        // Dashboard tab
        tabbedPane.addTab("Dashboard", createDashboardPanel());

        // Courses tab
        tabbedPane.addTab("My Courses", createCoursesPanel());

        // Assignments tab
        tabbedPane.addTab("Assignments", createAssignmentsPanel());

        // Grading tab
        tabbedPane.addTab("Grading", createGradingPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + teacher.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(244, 67, 54));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.addActionListener(e -> mainFrame.showLogin());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(33, 150, 243));
        buttonPanel.add(logoutButton);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Statistics cards
        List<Course> teacherCourses = courseService.getCoursesByTeacher(teacher);
        List<Assignment> teacherAssignments = assignmentService.getAssignmentsByTeacher(teacher);

        // Courses card
        JPanel coursesCard = createStatCard("Courses", String.valueOf(teacherCourses.size()),
                new Color(76, 175, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1;
        panel.add(coursesCard, gbc);

        // Assignments card
        JPanel assignmentsCard = createStatCard("Assignments", String.valueOf(teacherAssignments.size()),
                new Color(33, 150, 243));
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(assignmentsCard, gbc);

        // Submissions card
        int totalSubmissions = teacherAssignments.stream()
                .mapToInt(Assignment::getSubmissionCount)
                .sum();
        JPanel submissionsCard = createStatCard("Total Submissions", String.valueOf(totalSubmissions),
                new Color(255, 152, 0));
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(submissionsCard, gbc);

        // Recent assignments
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBorder(BorderFactory.createTitledBorder("Recent Assignments"));
        recentPanel.setBackground(Color.WHITE);

        String[] recentColumns = {"Title", "Course", "Due Date", "Submissions"};
        DefaultTableModel recentModel = new DefaultTableModel(recentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable recentTable = new JTable(recentModel);
        recentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Populate recent assignments
        teacherAssignments.stream()
                .limit(5)
                .forEach(assignment -> {
                    recentModel.addRow(new Object[]{
                        assignment.getTitle(),
                        assignment.getCourse().getCourseCode(),
                        assignment.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        assignment.getSubmissionCount()
                    });
                });

        recentPanel.add(new JScrollPane(recentTable), BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3; gbc.weighty = 1;
        panel.add(recentPanel, gbc);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createRaisedBevelBorder());
        card.setPreferredSize(new Dimension(200, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createCourseButton = new JButton("Create New Course");
        createCourseButton.setBackground(new Color(76, 175, 80));
        createCourseButton.setForeground(Color.WHITE);
        createCourseButton.addActionListener(e -> showCreateCourseDialog());
        toolbar.add(createCourseButton);

        // Courses table
        String[] columns = {"Course Code", "Course Name", "Department", "Credit Hours", "Enrolled Students"};
        coursesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable coursesTable = new JTable(coursesTableModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(coursesTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createAssignmentButton = new JButton("Create New Assignment");
        createAssignmentButton.setBackground(new Color(33, 150, 243));
        createAssignmentButton.setForeground(Color.WHITE);
        createAssignmentButton.addActionListener(e -> showCreateAssignmentDialog());
        toolbar.add(createAssignmentButton);

        // Assignments table
        String[] columns = {"Title", "Course", "Type", "Due Date", "Max Marks", "Submissions", "Status"};
        assignmentsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable assignmentsTable = new JTable(assignmentsTableModel);
        assignmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(assignmentsTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGradingPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Submissions table
        String[] columns = {"Assignment", "Student", "Submitted At", "Status", "Marks", "Actions"};
        submissionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable submissionsTable = new JTable(submissionsTableModel);
        submissionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Grade button
        JButton gradeButton = new JButton("Grade Selected Submission");
        gradeButton.setBackground(new Color(255, 152, 0));
        gradeButton.setForeground(Color.WHITE);
        gradeButton.addActionListener(e -> {
            int selectedRow = submissionsTable.getSelectedRow();
            if (selectedRow >= 0) {
                showGradingDialog(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a submission to grade.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(gradeButton);

        panel.add(new JScrollPane(submissionsTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showCreateCourseDialog() {
        CreateCourseDialog dialog = new CreateCourseDialog(mainFrame, teacher, courseService);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            refreshData();
        }
    }

    private void showCreateAssignmentDialog() {
        List<Course> teacherCourses = courseService.getCoursesByTeacher(teacher);
        if (teacherCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please create a course first before creating assignments.",
                "No Courses", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CreateAssignmentDialog dialog = new CreateAssignmentDialog(mainFrame, teacher,
                teacherCourses, assignmentService);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            refreshData();
        }
    }

    private void showGradingDialog(int submissionIndex) {
        // Implementation for grading dialog
        JOptionPane.showMessageDialog(this, "Grading dialog will be implemented here.");
    }

    private void refreshData() {
        refreshCoursesTable();
        refreshAssignmentsTable();
        refreshSubmissionsTable();
    }

    private void refreshCoursesTable() {
        coursesTableModel.setRowCount(0);
        List<Course> courses = courseService.getCoursesByTeacher(teacher);

        for (Course course : courses) {
            coursesTableModel.addRow(new Object[]{
                course.getCourseCode(),
                course.getCourseName(),
                course.getDepartment(),
                course.getCreditHours(),
                course.getEnrolledStudents().size()
            });
        }
    }

    private void refreshAssignmentsTable() {
        assignmentsTableModel.setRowCount(0);
        List<Assignment> assignments = assignmentService.getAssignmentsByTeacher(teacher);

        for (Assignment assignment : assignments) {
            assignmentsTableModel.addRow(new Object[]{
                assignment.getTitle(),
                assignment.getCourse().getCourseCode(),
                assignment.getType().toString(),
                assignment.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                assignment.getMaxMarks(),
                assignment.getSubmissionCount(),
                assignment.getStatus().toString()
            });
        }
    }

    private void refreshSubmissionsTable() {
        submissionsTableModel.setRowCount(0);
        List<Assignment> assignments = assignmentService.getAssignmentsByTeacher(teacher);

        for (Assignment assignment : assignments) {
            List<Submission> submissions = assignmentService.getSubmissionsForAssignment(assignment);
            for (Submission submission : submissions) {
                submissionsTableModel.addRow(new Object[]{
                    assignment.getTitle(),
                    submission.getStudent().getName(),
                    submission.getSubmittedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                    submission.getStatus().toString(),
                    submission.getMarks() != null ? submission.getMarks() + "/" + assignment.getMaxMarks() : "Not Graded",
                    "Grade"
                });
            }
        }
    }
}
