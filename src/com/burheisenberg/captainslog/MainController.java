package com.burheisenberg.captainslog;

import com.burheisenberg.captainslog.dailybooks.DailyBook;
import com.burheisenberg.captainslog.dailybooks.DailyBookRecords;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ListView <DailyBook> logsListView;

    @FXML
    private TextArea logsDetailsTextArea;

    @FXML
    private Label logsDateLabel;

    @FXML
    private Button newLogButton;

    @FXML
    private Button editLogButton;

    @FXML
    private Button deleteButton;

    //context menu for the right-click
    @FXML
    private ContextMenu listContextMenu;

    public void initialize() {


        //initializing the context menu object
        listContextMenu = new ContextMenu();

        //initializing the delete option
        MenuItem deleteOption = new MenuItem("Delete");
        deleteOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //ask for confirmation
                deleteLog();
                //refresh the list
                logsListView.setItems(DailyBookRecords.getInstance().getOwnedBooks());
            }
        });
        //add it to the context menu
        listContextMenu.getItems().addAll(deleteOption);

        //initializing the edit option
        MenuItem editOption = new MenuItem("Edit");
        editOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    editLogScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //refresh the list
                logsListView.setItems(DailyBookRecords.getInstance().getOwnedBooks());
            }
        });
        //add it to the context menu
        listContextMenu.getItems().addAll(editOption);


        logsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DailyBook>() {
            @Override
            public void changed(ObservableValue<? extends DailyBook> observableValue, DailyBook previousBook, DailyBook newBook) {
                if (newBook != null) {
                    DailyBook dailyBook = logsListView.getSelectionModel().getSelectedItem();
                    logsDetailsTextArea.setText(dailyBook.getContent());
                    logsDateLabel.setText(dailyBook.getDate().toString());
                }
            }
        });

        logsListView.setItems(DailyBookRecords.getInstance().getOwnedBooks());
        logsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        logsListView.getSelectionModel().selectFirst();


        //in order to implement the context menu to the logslistview
        //there is a lot I need to figure out here
        logsListView.setCellFactory(new Callback<ListView<DailyBook>, ListCell<DailyBook>>() {
            @Override
            public ListCell<DailyBook> call(ListView<DailyBook> dailyBookListView) {
                ListCell<DailyBook> cell = new ListCell<DailyBook>() {
                    @Override
                    protected void updateItem(DailyBook dailyBook, boolean empty) {
                        super.updateItem(dailyBook, empty);
                        if(empty) {
                            setText(null);
                        } else {
                            setText(dailyBook.getTitle());
                        }
                    }
                };

                //if the line is empty, don't show the context menu
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );

                return cell;
            }
        });
    }

    /**
     * an empty configuraiton screen for a new log
     * @throws IOException
     */
    @FXML
    public void newLogScreen() throws IOException {

        //setup the dialog window
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Configure the log");

        //initialize the loader
        FXMLLoader loader = new FXMLLoader();

        //load the dialog window
        try {
            loader.setLocation(getClass().getResource("resources/ConfigurationScreen.fxml"));
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        //obtain the controller
        ConfigurationController controller = loader.getController();

        //add the buttons
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> choice = dialog.showAndWait();
        if(choice.isPresent() && (choice.get() == ButtonType.OK)) {
            //obtain the created dailybook
            DailyBook newDaily = controller.createDailyBook();
            //refresh the list
            logsListView.setItems(DailyBookRecords.getInstance().getOwnedBooks());
            //select the new one
            logsListView.getSelectionModel().select(newDaily);
        }
    }

    /**
     * a configuration screen with printed values of the log-to-be-edited
     * @throws IOException
     */
    @FXML
    public void editLogScreen() throws IOException {

        //setup the dialog window
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Configure the log");

        //initialize the loader
        FXMLLoader loader = new FXMLLoader();

        //load the dialog window
        try {
            loader.setLocation(getClass().getResource("resources/ConfigurationScreen.fxml"));
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        //obtain the controller
        ConfigurationController controller = loader.getController();

        //add the buttons
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //get the selected book oldbook
        DailyBook oldBook = logsListView.getSelectionModel().getSelectedItem();
        //print its values
        controller.printSelectedLog(oldBook);

        Optional<ButtonType> choice = dialog.showAndWait();
        if(choice.isPresent() && (choice.get() == ButtonType.OK)) {
            //obtain the created dailybook
            DailyBook newBook = controller.replaceSelectedLog(oldBook);
            //refresh the list
            logsListView.setItems(DailyBookRecords.getInstance().getOwnedBooks());
            //select the new one
            logsListView.getSelectionModel().select(newBook);
        }
    }

    /**
     * alert window for deleting log
     *
     */
    @FXML
    public void deleteLog() {
        //get the selected log
        DailyBook selectedLog = logsListView.getSelectionModel().getSelectedItem();
        //create an alert window by using a ready-to-go layout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete the log");
        alert.setHeaderText(null);
        alert.setContentText("Do you confirm to delete \"" + selectedLog.getTitle() + "\"");

        //take the choice
        Optional<ButtonType> choice = alert.showAndWait();
        //if ok pressed
        if(choice.isPresent() && (choice.get() == (ButtonType.OK))) {
            //remove the log from the list
            DailyBookRecords.getInstance().removeDailyBook(selectedLog);
        }

        //refresh the logslist
        logsListView.setItems(DailyBookRecords.getInstance().getOwnedBooks());
        // TODO: 7.09.2020 the listview needs to be refreshed after each action such as deleting, editing or creating one
        //  and selected the reagarding item.
        //select the first log in the list
        logsListView.getSelectionModel().selectFirst();
    }

}
