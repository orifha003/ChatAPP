/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.progapp;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the ChatApp.
 * Shows the register/login menu and, after a successful login, the QuickChat menu.
 */
public class ProgAPP {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RegisterConsole registerConsole = new RegisterConsole();

        boolean running = true;

        // while loop keeps the main menu running until the user chooses Exit
        while (running) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Choose an option: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    registerUser(input, registerConsole);
                    break;
                case "2":
                    // QuickChat only opens after a successful login
                    if (loginUser(input, registerConsole)) {
                        quickChat(input, registerConsole);
                    }
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, or 3.");
                    break;
            }
        }
    }

    // asks for the registration details, repeating each question until the answer is valid
    static void registerUser(Scanner input, RegisterConsole registerConsole) {
        System.out.println("Enter first name: ");
        String firstName = input.nextLine();
        System.out.println("********************");

        System.out.println("Enter last name: ");
        String lastName = input.nextLine();
        System.out.println("********************");

        String username;
        while (true) {
            System.out.println("Enter username: ");
            username = input.nextLine();
            System.out.println("********************");
            System.out.println(registerConsole.usernameMessage(username));
            if (registerConsole.checkUserName(username)) {
                break;
            }
        }

        String password;
        while (true) {
            System.out.println("Enter password: ");
            password = input.nextLine();
            System.out.println("********************");
            System.out.println(registerConsole.passwordMessage(password));
            if (registerConsole.checkPasswordComplexity(password)) {
                break;
            }
        }

        String cellNumber;
        while (true) {
            System.out.println("Enter South African cell phone number (e.g. +27838968976): ");
            cellNumber = input.nextLine();
            System.out.println("********************");
            System.out.println(registerConsole.cellNumberMessage(cellNumber));
            if (registerConsole.checkCellPhoneNumber(cellNumber)) {
                break;
            }
        }

        System.out.println(registerConsole.registerUser(firstName, lastName, username, password, cellNumber));
    }

    // returns true if the login worked
    static boolean loginUser(Scanner input, RegisterConsole registerConsole) {
        System.out.println("Enter username: ");
        String username = input.nextLine();
        System.out.println("********************");

        System.out.println("Enter password: ");
        String password = input.nextLine();
        System.out.println("********************");

        boolean loginSuccess = registerConsole.loginUser(username, password);
        System.out.println(registerConsole.returnLoginStatus(loginSuccess));
        return loginSuccess;
    }

    // keeps asking until the user types a whole number of messages
    static int askHowManyMessages(Scanner input) {
        int maxMessages = -1;
        while (maxMessages < 0) {
            System.out.println("How many messages do you wish to enter? ");
            String answer = input.nextLine();
            try {
                maxMessages = Integer.parseInt(answer.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
        return maxMessages;
    }

    // the QuickChat menu, shown after a successful login
    static void quickChat(Scanner input, RegisterConsole registerConsole) {
        Message message = new Message(registerConsole.storedUsername);

        // fills the stored messages array from the JSON file
        message.loadStoredMessages();

        System.out.println("Welcome to QuickChat.");

        int maxMessages = askHowManyMessages(input);
        boolean messagesEntered = false;
        boolean chatting = true;

        // while loop keeps QuickChat running until the user chooses Quit
        while (chatting) {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.println("Choose an option: ");
            String option = input.nextLine();

            switch (option) {
                case "1":
                    if (messagesEntered) {
                        System.out.println("You have already entered the " + maxMessages + " message(s) you set.");
                    } else {
                        // for loop runs once for each message the user said they want to enter
                        for (int messageNumber = 0; messageNumber < maxMessages; messageNumber++) {
                            System.out.println("\nMessage " + (messageNumber + 1) + " of " + maxMessages);
                            sendOneMessage(input, message, messageNumber);
                        }
                        messagesEntered = true;
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    chatting = false;
                    break;
                case "4":
                    storedMessagesMenu(input, message);
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, 3, or 4.");
                    break;
            }
        }

        System.out.println("\n===== MESSAGES SENT =====");
        System.out.println(message.printMessages());
        System.out.println("Total messages sent: " + message.returnTotalMessages());
    }

    // the Stored Messages menu: searches and reports that use the arrays
    static void storedMessagesMenu(Scanner input, Message message) {
        boolean viewing = true;

        while (viewing) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back");
            System.out.println("Choose an option: ");
            String option = input.nextLine();

            switch (option) {
                case "1":
                    System.out.println(message.displayStoredSenderAndRecipient());
                    break;
                case "2":
                    System.out.println(message.returnLongestStoredMessage());
                    break;
                case "3":
                    searchByIDOption(input, message);
                    break;
                case "4":
                    searchByRecipientOption(input, message);
                    break;
                case "5":
                    deleteByHashOption(input, message);
                    break;
                case "6":
                    System.out.println(message.displayReport(Message.STORED));
                    break;
                case "7":
                    viewing = false;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1 to 7.");
                    break;
            }
        }
    }

    // asks for a message ID and shows the matching recipient and message
    static void searchByIDOption(Scanner input, Message message) {
        System.out.println("Enter the message ID: ");
        String messageID = input.nextLine();
        System.out.println("Recipient: " + message.searchRecipientByID(messageID));
        System.out.println("Message: " + message.searchMessageByID(messageID));
    }

    // asks for a recipient and shows every message sent or stored for them
    static void searchByRecipientOption(Scanner input, Message message) {
        System.out.println("Enter the recipient's cell number: ");
        String recipient = input.nextLine();

        ArrayList<String> found = message.searchByRecipient(recipient);
        if (found.isEmpty()) {
            System.out.println("No messages found for that recipient.");
        }
        for (int i = 0; i < found.size(); i++) {
            System.out.println((i + 1) + ". " + found.get(i));
        }
    }

    // asks for a message hash and deletes the matching message
    static void deleteByHashOption(Scanner input, Message message) {
        System.out.println("Enter the message hash: ");
        String messageHash = input.nextLine();
        System.out.println(message.deleteByMessageHash(messageHash));
    }

    // keeps asking until the recipient number is valid
    static String askRecipient(Scanner input, Message message) {
        String recipient;
        while (true) {
            System.out.println("Enter recipient cell number (e.g. +27718693002): ");
            recipient = input.nextLine();
            System.out.println(message.checkRecipientCell(recipient));
            if (message.isValidCell(recipient)) {
                break;
            }
        }
        return recipient;
    }

    // keeps asking until the message is 250 characters or less
    static String askMessageText(Scanner input, Message message) {
        String text;
        while (true) {
            System.out.println("Enter your message: ");
            text = input.nextLine();
            System.out.println(message.checkMessageLength(text));
            if (message.isMessageLengthOk(text)) {
                break;
            }
        }
        return text;
    }

    // keeps asking until the user picks 1, 2 or 3
    static String askSendChoice(Scanner input) {
        String choice;
        while (true) {
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.println("Choose an option: ");
            choice = input.nextLine();
            if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                break;
            }
            System.out.println("Invalid option, please choose 1, 2, or 3.");
        }
        return choice;
    }

    // collects the details for one message, then sends, discards or stores it
    static void sendOneMessage(Scanner input, Message message, int messageNumber) {
        String messageID = message.generateMessageID();
        System.out.println(message.returnMessageIDStatus(messageID));

        String recipient = askRecipient(input, message);
        String text = askMessageText(input, message);
        String messageHash = message.createMessageHash(messageID, messageNumber, text);
        String choice = askSendChoice(input);

        System.out.println(message.sentMessage(Integer.parseInt(choice)));

        if (choice.equals("1")) {
            message.addSentMessage(messageID, messageHash, recipient, text);
            System.out.println("Message ID: " + messageID);
            System.out.println("Message Hash: " + messageHash);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + text);
        } else if (choice.equals("2")) {
            message.addDisregardedMessage(messageID, messageHash, recipient, text);
            String zero = input.nextLine();
            if (zero.trim().equals("0")) {
                System.out.println("Message deleted.");
            } else {
                System.out.println("Message was not deleted, but it was not sent either.");
            }
        } else {
            message.storeMessage(messageID, messageHash, recipient, text);
        }
    }
}