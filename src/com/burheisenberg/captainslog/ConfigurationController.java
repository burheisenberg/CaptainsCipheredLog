package com.burheisenberg.captainslog;

import com.burheisenberg.captainslog.dailybooks.DailyBook;
import com.burheisenberg.captainslog.dailybooks.DailyBookRecords;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class ConfigurationController {

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea contentTextArea;

    @FXML
    private DatePicker datePicker;

    public void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    public DailyBook createDailyBook() {
        //take the input
        String title = titleTextField.getText().trim();
        String content = contentTextArea.getText().trim();
        LocalDate date = datePicker.getValue();

        //create the dailybook
        DailyBook dailyBook = new DailyBook(title, content, date);

        //record it
        DailyBookRecords.getInstance().addDailyBook(dailyBook);

        return dailyBook;
    }

    public void printSelectedLog(DailyBook selectedLog) {
        titleTextField.setText(selectedLog.getTitle());
        contentTextArea.setText(selectedLog.getContent());
        datePicker.setValue(selectedLog.getDate());
    }

    public DailyBook replaceSelectedLog(DailyBook selectedLog) {

        //obtain the values
        String title = titleTextField.getText().trim();
        String content = contentTextArea.getText().trim();
        LocalDate date = datePicker.getValue();

        //create the new log
        DailyBook newLog = new DailyBook(title, content, date);

        //replace selected log with the new one
        DailyBookRecords.getInstance().replaceDailyBooks(selectedLog, newLog);

        return newLog;
    }
}
