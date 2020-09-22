package com.burheisenberg.captainslog;

import com.burheisenberg.captainslog.loginauthentication.User;
import com.burheisenberg.captainslog.loginauthentication.UserLogin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userNameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label loginStatus;

    @FXML
    private CheckBox newUserCheckBox;

    @FXML
    private Button loginButton;

    public void initialize() {
        //test codes here
    }

    public void loadMainScreen() throws IOException {
        //start the main screen
        Parent root = FXMLLoader.load(getClass().getResource("resources/MainScreen.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Captain's Ciphered Log");
        stage.show();
        //hide the login screen
        loginButton.getScene().getWindow().hide();
    }

    /**
     * obtain the user from login dialog
     * @return
     */
    public User getUser() {

        //check whether the user is present
        String userName = userNameField.getText();
        int indexOfUser = UserLogin.getInstance().searchInUsers(userName);
        String password = passwordField.getText();

        //check if the box is checked
        if(newUserCheckBox.isSelected()) {

            //if the username is not found, create a new user and return it
            if(indexOfUser == -1) {
                User newUser = new User(userName, password);
                UserLogin.getInstance().addNewUser(newUser);
                loginStatus.setText("New user has been successfully added");
                return newUser;
            } else {
                //inform that this username has been taken
                loginStatus.setText("The username has been taken.");
                return null;
            }
        }

        //if the user does not exist, recommend to create one
        if(indexOfUser == -1){
            loginStatus.setText("The user does not exist.");
            return null;
            //if the user exist, check the password is correct
            //if not, give an error
        } else if(UserLogin.getInstance().getUsers().get(indexOfUser).getPassword().equals(password)) {
            loginStatus.setText("The login is successful!");
            return UserLogin.getInstance().getUsers().get(indexOfUser);
        } else {
            loginStatus.setText("The password is incorrect");
            return null;
        }

    }

    @FXML
    private void loginButtonHandle() throws IOException {

        //return  a valid user
        User probableUser = getUser();
        if (probableUser != null) {
            UserLogin.getInstance().setAuthorizedUser(probableUser);
            loadMainScreen();
        }
    }

    private void waitForTheButton(Button button) {
        while(!button.isPressed()) {
            //wait until the button is pressed
        }
    }
}
