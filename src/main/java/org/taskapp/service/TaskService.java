package org.taskapp.service;

import org.taskapp.model.Task;
import org.taskapp.repository.TaskRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService() {
        this.taskRepository = new TaskRepository();
    }

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        try {
            return taskRepository.findAll();
        } catch (SQLException e) {
            System.err.println("Error fetching tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Task createTask(String title, String description, String createdBy, LocalDateTime createdDate) {
        Task task = new Task(0, title, description, false, createdBy, createdDate);
        try {
            return taskRepository.save(task);
        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
            return null;
        }
    }

    public boolean updateTask(Task task) {
        try {
            taskRepository.update(task);
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTask(int id) {
        try {
            taskRepository.delete(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
            return false;
        }
    }
}

