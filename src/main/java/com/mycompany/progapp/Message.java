/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Message class for QuickChat.
 * Creates message IDs and hashes, checks recipient and message length,
 * keeps the message arrays, searches them and stores messages in a JSON file.
 */
public class Message {

    // flags that say what the user did with a message
    static final String SENT = "Sent";
    static final String STORED = "Stored";
    static final String DISREGARD = "Disregard";

    // parallel arrays: the same index in each list belongs to the same message
    private ArrayList<String> messageIDs = new ArrayList<>();
    private ArrayList<String> messageHashes = new ArrayList<>();
    private ArrayList<String> recipients = new ArrayList<>();
    private ArrayList<String> messageTexts = new ArrayList<>();
    private ArrayList<String> messageFlags = new ArrayList<>();

    // messages grouped by what the user did with them
    private ArrayList<String> sentMessages = new ArrayList<>();
    private ArrayList<String> disregardedMessages = new ArrayList<>();
    private ArrayList<String> storedMessages = new ArrayList<>();

    // the user who is logged in (shown as the sender)
    private String sender;

    // reuses the cell number check written for Part 1
    private RegisterConsole registerChecks = new RegisterConsole();

    // name of the JSON file (the unit tests change this so they do not touch the real file)
    String jsonFileName = "messages.json";

    Message() {
        this("Unknown");
    }

    Message(String sender) {
        this.sender = sender;
    }

    //  message ID, recipient and length checks 

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

    // message hash 

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

    //  send, disregard, store -

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

    //  the arrays

    // adds a message to the parallel arrays, then updates the sent/disregarded/stored arrays
    void addMessage(String messageID, String messageHash, String recipient, String text, String flag) {
        messageIDs.add(messageID);
        messageHashes.add(messageHash);
        recipients.add(recipient);
        messageTexts.add(text);
        messageFlags.add(flag);
        rebuildMessageLists();
    }

    void addSentMessage(String messageID, String messageHash, String recipient, String text) {
        addMessage(messageID, messageHash, recipient, text, SENT);
    }

    void addDisregardedMessage(String messageID, String messageHash, String recipient, String text) {
        addMessage(messageID, messageHash, recipient, text, DISREGARD);
    }

