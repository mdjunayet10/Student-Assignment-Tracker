package org.app.model;

/**
 * Submission status in BUP UCAM Assignment Tracker
 */
public enum SubmissionStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under Review"),
    GRADED("Graded"),
    RETURNED("Returned for Revision");

    private final String displayName;

    SubmissionStatus(String displayName) {
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
