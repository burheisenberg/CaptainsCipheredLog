package com.burheisenberg.captainslog.ciphering;

public class Caesar {
    private static Caesar instance = new Caesar();

    int levelOfCiphering;
    String splitter;

    public static Caesar getInstance() {
        return instance;
    }

    private Caesar() {
        splitter="woıebgvq";
        levelOfCiphering = 3;
    }

    public String getSplitter() {
        return splitter;
    }

    public String encrypt(String message) {

        //take a char array copy of the message
        char[] chars = message.toCharArray();

        //encrypt each single character
        for(int i=0; i<chars.length; i++) {
            chars[i] = (char)(chars[i] + levelOfCiphering);
        }

        //initialize an empty string
        String encryptedMessage = "";

        //merge character array and put it into the string
        for(int i=0; i<chars.length; i++) {
            encryptedMessage = encryptedMessage + chars[i];
        }

        return encryptedMessage;
    }

    public String decrypt(String message) {

        //put the message into a char array
        char[] chars = message.toCharArray();

        //decrypt each single character
        for(int i=0; i<chars.length; i++) {
            chars[i] = (char) (chars[i] - levelOfCiphering);
        }

        //initialize an empty string
        String decryptedMessage = "";

        //merge the characters
        for(int i=0; i<chars.length; i++) {
            decryptedMessage = decryptedMessage + chars[i];
        }

        return decryptedMessage;
    }
}
