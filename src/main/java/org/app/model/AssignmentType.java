package org.app.model;

/**
 * Assignment types in BUP UCAM Assignment Tracker
 */
public enum AssignmentType {
    HOMEWORK("Homework"),
    PROJECT("Project"),
    LAB("Lab Assignment"),
    QUIZ("Quiz"),
    EXAM("Exam"),
    PRESENTATION("Presentation"),
    RESEARCH("Research Paper"),
    CASE_STUDY("Case Study");

    private final String displayName;

    AssignmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
