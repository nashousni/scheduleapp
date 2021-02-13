package com.schedule.app;

import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXButton;
import com.schedule.storage.AppStorage;
import com.schedule.storage.StorageGenerator;
import com.schedule.utils.Glyph;
import com.schedule.utils.ScheduleAlerts;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import org.apache.commons.validator.GenericValidator;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.UUID;


public class SchedulePane extends BorderPane {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.SchedulePane");

    private final TaskManager taskManager;

    private final TextField nameTextField;

    private final DatePicker datePicker;

    private final CalendarView calendarView;

    private Button buttonRegister;

    private final VBox eventsBox = new VBox();

    private final JFXButton tableViewButton;

    private final JFXButton calendarViewButton;

    private final TableView<Task> tableView;

    private final ObservableList<Task> toDoList;

    private final MasterDetailPane mainPane;

    private final AppStorage storage;

    private final Font tableFont;

    SchedulePane() {

        storage = new StorageGenerator("mapdb").getDb();
        taskManager = TaskManager.getInstance(storage);
        toDoList = FXCollections.observableArrayList();
        calendarView = new CalendarView();
        calendarView.setPrefHeight(500);
        calendarView.showMonthPage();
        tableView = createTaskTableView();
        tableView.setPrefHeight(500);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().getSelectedItems().addListener(this::tableViewChangeListener);
        mainPane = new MasterDetailPane();
        mainPane.setMasterNode(calendarView);
        mainPane.setDetailNode(createScriptValidationOkNode());
        mainPane.setShowDetailNode(false);
        mainPane.setDetailSide(Side.BOTTOM);
        tableFont = Font.font("Helvetica", FontWeight.BOLD, 13);

        tableViewButton = new JFXButton(RESOURCE_BUNDLE.getString("TableView"), Glyph.createAwesomeFont('\uf093').color("white"));
        calendarViewButton = new JFXButton(RESOURCE_BUNDLE.getString("CalendarView"), Glyph.createAwesomeFont('\uf019').color("white"));
        calendarViewButton.setDisable(true);
        tableViewButton.setOnAction(event -> CreateConcurrentTask(() -> {
            if (mainPane.getMasterNode() != tableView) {
                Platform.runLater(() -> mainPane.setMasterNode(tableView));
                tableViewButton.setDisable(true);
                calendarViewButton.setDisable(false);
            }
        }));
        calendarViewButton.setOnAction(event -> CreateConcurrentTask(() -> {
            if (mainPane.getMasterNode() != calendarView) {
                Platform.runLater(() -> mainPane.setMasterNode(calendarView));
                calendarViewButton.setDisable(true);
                tableViewButton.setDisable(false);
            }
        }));

        EventHandler<KeyEvent> enterKeyEventHandler = event -> {
            if (KeyCode.ENTER.equals(event.getCode()) && buttonRegister.disableProperty().not().get()) {
                createTask();
            }
        };

        Text nameLabel = new Text("Event");
        nameTextField = TextFields.createClearableTextField();
        nameTextField.addEventHandler(KeyEvent.KEY_PRESSED, enterKeyEventHandler);

        Text dateLabel = new Text("Choose Date");
        datePicker = new DatePicker();
        datePicker.getEditor().textProperty().addListener((observable, oldText, newText) -> validateDateFormat(newText));

        buttonRegister = new Button("Register");
        buttonRegister.getStyleClass().add("button-register");
        buttonRegister.disableProperty().bind(nameTextField.textProperty().isEmpty().or(datePicker.getEditor().textProperty().isEmpty()));
        buttonRegister.setOnAction(event -> createTask());

        eventsBox.getChildren().addAll(mainPane);


        GridPane gridPane = new GridPane();

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);
        gridPane.add(calendarViewButton, 9, 0);
        gridPane.add(tableViewButton, 10, 0);
        gridPane.add(dateLabel, 0, 1);
        gridPane.add(datePicker, 1, 1);
        gridPane.add(buttonRegister, 2, 1);

