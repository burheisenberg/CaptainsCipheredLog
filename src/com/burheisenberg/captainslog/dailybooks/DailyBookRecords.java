package com.burheisenberg.captainslog.dailybooks;

import com.burheisenberg.captainslog.ciphering.Caesar;
import com.burheisenberg.captainslog.loginauthentication.User;
import com.burheisenberg.captainslog.loginauthentication.UserLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class DailyBookRecords {
    private static DailyBookRecords instance = new DailyBookRecords();

    private String dailyRecordsFile = "dailyBooks.ccl";

    private List<DailyBook> dailyBooks;
    private ObservableList<DailyBook> ownedBooks;

    public static DailyBookRecords getInstance() {
        return instance;
    }

    private DailyBookRecords() {
    }

    public List<DailyBook> getDailyBooks() {
        return dailyBooks;
    }

    public void loadDailyBooks() {

        //initialize the daily books list
        dailyBooks = FXCollections.observableArrayList();

        try {
            Path path = Paths.get(dailyRecordsFile);
            BufferedReader br = Files.newBufferedReader(path);
            String input;

            while((input = br.readLine()) != null) {

                //decrypted input
                input = Caesar.getInstance().decrypt(input);

                //divide into elements
                String[] dailyData = input.split(Caesar.getInstance().getSplitter());

                //obtain the elements
                String title = dailyData[0];
                String content = dailyData[1];
                String dateString = dailyData[2];
                LocalDate date = LocalDate.parse(dateString);
                String ownerName = dailyData[3];
                int indexOfOwner = UserLogin.getInstance().searchInUsers(ownerName);
                User owner = UserLogin.getInstance().getUsers().get(indexOfOwner);

                //create the related dailybook
                DailyBook obtainedDailyBook = new DailyBook(title, content, date, owner);

                //add to the list
                dailyBooks.add(obtainedDailyBook);
            }
        } catch (IOException e) {
            System.out.println("IOException: daily books could not be imported.");
        }
    }

    public void storeDailyBooks() {
        try{
            Path path = Paths.get(dailyRecordsFile);
            BufferedWriter bw = Files.newBufferedWriter(path);

            for(DailyBook daily : dailyBooks) {
                //encrypt each element
                String encryptedTitle = Caesar.getInstance().encrypt(daily.getTitle());
                String encryptedSplitter = Caesar.getInstance().encrypt(Caesar.getInstance().getSplitter());
                String encryptedContent = Caesar.getInstance().encrypt(daily.getContent());
                String encryptedDate = Caesar.getInstance().encrypt(daily.getDate().toString());
                String encryptedOwner = Caesar.getInstance().encrypt(daily.getOwner().toString());
                //combine and write them
                bw.write(encryptedTitle + encryptedSplitter + encryptedContent + encryptedSplitter
                            + encryptedDate + encryptedSplitter + encryptedOwner);
                bw.newLine();
            }
            bw.close();
        } catch(IOException e) {
            System.out.println("IOException: daily books could not be stored.");
        }
    }

    public void addDailyBook(DailyBook dailyBook) {
        dailyBooks.add(dailyBook);
        getOwnedBooks();
    }

    public void removeDailyBook(DailyBook dailyBook) {
        dailyBooks.remove(dailyBook);
        getOwnedBooks();
    }

    /**
     * unfinished code here
     *
     * @param dailyBook
     * @return
     */
    public int searchInDailyBooks(DailyBook dailyBook) {

        for(DailyBook daily : dailyBooks) {
            if(daily == dailyBook) {
                return dailyBooks.indexOf(daily);
            }
        }
        return -1;
    }

    /**
     * unfinished code here
     *
     * @param oldBook
     * @param newBook
     * @return
     */
    public void replaceDailyBooks(DailyBook oldBook, DailyBook newBook) {

        //obtain the index of oldLog
        int index = searchInDailyBooks(oldBook);

        //remove the oldLog
        dailyBooks.remove(oldBook);

        //add the newLog to the specified index
        dailyBooks.add(index, newBook);
    }


    /**
     *
     * this method returns the authorized (who logged in) user's books
     *
     * @return
     */
    public ObservableList<DailyBook> getOwnedBooks() {

        //refresh the list
        ownedBooks = FXCollections.observableArrayList();
        for(DailyBook daily : dailyBooks) {
            if(daily.getOwner().matches(UserLogin.getInstance().getAuthorizedUser())) {
                ownedBooks.add(daily);
            }
        }

        return ownedBooks;
    }
}
