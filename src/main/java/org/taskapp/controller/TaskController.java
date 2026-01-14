package org.taskapp.controller;

import org.taskapp.model.Task;
import org.taskapp.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task createTask(String title, String description, String username) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        return taskService.createTask(
                title,
                description,
                username,
                LocalDateTime.now()
        );
    }

    // Update task: completion, assignee, updatedBy, updatedDate
    public void updateTask(Task task, String updatedBy, String newAssignee) {
        task.setUpdatedBy(updatedBy);
        task.setUpdatedDate(LocalDateTime.now());
        if (newAssignee != null) {
            task.setAssignee(newAssignee);
        }
        taskService.updateTask(task);
    }

    public boolean deleteTask(int taskId) {
        return taskService.deleteTask(taskId);
    }

    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
}