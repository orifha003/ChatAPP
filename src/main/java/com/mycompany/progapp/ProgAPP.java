/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.progapp;

import java.util.Scanner;

/**p
 *
 * @author orifh
 */


public class ProgAPP {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RegisterConsole reg = new RegisterConsole();

        boolean running = true;

        while (running) {
            System.out.println("\n*** MENU ***");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Choose an option: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    registerUser(input, reg);
                    break;
                case "2":
                    // QuickChat only opens after a successful login
                    if (loginUser(input, reg)) {
                        quickChat(input);
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

    static void registerUser(Scanner input, RegisterConsole reg) {
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
            System.out.println(reg.usernameMessage(username));
            if (reg.checkUserName(username)) {
                break;
            }
        }

        String password;
        while (true) {
            System.out.println("Enter password: ");
            password = input.nextLine();
            System.out.println("********************");
            System.out.println(reg.passwordMessage(password));
            if (reg.checkPasswordComplexity(password)) {
                break;
            }
        }

        String cellNumber;
        while (true) {
            System.out.println("Enter South African cell phone number (e.g. +27838968976): ");
            cellNumber = input.nextLine();
            System.out.println("********************");
            System.out.println(reg.cellNumberMessage(cellNumber));
            if (reg.checkCellPhoneNumber(cellNumber)) {
                break;
            }
        }

        System.out.println(reg.registerUser(firstName, lastName, username, password, cellNumber));
    }

    // returns true if the login worked
    static boolean loginUser(Scanner input, RegisterConsole reg) {
        System.out.println("Enter username: ");
        String username = input.nextLine();
        System.out.println("********************");

        System.out.println("Enter password: ");
        String password = input.nextLine();
        System.out.println("********************");

        boolean success = reg.loginUser(username, password);
        System.out.println(reg.returnLoginStatus(success));
        return success;
    }

    static void quickChat(Scanner input) {
        Message msg = new Message();

        System.out.println("Welcome to QuickChat.");

        // keeps asking until the user types a whole number
        int maxMessages = -1;
        while (maxMessages < 0) {
            System.out.println("How many messages do you wish to enter? ");
            String text = input.nextLine();
            try {
                maxMessages = Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }

        int entered = 0;
        boolean chatting = true;

        while (chatting) {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("Choose an option: ");
            String option = input.nextLine();

            switch (option) {
                case "1":
                    if (entered >= maxMessages) {
                        System.out.println("You have already entered the " + maxMessages + " message(s) you set.");
                    } else {
                        sendOneMessage(input, msg);
                        entered++;
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    chatting = false;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, or 3.");
                    break;
            }
        }

        System.out.println("\n===== MESSAGES SENT =====");
        System.out.println(msg.printMessages());
        System.out.println("Total messages sent: " + msg.returnTotalMessages());
    }

    static void sendOneMessage(Scanner input, Message msg) {
        String id = msg.generateMessageID();
        System.out.println(msg.returnMessageIDStatus(id));

        // keeps asking until the recipient number is valid
        String recipient;
        while (true) {
            System.out.println("Enter recipient cell number (e.g. +27718693002): ");
            recipient = input.nextLine();
            System.out.println(msg.checkRecipientCell(recipient));
            if (msg.isValidCell(recipient)) {
                break;
            }
        }

        // keeps asking until the message is 250 characters or less
        String text;
        while (true) {
            System.out.println("Enter your message: ");
            text = input.nextLine();
            System.out.println(msg.checkMessageLength(text));
            if (msg.isMessageLengthOk(text)) {
                break;
            }
        }

        String hash = msg.createMessageHash(id, msg.returnTotalMessages(), text);

        // keeps asking until the user picks 1, 2 or 3
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

        System.out.println(msg.sentMessage(Integer.parseInt(choice)));

        if (choice.equals("1")) {
            msg.addSentMessage(id, hash, recipient, text);
            System.out.println("Message ID: " + id);
            System.out.println("Message Hash: " + hash);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + text);
        } else if (choice.equals("2")) {
            String zero = input.nextLine();
            if (zero.trim().equals("0")) {
                System.out.println("Message deleted.");
            } else {
                System.out.println("Message was not deleted, but it was not sent either.");
            }
        } else {
            msg.storeMessage(id, hash, recipient, text);
        }
    }
}