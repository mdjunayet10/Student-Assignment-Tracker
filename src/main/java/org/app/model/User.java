package org.app.model;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base User class for BUP UCAM Assignment Tracker
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Teacher.class, name = "teacher"),
    @JsonSubTypes.Type(value = Student.class, name = "student")
})
public abstract class User {
    @JsonProperty("userId")
    protected String userId;
    @JsonProperty("name")
    protected String name;
    @JsonProperty("email")
    protected String email;
    @JsonProperty("password")
    protected String password;
    @JsonProperty("role")
    protected UserRole role;
    @JsonProperty("createdAt")
    protected LocalDateTime createdAt;
    @JsonProperty("isActive")
    protected boolean isActive;

    // Default constructor for Jackson
    protected User() {}

    public User(String userId, String name, String email, String password, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // Abstract methods
    public abstract void displayDashboard();
    public abstract String getDisplayName();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s', email='%s', role=%s}",
                getClass().getSimpleName(), userId, name, email, role);
    }
}
