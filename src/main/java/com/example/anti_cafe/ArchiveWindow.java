package com.example.anti_cafe;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class responsible for displaying the archive user interface,
 * which includes information about revenue, average table reservation time,
 * the most popular table, and the table with the highest profit.
 */
public class ArchiveWindow extends TableCloseWindow implements ActionForButtonArchive {

    double[] averageTableTimes = new double[10];

    /**
     * Displays an archive window with information about revenue,
     * average reservation time of tables, the most popular table,
     * and the table with the highest profit.
     */
    @Override
    public void showArchiveWindow() {
        Stage archive = new Stage();
        VBox tables = new VBox(10);
        Label totalProfitLabel = new Label("Выручка: ");
        Label averageTimeLabel = new Label("Среднее время брони столов: ");
        Label mostPopularTableLabel = new Label("Чаще всего берут стол №");
        Label mostIncomeTableLabel = new Label("Больше всего заработок от ");

        updateLabels(totalProfitLabel, averageTimeLabel, mostPopularTableLabel, mostIncomeTableLabel);
        tables.getChildren().addAll(totalProfitLabel, averageTimeLabel, mostPopularTableLabel, mostIncomeTableLabel);
        Scene scene = new Scene(tables, 600, 400);
        archive.setScene(scene);
        archive.setTitle("Архив");
        archive.show();
    }

    /**
     * Updates the tags in the archive with current information about revenue, average
     * table reservation time, the most popular table, and the table with the highest profit.
     *
     * @param allProfit        tag displaying the total revenue
     * @param averageTableTime tag displaying the average table reservation time
     * @param mostPopularTable tag displaying the most popular table
     * @param mostIncomeTable  tag displaying the table with the highest profit
     */
    @Override
    public void updateLabels(Label allProfit, Label averageTableTime, Label mostPopularTable, Label mostIncomeTable) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            allProfit.setText("Выручка: " + totalProfit);
            averageTableTime.setText("Среднее время брони столов: " + "\n" + "Стол №1: " + calculateAverageTimeForTable(0) + " мин" + "\n"
                    + "Стол №2: " + calculateAverageTimeForTable(1) + " мин" + "\n" + "Стол №3: " + calculateAverageTimeForTable(2) + " мин" + "\n"
                    + "Стол №4: " + calculateAverageTimeForTable(3) + " мин" + "\n" + "Стол №5: " + calculateAverageTimeForTable(4) + " мин" + "\n"
                    + "Стол №6: " + calculateAverageTimeForTable(5) + " мин" + "\n" + "Стол №7: " + calculateAverageTimeForTable(6) + " мин" + "\n"
                    + "Стол №8: " + calculateAverageTimeForTable(7) + " мин" + "\n" + "Стол №9: " + calculateAverageTimeForTable(8) + " мин" + "\n"
                    + "Стол №10: " + calculateAverageTimeForTable(9) + " мин");
            mostPopularTable.setText("Чаще всего берут стол №" + mostPopularTableIndex);
            mostIncomeTable.setText("Больше всего заработок от " + mostProfitableTableIndex + " стола");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Calculates the average reservation time for the specified table.
     *
     * @param index index of the table for which to calculate the average time
     * @return average reservation time for the table
     */
    @Override
    public double calculateAverageTimeForTable(int index) {
        if (tablePopularityCount[index] != 0) {
            averageTableTimes[index] = totalUsageTimes[index] / tablePopularityCount[index];
        }
        return averageTableTimes[index];
    }
}
