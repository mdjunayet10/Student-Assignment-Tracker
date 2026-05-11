package org.app.model;

/**
 * User roles in BUP UCAM Assignment Tracker
 */
public enum UserRole {
    TEACHER("Teacher"),
    STUDENT("Student"),
    ADMIN("Administrator");

    private final String displayName;

    UserRole(String displayName) {
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
