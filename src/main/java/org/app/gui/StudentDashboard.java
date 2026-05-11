package org.app.gui;

import org.app.model.*;
import org.app.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Student Dashboard GUI for BUP UCAM Assignment Tracker
 */
public class StudentDashboard extends JPanel {
    private Student student;
    private UserService userService;
    private CourseService courseService;
    private AssignmentService assignmentService;
    private AssignmentTrackerGUI mainFrame;

    private JTabbedPane tabbedPane;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel assignmentsTableModel;
    private DefaultTableModel submissionsTableModel;
    private DefaultTableModel gradesTableModel;

    public StudentDashboard(Student student, UserService userService,
                           CourseService courseService, AssignmentService assignmentService,
                           AssignmentTrackerGUI mainFrame) {
        this.student = student;
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

        // Submissions tab
        tabbedPane.addTab("My Submissions", createSubmissionsPanel());

        // Grades tab
        tabbedPane.addTab("Grades", createGradesPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + " (" + student.getStudentId() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Student info
        JLabel infoLabel = new JLabel("Program: " + student.getProgram() + " | Semester: " + student.getSemester());
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(Color.WHITE);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(76, 175, 80));
        leftPanel.add(welcomeLabel, BorderLayout.CENTER);
        leftPanel.add(infoLabel, BorderLayout.SOUTH);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(244, 67, 54));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.addActionListener(e -> mainFrame.showLogin());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(76, 175, 80));
        buttonPanel.add(logoutButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
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
        List<Course> studentCourses = courseService.getCoursesForStudent(student);
        List<Submission> studentSubmissions = assignmentService.getSubmissionsByStudent(student);

        // Get all assignments for enrolled courses
        int totalAssignments = studentCourses.stream()
                .mapToInt(course -> assignmentService.getAssignmentsByCourse(course).size())
                .sum();

        // Count graded submissions
        long gradedSubmissions = studentSubmissions.stream()
                .filter(submission -> submission.getStatus() == SubmissionStatus.GRADED)
                .count();

        // Courses card
        JPanel coursesCard = createStatCard("Enrolled Courses", String.valueOf(studentCourses.size()),
                new Color(33, 150, 243));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1;
        panel.add(coursesCard, gbc);

        // Assignments card
        JPanel assignmentsCard = createStatCard("Total Assignments", String.valueOf(totalAssignments),
                new Color(255, 152, 0));
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(assignmentsCard, gbc);

        // Submissions card
        JPanel submissionsCard = createStatCard("My Submissions", String.valueOf(studentSubmissions.size()),
                new Color(76, 175, 80));
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(submissionsCard, gbc);

        // Graded card
        JPanel gradedCard = createStatCard("Graded", String.valueOf(gradedSubmissions),
                new Color(156, 39, 176));
        gbc.gridx = 3; gbc.gridy = 0;
        panel.add(gradedCard, gbc);

        // Upcoming assignments
        JPanel upcomingPanel = new JPanel(new BorderLayout());
        upcomingPanel.setBorder(BorderFactory.createTitledBorder("Upcoming Assignments"));
        upcomingPanel.setBackground(Color.WHITE);

        String[] upcomingColumns = {"Assignment", "Course", "Due Date", "Status"};
        DefaultTableModel upcomingModel = new DefaultTableModel(upcomingColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable upcomingTable = new JTable(upcomingModel);
        upcomingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Populate upcoming assignments
        studentCourses.forEach(course -> {
            assignmentService.getAssignmentsByCourse(course).stream()
                    .filter(assignment -> assignment.getStatus() == AssignmentStatus.ACTIVE)
                    .limit(5)
                    .forEach(assignment -> {
                        boolean hasSubmitted = studentSubmissions.stream()
                                .anyMatch(sub -> sub.getAssignment().equals(assignment));

                        upcomingModel.addRow(new Object[]{
                            assignment.getTitle(),
                            assignment.getCourse().getCourseCode(),
                            assignment.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                            hasSubmitted ? "Submitted" : "Pending"
                        });
                    });
        });

        upcomingPanel.add(new JScrollPane(upcomingTable), BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4; gbc.weighty = 1;
        panel.add(upcomingPanel, gbc);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createRaisedBevelBorder());
        card.setPreferredSize(new Dimension(150, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Courses table
        String[] columns = {"Course Code", "Course Name", "Instructor", "Credit Hours", "Department"};
        coursesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable coursesTable = new JTable(coursesTableModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(coursesTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton submitButton = new JButton("Submit Assignment");
        submitButton.setBackground(new Color(76, 175, 80));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> showSubmitAssignmentDialog());
        toolbar.add(submitButton);

        // Assignments table
        String[] columns = {"Title", "Course", "Type", "Due Date", "Max Marks", "Status"};
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

    private JPanel createSubmissionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Submissions table
        String[] columns = {"Assignment", "Course", "Submitted At", "Status", "Late"};
        submissionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable submissionsTable = new JTable(submissionsTableModel);
        submissionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(submissionsTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Grades table
        String[] columns = {"Assignment", "Course", "Marks", "Max Marks", "Percentage", "Feedback"};
        gradesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable gradesTable = new JTable(gradesTableModel);
        gradesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Performance summary
        JPanel summaryPanel = new JPanel(new FlowLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Performance Summary"));

        List<Submission> gradedSubmissions = assignmentService.getSubmissionsByStudent(student).stream()
                .filter(submission -> submission.getStatus() == SubmissionStatus.GRADED)
                .toList();

        if (!gradedSubmissions.isEmpty()) {
            double totalMarks = gradedSubmissions.stream()
                    .mapToInt(Submission::getMarks)
                    .sum();
            double totalPossible = gradedSubmissions.stream()
                    .mapToInt(submission -> submission.getAssignment().getMaxMarks())
                    .sum();
            double overallPercentage = (totalMarks / totalPossible) * 100;

            JLabel overallLabel = new JLabel("Overall Performance: " +
                    String.format("%.1f%% (%.0f/%.0f)", overallPercentage, totalMarks, totalPossible));
            overallLabel.setFont(new Font("Arial", Font.BOLD, 16));
            summaryPanel.add(overallLabel);
        } else {
            summaryPanel.add(new JLabel("No graded assignments yet."));
        }

        panel.add(summaryPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(gradesTable), BorderLayout.CENTER);

        return panel;
    }

    private void showSubmitAssignmentDialog() {
        List<Course> studentCourses = courseService.getCoursesForStudent(student);
        if (studentCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "You are not enrolled in any courses.",
                "No Courses", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SubmitAssignmentDialog dialog = new SubmitAssignmentDialog(mainFrame, student,
                studentCourses, assignmentService);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            refreshData();
        }
    }

    private void refreshData() {
        refreshCoursesTable();
        refreshAssignmentsTable();
        refreshSubmissionsTable();
        refreshGradesTable();
    }

    private void refreshCoursesTable() {
        coursesTableModel.setRowCount(0);
        List<Course> courses = courseService.getCoursesForStudent(student);

        for (Course course : courses) {
            coursesTableModel.addRow(new Object[]{
                course.getCourseCode(),
                course.getCourseName(),
                course.getInstructor().getName(),
                course.getCreditHours(),
                course.getDepartment()
            });
        }
    }

    private void refreshAssignmentsTable() {
        assignmentsTableModel.setRowCount(0);
        List<Course> courses = courseService.getCoursesForStudent(student);
        List<Submission> studentSubmissions = assignmentService.getSubmissionsByStudent(student);

        for (Course course : courses) {
            List<Assignment> assignments = assignmentService.getAssignmentsByCourse(course);
            for (Assignment assignment : assignments) {
                boolean hasSubmitted = studentSubmissions.stream()
                        .anyMatch(sub -> sub.getAssignment().equals(assignment));

                assignmentsTableModel.addRow(new Object[]{
                    assignment.getTitle(),
                    assignment.getCourse().getCourseCode(),
                    assignment.getType().toString(),
                    assignment.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                    assignment.getMaxMarks(),
                    hasSubmitted ? "Submitted" : "Pending"
                });
            }
        }
    }

    private void refreshSubmissionsTable() {
        submissionsTableModel.setRowCount(0);
        List<Submission> submissions = assignmentService.getSubmissionsByStudent(student);

        for (Submission submission : submissions) {
            submissionsTableModel.addRow(new Object[]{
                submission.getAssignment().getTitle(),
                submission.getAssignment().getCourse().getCourseCode(),
                submission.getSubmittedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                submission.getStatus().toString(),
                submission.isLateSubmission() ? "Yes" : "No"
            });
        }
    }

    private void refreshGradesTable() {
        gradesTableModel.setRowCount(0);
        List<Submission> submissions = assignmentService.getSubmissionsByStudent(student);

        for (Submission submission : submissions) {
            if (submission.getStatus() == SubmissionStatus.GRADED) {
                Assignment assignment = submission.getAssignment();
                double percentage = (submission.getMarks() * 100.0) / assignment.getMaxMarks();

                gradesTableModel.addRow(new Object[]{
                    assignment.getTitle(),
                    assignment.getCourse().getCourseCode(),
                    submission.getMarks(),
                    assignment.getMaxMarks(),
                    String.format("%.1f%%", percentage),
                    submission.getFeedback() != null ? submission.getFeedback() : "No feedback"
                });
            }
        }
    }
}
