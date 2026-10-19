/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Message {

    // each message is saved as {message ID, message hash, recipient, message text}
    private ArrayList<String[]> sentMessages = new ArrayList<>();
    private ArrayList<String[]> storedMessages = new ArrayList<>();

    // reuses the cell number check written for Part 1
    private RegisterConsole registerChecks = new RegisterConsole();

    // name of the JSON file (the unit tests change this so they do not touch the real file)
    String jsonFileName = "messages.json";

    // creates a random ten-digit message ID using string manipulation
    String generateMessageID() {
        Random random = new Random();

        // random number with up to 19 digits (>>> 1 makes sure it is not negative)
        String digits = String.valueOf(random.nextLong() >>> 1);

        // pad with zeros at the front if it is shorter than ten digits
        while (digits.length() < 10) {
            digits = "0" + digits;
        }

        // substring keeps only the first ten digits
        return digits.substring(0, 10);
    }

    // message ID must be no more than ten characters
    boolean checkMessageID(String messageID) {
        return messageID.length() > 0 && messageID.length() <= 10;
    }

    String returnMessageIDStatus(String messageID) {
        return "Message ID generated: " + messageID;
    }

    // recipient must have an international code and be the correct length
    boolean isValidCell(String cellNumber) {
        return registerChecks.checkCellPhoneNumber(cellNumber);
    }

    String checkRecipientCell(String cellNumber) {
        if (isValidCell(cellNumber)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    boolean isMessageLengthOk(String message) {
        return message.length() <= 250;
    }

    String checkMessageLength(String message) {
        if (isMessageLengthOk(message)) {
            return "Message ready to send.";
        }
        int extraCharacters = message.length() - 250;
        return "Message exceeds 250 characters by " + extraCharacters + "; please reduce the size.";
    }

    // keeps only letters and digits, so "tonight?" becomes "tonight"
    private String lettersAndDigitsOnly(String word) {
        String cleanWord = "";
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (Character.isLetterOrDigit(letter)) {
                cleanWord = cleanWord + letter;
            }
        }
        return cleanWord;
    }

    // hash = first two digits of the ID : message number : first word + last word, all in caps
    String createMessageHash(String messageID, int messageNumber, String message) {
        String firstTwoDigits = messageID.substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = lettersAndDigitsOnly(words[0]);
        String lastWord = lettersAndDigitsOnly(words[words.length - 1]);
        return (firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // choice: 1 = send, 2 = disregard, 3 = store
    String sentMessage(int choice) {
        switch (choice) {
            case 1:
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    void addSentMessage(String messageID, String messageHash, String recipient, String message) {
        sentMessages.add(new String[]{messageID, messageHash, recipient, message});
    }

    // returns all messages sent while the program is running
    String printMessages() {
        if (sentMessages.size() == 0) {
            return "No messages have been sent.";
        }
        String allMessages = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            String[] sent = sentMessages.get(i);
            allMessages = allMessages + "Message ID: " + sent[0] + "\n"
                    + "Message Hash: " + sent[1] + "\n"
                    + "Recipient: " + sent[2] + "\n"
                    + "Message: " + sent[3] + "\n\n";
        }
        return allMessages.trim();
    }

    int returnTotalMessages() {
        return sentMessages.size();
    }

    // makes text safe to put inside JSON quotes
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Stores the message in a JSON file.
    // References: JSON format rules from https://www.json.org ;
    // FileWriter and try-with-resources from the Java SE API documentation (docs.oracle.com).
    void storeMessage(String messageID, String messageHash, String recipient, String message) {
        storedMessages.add(new String[]{messageID, messageHash, recipient, message});

        String json = "[\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            String[] stored = storedMessages.get(i);
            json = json + "  {\n";
            json = json + "    \"messageID\": \"" + escapeJson(stored[0]) + "\",\n";
            json = json + "    \"messageHash\": \"" + escapeJson(stored[1]) + "\",\n";
            json = json + "    \"recipient\": \"" + escapeJson(stored[2]) + "\",\n";
            json = json + "    \"message\": \"" + escapeJson(stored[3]) + "\"\n";
            json = json + "  }";
            if (i < storedMessages.size() - 1) {
                json = json + ",";
            }
            json = json + "\n";
        }
        json = json + "]";

        try (FileWriter writer = new FileWriter(jsonFileName)) {
            writer.write(json);
        } catch (IOException e) {
            System.out.println("Could not save the message to the JSON file.");
        }
    }
}