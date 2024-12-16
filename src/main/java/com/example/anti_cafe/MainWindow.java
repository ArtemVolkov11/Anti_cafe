package com.example.anti_cafe;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class of the application, responsible for initializing
 * the interface and managing table statuses.
 */
public class MainWindow extends Application {

    public static Logger logger = LogManager.getLogger();

    protected static TableStatus[] tableStatuses = {TableStatus.AVAILABLE, TableStatus.AVAILABLE, TableStatus.AVAILABLE,
            TableStatus.AVAILABLE, TableStatus.AVAILABLE, TableStatus.AVAILABLE, TableStatus.AVAILABLE, TableStatus.AVAILABLE
            , TableStatus.AVAILABLE, TableStatus.AVAILABLE};
    protected static Timer[] tableTimers = new Timer[10];
    protected static int[] elapsedTimes = new int[10];

    /**
     * Launches the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        logger.info("Запуск приложения"); // Логируем запуск приложения
        launch(args);
    }

    /**
     * Starts a timer for the specified table and begins the time count.
     *
     * @param index the index of the table for which to start the timer
     */
    protected static void startTableTimer(int index) {
        logger.debug("Запуск таймера для стола №" + (index + 1));
        tableTimers[index] = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++elapsedTimes[index];
                logger.debug("Увеличение времени для стола №" + (index + 1) + ": " + elapsedTimes[index] + " минут(ы)");
            }
        };
        tableTimers[index].schedule(task, 0, 60000);
    }

    /**
     * Initializes the primary stage of the application with buttons
     * for table selection, closing tables, and viewing the archive.
     *
     * @param primaryStage the main stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        Button tableChoice = new Button("Выбрать столик");
        tableChoice.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                logger.info("Пользователь открыл окно выбора столов");
                TableSelectionWindow.openTableSelectionWindow();
            }
        });
        Button closeTable = new Button("Закрыть столик");
        closeTable.setOnAction(e -> {
            logger.info("Пользователь открыл окно закрытия столиков");
            new TableCloseWindow().openTableClosureWindow();
        });
        Button archive = new Button("Архив");
        archive.setOnAction(e -> {
            logger.info("Пользователь открыл архив");
            new ArchiveWindow().showArchiveWindow();
        });

        configureButtonStyles(tableChoice, closeTable, archive);

        GridPane root = initializeGridPane();
        root.add(tableChoice, 0, 0);
        root.add(closeTable, 1, 0);
        root.add(archive, 2, 0);

        primaryStage.setOnCloseRequest(event -> {
            logger.info("Закрытие приложения");
            for (Timer timer : tableTimers) {
                if (timer != null) {
                    timer.cancel();
                }
            }
        });

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Anfi");
        primaryStage.show();
    }

    /**
     * Sets the style for the specified buttons.
     *
     * @param buttons an array of buttons to style
     */
    private void configureButtonStyles(Button... buttons) {
        for (Button button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(Double.MAX_VALUE);
        }
    }

    /**
     * Initializes the GridPane for placing interface elements.
     *
     * @return the initialized GridPane object
     */
    private GridPane initializeGridPane() {
        GridPane root = new GridPane();

        for (int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(33.33);
            root.getColumnConstraints().add(columnConstraints);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(50);
        root.getRowConstraints().add(rowConstraints);
        return root;
    }

    protected enum TableStatus {AVAILABLE, OCCUPIED}
}
