package com.example.anti_cafe;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public interface ActionForButtonCloseTable {

    /**
     * Opens the table close window and initializes the buttons for each table.
     */
    void openTableClosureWindow();

    /**
     * Sets the table status to available and updates the corresponding label.
     *
     * @param index       index of the table to be released
     * @param statusLabel label to display the table status
     */
    void setTableStatusToAvailable(int index, Label statusLabel);

    /**
     * Applies a button style based on the table status.
     *
     * @param button the button to apply the style to
     * @param status the table status
     */
    void applyButtonStyleBasedOnStatus(Button button, MainWindow.TableStatus status);

    /**
     * Stops the timer for the specified table and resets its time.
     *
     * @param index index of the table for which to stop the timer
     */
    void cancelTableTimer(int index);

    /**
     * Displays a closing summary for the specified table, including information about its
     * usage time and price.
     *
     * @param index the index of the table for which to display the summary
     */
    void showClosureSummary(int index);

    /**
     * Calculates the total usage time of the specified table.
     *
     * @param index index of the table whose usage time is to be accumulated
     */
    void accumulateTableUsageTime(int index);

    /**
     * Calculates the total profit for the specified table based on the time
     * it has been in use.
     *
     * @param index index of the table for which to calculate the profit
     */
    void calculateTotalProfit(int index);

    /**
     * Updates the most profitable table based on the current
     * profit.
     *
     * @param index index of the table whose profit should be updated
     */
    void updateMostProfitableTable(int index);

    /**
     * Updates the popularity counter of the specified table.
     *
     * @param index index of the table whose popularity should be updated
     */
    void updateTablePopularity(int index);

    /**
     * Updates the table button status and corresponding labels using animation.
     *
     * @param tableButton   table button
     * @param statusLabel   table status label
     * @param timeOfReserve reservation timestamp
     * @param priceForTable price label for the table
     * @param index         table index
     */
    void refreshTableButtonStatus(Button tableButton, Label statusLabel, Label timeOfReserve, Label priceForTable, int index);
}