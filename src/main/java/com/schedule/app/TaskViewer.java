package com.schedule.app;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;


public class TaskViewer extends GridPane {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.TaskViewer");

    private final Label eventExpiredLabel;

    private final Label nameLabel;

    private final TextField nameTextField;

    private final Label dateLabel;

    private final DatePicker datePicker;

    private final Label descriptionLabel;

    private final TextArea descriptionArea;

    private final Task task;

    private final TaskManager taskManager;

    public TaskViewer(Task task, TaskManager taskManager) {
        this.task = Objects.requireNonNull(task);
        this.taskManager = taskManager;
        setVgap(5);
        setHgap(5);
        setPrefSize(330, 200);

        eventExpiredLabel = new Label(LocalDate.now().isBefore(LocalDate.ofEpochDay(task.getEventDate())) ? "" : RESOURCE_BUNDLE.getString("EventIsExpired"));
        eventExpiredLabel.setTextFill(Color.RED);
        nameLabel = new Label(RESOURCE_BUNDLE.getString("Name"));
        nameTextField = TextFields.createClearableTextField();
        nameTextField.setText(task.getName());
        dateLabel = new Label("Date");
        datePicker = new DatePicker(LocalDate.ofEpochDay(task.getEventDate()));
        descriptionLabel = new Label("Description :");
        descriptionArea = new TextArea(task.getDescription());

        add(eventExpiredLabel, 1, 0, 1, 1);
        add(nameLabel, 0, 1);
        add(nameTextField, 1, 1);
        add(dateLabel, 0, 2);
        add(datePicker, 1, 2);
        add(descriptionLabel, 0, 3);
        add(descriptionArea, 0, 4, 2, 2);


    }

    public void registerTask() {
        task.setName(nameTextField.getText());
        task.setEventDate(datePicker.getValue().toEpochDay());
        task.setDescription(descriptionArea.getText());
        taskManager.updateTask(task);
    }

    public BooleanBinding okProperty() {
        return nameTextField.textProperty().isEmpty();
    }
}