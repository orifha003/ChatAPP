/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progapp;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author orifh
 */

public class Message {

    // each message is saved as {id, hash, recipient, message}
    private ArrayList<String[]> sentMessages = new ArrayList<>();
    private ArrayList<String[]> storedMessages = new ArrayList<>();

    // creates a random ten-digit message ID
    String generateMessageID() {
        Random random = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + random.nextInt(10);
        }
        return id;
    }

    // message ID must be no more than ten characters
    boolean checkMessageID(String id) {
        return id.length() > 0 && id.length() <= 10;
    }

    String returnMessageIDStatus(String id) {
        return "Message ID generated: " + id;
    }

    // recipient must start with + and have only digits after it
    boolean isValidCell(String number) {
        boolean startsWithPlus = number.length() > 1 && number.charAt(0) == '+';
        boolean allDigits = true;
        for (int i = 1; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) {
                allDigits = false;
            }
        }
        boolean validLength = number.length() <= 13;
        return startsWithPlus && allDigits && validLength;
    }

    String checkRecipientCell(String number) {
        if (isValidCell(number)) {
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
        int extra = message.length() - 250;
        return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
    }

    // keeps only letters and digits, so "tonight?" becomes "tonight"
    private String lettersOnly(String word) {
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                result = result + c;
            }
        }
        return result;
    }

    // hash = first two digits of ID : message number : first word + last word (all caps)
    String createMessageHash(String id, int messageNumber, String message) {
        String firstTwo = id.length() >= 2 ? id.substring(0, 2) : id;
        String[] words = message.trim().split("\\s+");
        String first = lettersOnly(words[0]);
        String last = lettersOnly(words[words.length - 1]);
        return (firstTwo + ":" + messageNumber + ":" + first + last).toUpperCase();
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

    void addSentMessage(String id, String hash, String recipient, String message) {
        sentMessages.add(new String[]{id, hash, recipient, message});
    }

    // returns all messages sent while the program is running
    String printMessages() {
        if (sentMessages.size() == 0) {
            return "No messages have been sent.";
        }
        String output = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            String[] m = sentMessages.get(i);
            output = output + "Message ID: " + m[0] + "\n"
                    + "Message Hash: " + m[1] + "\n"
                    + "Recipient: " + m[2] + "\n"
                    + "Message: " + m[3] + "\n\n";
        }
        return output.trim();
    }

    int returnTotalMessages() {
        return sentMessages.size();
    }

    // makes text safe to put inside JSON quotes
    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // saves the stored messages to messages.json
    void storeMessage(String id, String hash, String recipient, String message) {
        storedMessages.add(new String[]{id, hash, recipient, message});

        String json = "[\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            String[] m = storedMessages.get(i);
            json = json + "  {\n";
            json = json + "    \"messageID\": \"" + escape(m[0]) + "\",\n";
            json = json + "    \"messageHash\": \"" + escape(m[1]) + "\",\n";
            json = json + "    \"recipient\": \"" + escape(m[2]) + "\",\n";
            json = json + "    \"message\": \"" + escape(m[3]) + "\"\n";
            json = json + "  }";
            if (i < storedMessages.size() - 1) {
                json = json + ",";
            }
            json = json + "\n";
        }
        json = json + "]";

        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(json);
        } catch (IOException e) {
            System.out.println("Could not save the message to the JSON file.");
        }
    }
}

