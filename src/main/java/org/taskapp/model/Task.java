package org.taskapp.model;

public class Task {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedDate;
    private String assignee;
    private String creator;

    public Task(){}

    public Task(int id, String title, String description, boolean completed, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdBy = createdBy;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', completed=" + completed + "}";
    }
}
