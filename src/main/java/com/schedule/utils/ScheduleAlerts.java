package com.schedule.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

public class ScheduleAlerts {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ScheduleAlerts");

    private ScheduleAlerts() {
    }

    public static Optional<ButtonType> showRemoveConfirmationAlert(String removeElementName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        final String delete = RESOURCE_BUNDLE.getString("Delete");
        alert.setTitle(delete);
        alert.setHeaderText(delete + " \'" + removeElementName + "\' ?");
        return alert.showAndWait();
    }

    public static void showDialogError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setResizable(true);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
