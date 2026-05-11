package org.app.model;

/**
 * Assignment status in BUP UCAM Assignment Tracker
 */
public enum AssignmentStatus {
    DRAFT("Draft"),
    ACTIVE("Active"),
    CLOSED("Closed"),
    GRADED("Graded"),
    ARCHIVED("Archived");

    private final String displayName;

    AssignmentStatus(String displayName) {
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
