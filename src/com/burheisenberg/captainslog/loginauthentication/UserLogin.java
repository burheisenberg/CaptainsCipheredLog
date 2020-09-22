package com.burheisenberg.captainslog.loginauthentication;

import com.burheisenberg.captainslog.ciphering.Caesar;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserLogin {

    private static UserLogin instance = new UserLogin();

    //users will be stored in an observable list
    private List<User> users;

    //and in a binary file
    private String usersFile = "usersFile.ccl";

    private User authorizedUser;


    public static UserLogin getInstance() {
        return instance;
    }

    private UserLogin() {
    }

    public List<User> getUsers() {
        return users;
    }

    public User getAuthorizedUser() {
        return authorizedUser;
    }

    public void setAuthorizedUser(User authorizedUser) {
        this.authorizedUser = authorizedUser;
    }

    public void addNewUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void loadUsers() throws IOException {

        //initialize the observable list
        users = new ArrayList<>();
        String input;

        try{
            Path path = Paths.get(usersFile);
            BufferedReader br = Files.newBufferedReader(path);

                //read
            while((input = br.readLine()) != null) {

                //decipher
                input = Caesar.getInstance().decrypt(input);
                //split
                String[] userData = input.split(Caesar.getInstance().getSplitter());
                //take the elements
                String userName = userData[0];
                String password = userData[1];

                //add the user to the list
                users.add(new User(userName, password));

                //print out a message to inform which users are imported for debugging issues
                //System.out.println("The user log " + user + " has been imported.");
                }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public void storeUsers() throws IOException {

        Path path = Paths.get(usersFile);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try{

            for(User user : users) {
                //encrypt each element
                String encryptedUserName = Caesar.getInstance().encrypt(user.getUserName());
                String encryptedSplitter = Caesar.getInstance().encrypt(Caesar.getInstance().getSplitter());
                String encryptedPassword = Caesar.getInstance().encrypt(user.getPassword());
                //combine and write them
                bw.write(encryptedUserName + encryptedSplitter + encryptedPassword);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public int searchInUsers(String userName) {
        for(User user : users) {
            if(user.getUserName().equals(userName)) {
                return users.indexOf(user);
            }
        }

        return -1;
    }


}
