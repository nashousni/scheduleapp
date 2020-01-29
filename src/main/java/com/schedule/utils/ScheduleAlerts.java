package com.schedule.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ScheduleAlerts {

    private ScheduleAlerts() {

    }

    public static Optional<ButtonType> showRemoveConfirmationAlert(String removeElementName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        final String delete = "Delete";
        alert.setTitle(delete);
        alert.setHeaderText(delete + " \'" + removeElementName + "\' ?");
        return alert.showAndWait();
    }
}
