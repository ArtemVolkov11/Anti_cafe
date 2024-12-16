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

import java.util.Arrays;

/**
 * Class for the table close window, allowing users to release tables
 * and manage their status and usage statistics.
 */
public class TableCloseWindow extends MainWindow implements ActionForButtonCloseTable {

    protected static double[] tablePopularityCount = new double[10];
    protected static int[] mostProfitableTables = new int[10];
    protected static int mostPopularTableIndex = 0;
    protected static double[] totalUsageTimes = new double[10];
    protected static int mostProfitableTableIndex = 0;
    protected static int maxProfitValue = 0;
    protected static double maxPopularityValue = 0;
    protected static int totalProfit = 0;

    /**
     * Checks if the table is occupied. If the table is free, throws an exception.
     *
     * @param tableIndex index of the table to check
     * @throws TableOccupiedException if the table is free
     */
    private static void ensureTableIsOccupied(int tableIndex) throws TableOccupiedException {
        if (tableStatuses[tableIndex] == TableStatus.AVAILABLE) {
            throw new TableOccupiedException("Стол уже свободен");
        }
    }

    /**
     * Displays an error message if the user attempts to
     * interact with an empty table.
     *
     * @param exception an exception containing error information
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

    /**
     * Opens the table close window and initializes the buttons for each table.
     */
    @Override
    public void openTableClosureWindow() {
        Stage takeTable = new Stage();
        FlowPane tables = new FlowPane(10, 10);
        tables.setOrientation(Orientation.VERTICAL);
        for (int i = 0; i < 10; i++) {
            Button tableButton = new Button("Стол №" + (i + 1));
            Label statusLabel = new Label("Доступен");
            Label reservedTimeLabel = new Label("Время: " + elapsedTimes[i] + " мин");
            Label tablePriceLabel = new Label("Цена: " + elapsedTimes[i] * 3 + " р");

            final int index = i;
            tableButton.setOnAction(e -> {
                try {
                    ensureTableIsOccupied(index);
                    logger.info("Закрытие стола №" + (index + 1) + "...");
                    setTableStatusToAvailable(index, statusLabel);
                    showClosureSummary(index);
                    calculateTotalProfit(index);
                    accumulateTableUsageTime(index);
                    updateMostProfitableTable(index);
                    updateTablePopularity(index);
                    cancelTableTimer(index);
                } catch (TableOccupiedException exception) {
                    displayErrorMessage(exception);
                    logger.warn("Попытка закрыть свободный стол №" + (index + 1));
                }
            });
            refreshTableButtonStatus(tableButton, statusLabel, reservedTimeLabel, tablePriceLabel, index);
            tables.getChildren().addAll(tableButton, statusLabel, reservedTimeLabel, tablePriceLabel);
        }

        Label profit = new Label("Текушая выручка: " + Arrays.stream(elapsedTimes).sum() * 3 + " р");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            profit.setText("Текушая выручка: " + Arrays.stream(elapsedTimes).sum() * 3 + " р");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        tables.getChildren().add(profit);
        Scene scene = new Scene(tables, 600, 600);
        takeTable.setScene(scene);
        takeTable.setTitle("Закрыть столик");
        takeTable.show();
    }

    /**
     * Sets the table status to available and updates the corresponding label.
     *
     * @param index       index of the table to be released
     * @param statusLabel label to display the table status
     */
    @Override
    public void setTableStatusToAvailable(int index, Label statusLabel) {
        logger.info("Установка статуса ДОСТУПЕН для стола №" + (index + 1));
        tableStatuses[index] = TableStatus.AVAILABLE;
        statusLabel.setText("Доступен");
    }

    /**
     * Applies a button style based on the table status.
     *
     * @param button the button to apply the style to
     * @param status the table status
     */
    @Override
    public void applyButtonStyleBasedOnStatus(Button button, TableStatus status) {
        if (status == TableStatus.AVAILABLE) {
            button.setStyle("-fx-base: #00FF7F;"); // Green for available
        } else {
            button.setStyle("-fx-base: #B22222;"); // Red for occupied
        }
    }

