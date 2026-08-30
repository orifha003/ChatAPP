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
    // stores the registered details so Login can check against them
    static String registeredUsername = null;
    static String registeredPassword = null;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RegisterConsole reg = new RegisterConsole();

        boolean running = true;

        while (running) {
            System.out.println("\n**** MENU ****");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                registerUser(input, reg);
            } else if (choice.equals("2")) {
                loginUser(input);
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                running = false;
            } else {
                System.out.println("Invalid option, please choose 1, 2, or 3.");
            }
        }
    }

    static void registerUser(Scanner input, RegisterConsole reg) {
        boolean usernameOk= false ;
        boolean passwordOk= false ;
        boolean cellNumberOk= false;

        System.out.println("Enter username: ");
        String username = input.nextLine();

        if (reg.checkUserName(username)) {
            System.out.println("Username successfully captured.");
            usernameOk = true;
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            
        }

        System.out.println("Enter password: ");
        String password = input.nextLine();

        if (reg.checkPassword(password)) {
            System.out.println("Password successfully captured.");
            passwordOk = true;
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            
        }

        System.out.println("Enter South African cell phone number (e.g. +27123456789): ");
        String cellNumber = input.nextLine();

        if (reg.checkCellNumber(cellNumber)) {
            System.out.println("Cell phone number successfully added.");
            cellNumberOk = true;
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            
        }

        if (usernameOk && passwordOk && cellNumberOk) {
            System.out.println("Welcome, " + username + "! Registration successful.");
            registeredUsername = username;
            registeredPassword = password;
        } else {
            System.out.println("Registration failed. Please correct the errors above and try again.");
        }
    }

    static void loginUser(Scanner input) {
        if (registeredUsername == null) {
            System.out.println("No account has been registered yet. Please register first.");
            return;
        }

        System.out.println("Enter username: ");
        String username = input.nextLine();

        System.out.println("Enter password: ");
        String password = input.nextLine();

        if (username.equals(registeredUsername) && password.equals(registeredPassword)) {
            System.out.println("Login successful. Welcome back, " + username + "!");
        } else {
            System.out.println("USERNAME OR PASSWORD INCORRECT, PLEASE TRY AGAIN.");
        }
    }

}
