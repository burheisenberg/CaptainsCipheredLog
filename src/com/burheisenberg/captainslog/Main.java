package com.burheisenberg.captainslog;

import com.burheisenberg.captainslog.dailybooks.DailyBookRecords;
import com.burheisenberg.captainslog.loginauthentication.UserLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/LoginScreen.fxml"));
        primaryStage.setTitle("Login Info");
        primaryStage.setScene(new Scene(root, 250, 150));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        try {
            UserLogin.getInstance().loadUsers();
            DailyBookRecords.getInstance().loadDailyBooks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        try {
            UserLogin.getInstance().storeUsers();
            DailyBookRecords.getInstance().storeDailyBooks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