    /**
     * Stops the timer for the specified table and resets its time.
     *
     * @param index index of the table for which to stop the timer
     */
    @Override
    public void cancelTableTimer(int index) {
        logger.info("Выключение таймера для стола №" + (index + 1));
        tableTimers[index].cancel(); // Остановка таймера
        tableTimers[index] = null; // Сброс ссылки на таймер
        elapsedTimes[index] = 0;
    }

    /**
     * Displays a closing summary for the specified table, including information about its
     * usage time and price.
     *
     * @param index the index of the table for which to display the summary
     */
    @Override
    public void showClosureSummary(int index) {
        logger.info("Отображение сводки закрытия для стола №" + (index + 1));
        Stage takeTable = new Stage();
        VBox tables = new VBox();
        Label table = new Label("Стол №" + (index + 1));
        Label price = new Label("Цена: " + elapsedTimes[index] * 3 + " р");
        Label time = new Label("Время: " + elapsedTimes[index] + " мин");
        Scene scene = new Scene(tables, 200, 100);
        tables.getChildren().addAll(table, time, price);
        takeTable.setScene(scene);
        takeTable.setTitle("итог");
        takeTable.show();
    }

    /**
     * Calculates the total usage time of the specified table.
     *
     * @param index index of the table whose usage time is to be accumulated
     */
    @Override
    public void accumulateTableUsageTime(int index) {
        logger.info("Рассчёт общего времени использования стола №" + (index + 1));
        totalUsageTimes[index] += elapsedTimes[index];
    }

    /**
     * Calculates the total profit for the specified table based on the time
     * it has been in use.
     *
     * @param index index of the table for which to calculate the profit
     */
    @Override
    public void calculateTotalProfit(int index) {
        logger.info("Расчет общей прибыли для стола №" + (index + 1));
        totalProfit += elapsedTimes[index] * 3;
    }

    /**
     * Updates the most profitable table based on the current
     * profit.
     *
     * @param index index of the table whose profit should be updated
     */
    @Override
    public void updateMostProfitableTable(int index) {
        mostProfitableTables[index] += elapsedTimes[index] * 3;
        for (int i = 0; i < 10; ++i) {
            if (maxProfitValue < mostProfitableTables[i]) {
                logger.info("Обнавление счетчика наиболее прибольного стола на стол.");
                maxProfitValue = mostProfitableTables[i];
                mostProfitableTableIndex = i + 1;
            }
        }
    }

    /**
     * Updates the popularity counter of the specified table.
     *
     * @param index index of the table whose popularity should be updated
     */
    @Override
    public void updateTablePopularity(int index) {
        tablePopularityCount[index] += 1;
        for (int i = 0; i < tablePopularityCount.length; ++i) {
            if (maxPopularityValue < tablePopularityCount[i]) {
                logger.info("Обновление счетчика популярности.");
                maxPopularityValue = tablePopularityCount[i];
                mostPopularTableIndex = i + 1;
            }
        }
    }

    /**
     * Updates the table button status and corresponding labels using animation.
     *
     * @param tableButton   table button
     * @param statusLabel   table status label
     * @param timeOfReserve reservation timestamp
     * @param priceForTable price label for the table
     * @param index         table index
     */
    @Override
    public void refreshTableButtonStatus(Button tableButton, Label statusLabel, Label timeOfReserve, Label priceForTable, int index) {
        Timeline timelines = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            applyButtonStyleBasedOnStatus(tableButton, tableStatuses[index]); // Initial style
            if (tableStatuses[index] == TableStatus.OCCUPIED) {
                statusLabel.setText("Занят");
            }
            timeOfReserve.setText("Время: " + elapsedTimes[index] + " мин");
            priceForTable.setText("Цена: " + elapsedTimes[index] * 3 + " р");
        }));
        timelines.setCycleCount(Animation.INDEFINITE);
        timelines.play();
    }
}