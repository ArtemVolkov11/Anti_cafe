package com.example.anti_cafe;

import javafx.scene.control.Label;

public interface ActionForButtonArchive {

    /**
     * Displays an archive window with information about revenue,
     * average reservation time of tables, the most popular table,
     * and the table with the highest profit.
     */
    void showArchiveWindow();

    /**
     * Updates the tags in the archive with current information about revenue, average
     * table reservation time, the most popular table, and the table with the highest profit.
     *
     * @param allProfit        tag displaying the total revenue
     * @param averageTableTime tag displaying the average table reservation time
     * @param mostPopularTable tag displaying the most popular table
     * @param mostIncomeTable  tag displaying the table with the highest profit
     */
    void updateLabels(Label allProfit, Label averageTableTime, Label mostPopularTable, Label mostIncomeTable);

    /**
     * Calculates the average reservation time for the specified table.
     *
     * @param index index of the table for which to calculate the average time
     * @return average reservation time for the table
     */
    double calculateAverageTimeForTable(int index);
}
