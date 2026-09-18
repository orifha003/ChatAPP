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
        
        //DECLARATION OF VARIABLES 
        Scanner input = new Scanner(System.in);
        RegisterConsole reg = new RegisterConsole();

        boolean running = true;

        // THE MAIN MENU 
        while (running) {
            System.out.println("\n***** MENU *****");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Choose an option: ");
            String choice = input.nextLine();
            System.out.println("********************");

            //GET INPUT AND PROCESS  
            switch (choice) {
                case "1":
                    registerUser(input, reg);
                    break;
                case "2":
                    loginUser(input, reg);
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

    //REGISTRATION INPUT PROCESS 
        static void registerUser(Scanner input, RegisterConsole reg) {
            
    //PROMT USER TO ENTER FISRT NAME
        System.out.println("Enter first name: ");
        String firstName = input.nextLine();

    //PROMPT USER TO ENTER LAST NAME 
        System.out.println("Enter last name: ");
        String lastName = input.nextLine();

    //EXECUTION OF LOOP FOR USERNAME 
        String username;
        while (true) {
            System.out.println("Enter username: ");
            username = input.nextLine();
            if (reg.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

    //EXECUTING OF LOOP FOR PASSWORD 
        String password;
        while (true) {
            System.out.println("Enter password: ");
            password = input.nextLine();
            if (reg.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }

    //EXECUTION OF LOOP FOR CELLPHONE NUMBER 
        String cellNumber;
        while (true) {
            System.out.println("Enter South African cell phone number (e.g. +27123456789): ");
            cellNumber = input.nextLine();
            if (reg.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell number successfully captured.");
                break;
            } else {
                System.out.println("Cell number is incorrectly formatted or does not contain international code; please correct the number and try again.");
            }
        }

    // DIPLAY OUTPUT MESSAGE
        String result = reg.registerUser(firstName, lastName, username, password, cellNumber);
        System.out.println(result);
    }
        
        static void loginUser(Scanner input, RegisterConsole reg) {
            System.out.println("Enter username: ");
            String username = input.nextLine();
            System.out.println("********************");

            System.out.println("Enter password: ");
            String password = input.nextLine();
            System.out.println("********************");

            boolean success = reg.loginUser(username, password);
            String status = reg.returnLoginStatus(success);
            System.out.println(status);  
        }
    }