        gridPane.setMinSize(850, 120);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_CENTER);

        setTop(gridPane);
        setCenter(eventsBox);

        storage.getTasks().forEach(value -> {
            taskManager.saveTask(value);
            refreshView();
        });
    }

    private void CreateConcurrentTask(Runnable runnable) {
        javafx.concurrent.Task task = new javafx.concurrent.Task() {
            @Override
            protected Integer call() throws Exception {
                getScene().setCursor(Cursor.WAIT);
                runnable.run();
                getScene().setCursor(Cursor.DEFAULT);
                return 0;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void createTask() {
        Task task = new Task(UUID.randomUUID().toString(), nameTextField.getText(), datePicker.getValue().toEpochDay());
        try {
            taskManager.addTask(task);
        } catch (TaskManagementException e) {
            ScheduleAlerts.showDialogError(e.getMessage());
        }
        refreshView();
    }

    private TableView<Task> createTaskTableView() {
        TableView<Task> taskTable = new TableView<>();

        TableColumn<Task, Task> nameColumn = new TableColumn<>(RESOURCE_BUNDLE.getString("EventName"));
        nameColumn.setCellValueFactory(param -> {
            Object data = null;
            if (param != null && param.getValue() != null) {
                data = param.getValue();
            }
            return new ReadOnlyObjectWrapper(data);
        });
        nameColumn.setCellFactory(param -> nameColumnCellFactory());
        nameColumn.setPrefWidth(590);

        TableColumn<Task, Long> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        dateColumn.setCellFactory(param -> dateColumnCellFactory());
        dateColumn.setPrefWidth(260);

        taskTable.getColumns().addAll(nameColumn, dateColumn);

        return taskTable;
    }

    private void tableViewChangeListener(ListChangeListener.Change<? extends Task> c) {
        if (c.getList().isEmpty()) {
            tableView.setContextMenu(null);
        } else if (c.getList().size() == 1) {
            Task task = c.getList().get(0);
            tableView.setContextMenu(createTaskContextMenu(task));
        } else {
            tableView.setContextMenu(createMultiTaskContextMenu(c.getList()));
        }
    }

    private ContextMenu createTaskContextMenu(Task task) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem();
        deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
        deleteMenuItem.setOnAction(event ->
                ScheduleAlerts.showRemoveConfirmationAlert(task.getName()).filter(btnType -> btnType.equals(ButtonType.OK)).ifPresent(okBtn -> {
                    taskManager.deleteTask(task);
                    refreshView();
                }));
        contextMenu.getItems().add(deleteMenuItem);
        return contextMenu;
    }

    private ContextMenu createMultiTaskContextMenu(ObservableList<? extends Task> tasks) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem();
        deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
        deleteMenuItem.setOnAction(event -> {
            tasks.forEach(taskManager::deleteTask);
            refreshView();
        });
        contextMenu.getItems().add(deleteMenuItem);
        return contextMenu;
    }

    private TableCell<Task, Task> nameColumnCellFactory() {
        return new TableCell<Task, Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setTextAlignment(TextAlignment.CENTER);
                    setText(task.getName());
                    setTextFill(LocalDate.ofEpochDay(task.getEventDate()).isBefore(LocalDate.now()) ? Color.RED : Color.BLACK);
                    setFont(tableFont);
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            TaskViewer taskViewer = new TaskViewer(task, taskManager);
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setTitle(task.getName());
                            ButtonType save = new ButtonType(RESOURCE_BUNDLE.getString("Save"), ButtonBar.ButtonData.YES);
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, save);
                            dialog.getDialogPane().lookupButton(save).disableProperty().bind(taskViewer.okProperty());
                            dialog.getDialogPane().setContent(taskViewer);
                            dialog.setResizable(true);
                            dialog.showAndWait().filter(buttonType -> buttonType.getButtonData() == ButtonBar.ButtonData.YES).ifPresent(saveButton -> {
                                taskViewer.registerTask();
                                updateItem(task, false);
                                refreshView();
                            });
                        }

                    });
                }
            }
        };
    }

    private TableCell<Task, Long> dateColumnCellFactory() {
        return new TableCell<Task, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    LocalDate localDate = LocalDate.ofEpochDay(item);
                    setTextAlignment(TextAlignment.CENTER);
                    setText(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yy")));
                    setTextFill(LocalDate.ofEpochDay(item).isBefore(LocalDate.now()) ? Color.RED : Color.BLACK);
                    setFont(tableFont);
                }
            }
        };
    }

    private void validateDateFormat(String value) {
        if (isValidFormat(value) || value.isEmpty()) {
            datePicker.getEditor().getStyleClass().remove("bad_date_format");
            Platform.runLater(() -> mainPane.setShowDetailNode(false));
        } else {
            datePicker.getEditor().getStyleClass().add("bad-date-format");
            mainPane.setShowDetailNode(true);
        }
    }

    private static boolean isValidFormat(String value) {
        return GenericValidator.isDate(value, "dd/MM/yyyy", true);
    }

    private Node createScriptValidationOkNode() {
        Text message = new Text(RESOURCE_BUNDLE.getString("WrongDateFormat") + " : dd/MM/yyyy");
        message.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        return createScriptValidationNode(message, 20, TextAlignment.CENTER, Color.web("#e85f5f"));
    }

    private Node createScriptValidationNode(Text message, int prefHeight, TextAlignment alignment, Color backgroundColor) {
        AnchorPane root = new AnchorPane();
        Background background = new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);

        TextFlow result = new TextFlow();
        result.setBackground(background);
        result.getChildren().add(message);
        result.setTextAlignment(alignment);

        root.getChildren().add(result);
        AnchorPane.setLeftAnchor(result, 0.0);
        AnchorPane.setTopAnchor(result, 0.0);
        AnchorPane.setRightAnchor(result, 10.0);
        AnchorPane.setBottomAnchor(result, 0.0);

        VBox box = new VBox();
        box.setPrefWidth(10.0);
        box.setAlignment(Pos.TOP_CENTER);
        Button closeButton = new Button();
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(event -> Platform.runLater(() -> mainPane.setShowDetailNode(false)));
        box.getChildren().add(closeButton);
        root.getChildren().add(box);
        AnchorPane.setTopAnchor(box, 0.0);
        AnchorPane.setRightAnchor(box, 0.0);

        root.setPrefHeight(prefHeight);

        return root;
    }

    private void refreshView() {
        toDoList.setAll(taskManager.getTasks());
        tableView.getItems().setAll(toDoList);
        tableView.refresh();
    }


}