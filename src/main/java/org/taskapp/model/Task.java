package org.taskapp.model;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;
    private String assignee;

    public Task(){}

    // ✅ Constructor for creating a new task
    public Task(String title, String description,boolean completed, String createdBy, LocalDateTime createdDate) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.completed = false;
    }

    public Task(int id, String title, String description,boolean completed, String createdBy, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.completed = false;
    }

    // ✅ Constructor for loading from DB
    public Task(int id, String title, String description, boolean completed,
                String createdBy, LocalDateTime createdDate,
                String updatedBy, LocalDateTime updatedDate,
                String assignee) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.assignee = assignee;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    @Override
    public String toString() {
        return title + (completed ? " ✔" : "");
    }
}
