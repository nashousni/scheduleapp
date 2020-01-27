package com.schedule.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScheduleApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new SchedulePane());
        scene.getStylesheets().add(getClass().getResource("/css/schedule.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Smart Schedule");
        stage.setResizable(false);
        stage.show();
    }
}