    // fills the sent, disregarded and stored arrays from the parallel arrays
    private void rebuildMessageLists() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        for (int i = 0; i < messageTexts.size(); i++) {
            String flag = messageFlags.get(i);
            if (flag.equals(SENT)) {
                sentMessages.add(messageTexts.get(i));
            } else if (flag.equals(DISREGARD)) {
                disregardedMessages.add(messageTexts.get(i));
            } else if (flag.equals(STORED)) {
                storedMessages.add(messageTexts.get(i));
            }
        }
    }

    ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    ArrayList<String> getStoredMessages() {
        return storedMessages;
    }

    ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    ArrayList<String> getMessageIDs() {
        return messageIDs;
    }

    int returnTotalMessages() {
        return sentMessages.size();
    }

    // returns all messages sent while the program is running
    String printMessages() {
        return displayReport(SENT);
    }

    //  stored messages features 

    // report with the full details of every message that has the given flag
    String displayReport(String flag) {
        String report = "";
        for (int i = 0; i < messageTexts.size(); i++) {
            if (messageFlags.get(i).equals(flag)) {
                report = report + "Message ID: " + messageIDs.get(i) + "\n"
                        + "Message Hash: " + messageHashes.get(i) + "\n"
                        + "Recipient: " + recipients.get(i) + "\n"
                        + "Message: " + messageTexts.get(i) + "\n\n";
            }
        }
        if (report.equals("")) {
            return "No " + flag.toLowerCase() + " messages.";
        }
        return report.trim();
    }

    // sender and recipient of every stored message
    String displayStoredSenderAndRecipient() {
        String output = "";
        for (int i = 0; i < messageTexts.size(); i++) {
            if (messageFlags.get(i).equals(STORED)) {
                output = output + "Sender: " + sender + ", Recipient: " + recipients.get(i) + "\n";
            }
        }
        if (output.equals("")) {
            return "No stored messages.";
        }
        return output.trim();
    }

    // the longest message in the stored messages array
    String returnLongestStoredMessage() {
        String longest = "";
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).length() > longest.length()) {
                longest = storedMessages.get(i);
            }
        }
        if (longest.equals("")) {
            return "No stored messages.";
        }
        return longest;
    }

    // finds the message text for a message ID
    String searchMessageByID(String messageID) {
        int index = messageIDs.indexOf(messageID);
        if (index == -1) {
            return "Message ID not found.";
        }
        return messageTexts.get(index);
    }

    // finds the recipient for a message ID
    String searchRecipientByID(String messageID) {
        int index = messageIDs.indexOf(messageID);
        if (index == -1) {
            return "Message ID not found.";
        }
        return recipients.get(index);
    }

    // all messages sent or stored for one recipient (disregarded messages are left out)
    ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            boolean sameRecipient = recipients.get(i).equals(recipient);
            boolean wasKept = !messageFlags.get(i).equals(DISREGARD);
            if (sameRecipient && wasKept) {
                found.add(messageTexts.get(i));
            }
        }
        return found;
    }

    // deletes a message from all the arrays using its message hash
    String deleteByMessageHash(String messageHash) {
        int index = messageHashes.indexOf(messageHash);
        if (index == -1) {
            return "Message hash not found.";
        }

        String text = messageTexts.get(index);
        boolean wasStored = messageFlags.get(index).equals(STORED);

        messageIDs.remove(index);
        messageHashes.remove(index);
        recipients.remove(index);
        messageTexts.remove(index);
        messageFlags.remove(index);
        rebuildMessageLists();

        // keep the JSON file up to date when a stored message is deleted
        if (wasStored) {
            saveStoredMessagesToJson();
        }
        return "Message \"" + text + "\" successfully deleted.";
    }

    //  JSON file 

    // makes text safe to put inside JSON quotes
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // adds the message to the stored messages and saves them all to the JSON file
    void storeMessage(String messageID, String messageHash, String recipient, String text) {
        addMessage(messageID, messageHash, recipient, text, STORED);
        saveStoredMessagesToJson();
    }

    // Writes every stored message to the JSON file.
    // References: JSON format rules from https://www.json.org ;
    // FileWriter and try-with-resources from the Java SE API documentation (docs.oracle.com).
    private void saveStoredMessagesToJson() {
        String json = "[\n";
        boolean firstEntry = true;
        for (int i = 0; i < messageTexts.size(); i++) {
            if (messageFlags.get(i).equals(STORED)) {
                if (!firstEntry) {
                    json = json + ",\n";
                }
                json = json + "  {\n";
                json = json + "    \"messageID\": \"" + escapeJson(messageIDs.get(i)) + "\",\n";
                json = json + "    \"messageHash\": \"" + escapeJson(messageHashes.get(i)) + "\",\n";
                json = json + "    \"recipient\": \"" + escapeJson(recipients.get(i)) + "\",\n";
                json = json + "    \"message\": \"" + escapeJson(messageTexts.get(i)) + "\"\n";
                json = json + "  }";
                firstEntry = false;
            }
        }
        json = json + "\n]";

        try (FileWriter writer = new FileWriter(jsonFileName)) {
            writer.write(json);
        } catch (IOException e) {
            System.out.println("Could not save the message to the JSON file.");
        }
    }

    // Reads the stored messages from the JSON file into the arrays.
    // References: reading a file line by line with Files.readAllLines from the Java SE API
    // documentation (docs.oracle.com); JSON format rules from https://www.json.org
    void loadStoredMessages() {
        File jsonFile = new File(jsonFileName);
        if (!jsonFile.exists()) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(jsonFile.toPath());
            String messageID = "";
            String messageHash = "";
            String recipient = "";

            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.startsWith("\"messageID\"")) {
                    messageID = readJsonValue(line);
                } else if (line.startsWith("\"messageHash\"")) {
                    messageHash = readJsonValue(line);
                } else if (line.startsWith("\"recipient\"")) {
                    recipient = readJsonValue(line);
                } else if (line.startsWith("\"message\"")) {
                    // "message" is the last key of each entry, so the entry is complete
                    addMessage(messageID, messageHash, recipient, readJsonValue(line), STORED);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read the JSON file.");
        }
    }

    // takes the value out of a line like:    "recipient": "+27838884567",
    private String readJsonValue(String line) {
        int start = line.indexOf(": \"") + 3;
        int end = line.lastIndexOf('"');
        String value = line.substring(start, end);

        // undo the escaping that was added when the message was stored
        String result = "";
        for (int i = 0; i < value.length(); i++) {
            char letter = value.charAt(i);
            if (letter == '\\' && i + 1 < value.length()) {
                i++;
                letter = value.charAt(i);
            }
            result = result + letter;
        }
        return result;
    }
}