package com.example.anti_cafe;

import org.junit.jupiter.api.Test;

import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainWindowTest {

    /**
     * Tests the start of the timer for the table.
     * Checks that the timer starts correctly.
     */
    @Test
    void startTableTimer() {
        int index = 0;
        MainWindow.startTableTimer(index);

        assertNotNull(MainWindow.tableTimers[index], "Таймер для стола не инициализирован");

        long initialElapsedTime = MainWindow.elapsedTimes[index];

        // Запускаем задачу таймера вручную
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++MainWindow.elapsedTimes[index];
            }
        };

        task.run(); // запускаем задачу
        assertEquals(initialElapsedTime + 1, MainWindow.elapsedTimes[index], "Время не увеличилось.");

    }
}
