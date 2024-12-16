package com.example.anti_cafe;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class for a table selector window that allows the user
 * to select a table to use and manages table statuses.
 */
public class TableSelectionWindow extends MainWindow {

    /**
     * Opens the table selection window and initializes the buttons for each table.
     */
    protected static void openTableSelectionWindow() {
        Stage takeTable = new Stage();
        FlowPane tables = new FlowPane(10, 10);
        tables.setOrientation(Orientation.VERTICAL);
        for (int i = 0; i < 10; i++) {
            Button selectTableButton = new Button("Стол №" + (i + 1));
            Label tableStatusLabel = new Label("Доступен");

            final int index = i;
            selectTableButton.setOnAction(e -> {
                try {
                    ensureTableIsAvailable(index);
                    logger.info("Выбран " + (index + 1) + " стол.");
                    startTableTimer(index);
                    occupyTable(index, tableStatusLabel);
                } catch (TableOccupiedException exception) {
                    displayErrorMessage(exception);
                    logger.warn("Попытка выбрать занятый стол №" + (index + 1));
                }
            });
            initializeButtonStatusUpdater(selectTableButton, tableStatusLabel, index);
            tables.getChildren().addAll(selectTableButton, tableStatusLabel);
        }
        Scene scene = new Scene(tables, 600, 450);
        takeTable.setScene(scene);
        takeTable.setTitle("Взять столик");
        takeTable.show();
    }

    /**
     * Applies a button style based on the table status.
     *
     * @param button the button to apply the style to
     * @param status the table status
     */
    private static void applyButtonStyleBasedOnStatus(Button button, TableStatus status) {
        if (status == TableStatus.AVAILABLE) {
            button.setStyle("-fx-base: #00FF7F;"); // Green for available
        } else {
            button.setStyle("-fx-base: #B22222;"); // Red for occupied
        }
    }

    /**
     * Sets the table status to busy and updates the corresponding label.
     *
     * @param index       index of the table to be occupied
     * @param statusLabel label for displaying the table status
     */
    static void occupyTable(int index, Label statusLabel) {
        logger.info("Установка статуса ЗАНЯТ для стола №" + (index + 1));
        tableStatuses[index] = MainWindow.TableStatus.OCCUPIED;
        statusLabel.setText("Занят");
    }

    /**
     * Initializes the table button status update using animation.
     *
     * @param tableButton table button
     * @param statusLabel table status label
     * @param index       table index
     */
    private static void initializeButtonStatusUpdater(Button tableButton, Label statusLabel, int index) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            applyButtonStyleBasedOnStatus(tableButton, tableStatuses[index]); // Initial style
            if (tableStatuses[index] == TableStatus.AVAILABLE) {
                statusLabel.setText("Доступен");
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Checks if the table is occupied. Throws an exception if the table is occupied.
     *
     * @param tableIndex index of the table to check
     * @throws TableOccupiedException if the table is occupied
     */
    private static void ensureTableIsAvailable(int tableIndex) throws TableOccupiedException {
        if (tableStatuses[tableIndex] == TableStatus.OCCUPIED) {
            throw new TableOccupiedException("Стол уже занят");
        }
    }

    /**
     * Displays an error message if the user attempts to occupy a
     * table that is already occupied.
     *
     * @param exception exception containing error information
     */
    private static void displayErrorMessage(TableOccupiedException exception) {
        Stage takeTable = new Stage();
        VBox tables = new VBox();
        Label error = new Label("Ошибка: " + exception.getMessage());
        Scene scene = new Scene(tables, 250, 50);
        tables.getChildren().addAll(error);
        takeTable.setScene(scene);
        takeTable.setTitle("Ошибка");
        takeTable.show();
    }
}

class TableOccupiedException extends Exception {

    /**
     * Constructor for TableOccupiedException.
     *
     * @param message message describing the cause of the exception
     */
    public TableOccupiedException(String message) {
        super(message);
    }

}

