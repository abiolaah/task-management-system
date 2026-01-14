package org.taskapp.repository;


import org.taskapp.config.DatabaseConfig;
import org.taskapp.model.Task;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    public List<Task> findAll() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Timestamp createdTs = rs.getTimestamp("created_date");
                Timestamp updatedTs = rs.getTimestamp("updated_date");

                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("completed"),
                        rs.getString("created_by"),
                        createdTs != null ? createdTs.toLocalDateTime() : null,
                        rs.getString("updated_by"),
                        updatedTs != null ? updatedTs.toLocalDateTime() : null,
                        rs.getString("assignee")
                );
                tasks.add(task);
            }
        }
        return tasks;
    }

    public Task save(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, completed, created_by, created_date) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, task.isCompleted());
            stmt.setString(4, task.getCreatedBy());
            stmt.setTimestamp(5, Timestamp.valueOf(task.getCreatedDate()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                }
            }
        }
        return task;
    }

    public void update(Task task) throws SQLException {
        String sql = """
            UPDATE tasks
            SET title = ?,
                description = ?,
                completed = ?,
                updated_by = ?,
                updated_date = ?
                assignee = ?
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isCompleted());
            pstmt.setString(4, task.getUpdatedBy());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(6, task.getAssignee());
            pstmt.setInt(7, task.getId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
