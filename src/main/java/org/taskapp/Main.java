package org.taskapp;

import org.glassfish.tyrus.server.Server;
import org.taskapp.config.DatabaseConfig;
import org.taskapp.ui.TaskUI;
import org.taskapp.websocket.TaskWebSocketServer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Task Management System...");

        // Run database migrations
        DatabaseConfig.runLiquibase();

        // Start Websocket server
        startWebSocketServer();

        // Launch JavaFX UI
        TaskUI.main(args);
    }

    private static void startWebSocketServer(){
        new Thread(() -> {
            Server server = new Server("localhost", 8080, "/ws", null, TaskWebSocketServer.class);
            try{
                server.start();
                System.out.println("WebSocket server started on ws://localhost:8080/ws/tasks");
                Thread.currentThread().join();
            } catch (Exception e){
                System.err.println("Error starting WebSocket server: " + e.getMessage());
            }
        }).start();
    }
}
