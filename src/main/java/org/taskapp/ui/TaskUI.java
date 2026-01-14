package org.taskapp.ui;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.taskapp.controller.TaskController;
import org.taskapp.model.Task;
import org.taskapp.service.AuthService;
import org.taskapp.service.TaskService;


public class TaskUI extends Application {
    private TaskService taskService;
    private AuthService authService;
    private TaskController taskController;
    private ListView<Task> taskListView;
    private String currentToken;

    @Override
    public void start(Stage primaryStage) {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
        authService = new AuthService();

        // Show login screen first
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage primaryStage) {
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Task Manager - Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            String token = authService.login(username, password);
            if (token != null) {
                currentToken = token;
                showMainScreen(primaryStage, username);
            } else {
                messageLabel.setText("Invalid credentials!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        loginBox.getChildren().addAll(
                titleLabel, usernameField, passwordField, loginButton, messageLabel
        );

        Scene scene = new Scene(loginBox, 400, 300);
        primaryStage.setTitle("Task Manager - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainScreen(Stage primaryStage, String username) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Title
        Label titleLabel = new Label("Task Manager - Welcome " + username);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.setTop(titleLabel);

        // Center: Task List
        taskListView = new ListView<>();

// Set the cell factory to display tasks nicely
        taskListView.setCellFactory(listView ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (empty || task == null) {
                            setText(null);
                        } else {
                            String status = task.isCompleted() ? "✓ " : "○ ";
                            String assignee = task.getAssignee() != null ? " (Assignee: " + task.getAssignee() + ")" : "";
                            setText("[" + task.getId() + "] " + status + task.getTitle() + assignee);
                        }
                    }
                }
        );

// Load tasks from controller
        loadTasks();
        root.setCenter(taskListView);


        // Bottom: Controls
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10, 0, 0, 0));

        TextField titleField = new TextField();
        titleField.setPromptText("Task title");
        titleField.setPrefWidth(200);

        TextField descField = new TextField();
        descField.setPromptText("Description");
        descField.setPrefWidth(200);

        Button addButton = new Button("Add Task");
        Button editButton = new Button("Edit Assignee");
        Button deleteButton = new Button("Delete Selected");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descField.getText();

            if (!title.isEmpty()) {
                Task task = taskController.createTask(title, description, username);
                if (task != null) {
                    titleField.clear();
                    descField.clear();
                    loadTasks();
                    showAlert("Success", "Task created successfully!");
                }
            } else {
                showAlert("Error", "Title cannot be empty!");
            }
        });

        editButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                TextInputDialog dialog = new TextInputDialog(selectedTask.getAssignee());
                dialog.setTitle("Edit Assignee");
                dialog.setHeaderText(null);
                dialog.setContentText("Assignee:");

                dialog.showAndWait().ifPresent(newAssignee -> {
                    if (!newAssignee.isEmpty()) {
                        taskController.updateTask(selectedTask, username, newAssignee);
                        loadTasks();
                        showAlert("Success", "Assignee updated successfully!");
                    }
                });
            } else {
                showAlert("Error", "No task selected!");
            }
        });

        deleteButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                if (taskController.deleteTask(selectedTask.getId())) {
                    loadTasks();
                    showAlert("Success", "Task deleted successfully!");
                }
            } else {
                showAlert("Error", "No task selected!");
            }
        });

        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    selectedTask.setCompleted(!selectedTask.isCompleted());
                    taskController.updateTask(selectedTask, username, selectedTask.getAssignee());
                    loadTasks();
                }
            }
        });



        refreshButton.setOnAction(e -> loadTasks());

        controls.getChildren().addAll(
                titleField, descField, addButton, editButton, deleteButton, refreshButton
        );
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
    }

    private void loadTasks() {
        Platform.runLater(() -> taskListView.getItems().setAll(taskController.getAllTasks()));
    }

    private int extractTaskId(String taskString) {
        String idStr = taskString.substring(
                taskString.indexOf("[") + 1,
                taskString.indexOf("]")
        );
        return Integer.parseInt(idStr);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}