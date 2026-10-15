package com.mycompany.progapp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author orifh
 */

public class RegisterConsole {

    // Declaration of variables
    String storedUsername;
    String storedPassword;
    String storedCellNumber;
    String storedFirstName;
    String storedLastName;

    //check username requirements
    boolean checkUserName(String username) {
        boolean hasUnderscore = false;
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (c == '_') hasUnderscore = true;
        }
        boolean validLength = username.length() <= 5;
        return hasUnderscore && validLength;
    }

    //checking password complexity
    boolean checkPasswordComplexity(String password) {
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (c == '@' || c == '.' || c == '!' || c == '_') hasSpecial = true;
        }
        return password.length() >= 8 && hasUpper && hasDigit && hasSpecial;
    }

    // check cell phone number requirements
    boolean checkCellPhoneNumber(String number) {
        boolean startsWithPlus = number.length() > 1 && number.charAt(0) == '+';
        boolean allDigits = true;
        for (int i = 1; i < number.length(); i++) {
            char c = number.charAt(i);
            if (!Character.isDigit(c)) {
                allDigits = false;
            }
        }
        // + and country code (up to 3 digits) followed by up to 10 digits
        boolean validLength = number.length() <= 13;
        return startsWithPlus && allDigits && validLength;
    }

    // messages used for each field
    String usernameMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    String passwordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    String cellNumberMessage(String number) {
        if (checkCellPhoneNumber(number)) {
            return "Cell number successfully captured.";
        }
        return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    }

    //display output message
    String registerUser(String firstName, String lastName, String username, String password, String cellNumber) {
        boolean usernameOk = checkUserName(username);
        boolean passwordOk = checkPasswordComplexity(password);
        boolean cellOk = checkCellPhoneNumber(cellNumber);

        if (usernameOk && passwordOk && cellOk) {
            // all checks passed, so store the details for login later
            storedFirstName = firstName;
            storedLastName = lastName;
            storedUsername = username;
            storedPassword = password;
            storedCellNumber = cellNumber;

            return "You have registered successfully.";
        } else {
            return "Unable to register.";
        }
    }

    boolean loginUser(String username, String password) {
        if (storedUsername == null) {
            return false;
        }
        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + storedFirstName + ", " + storedLastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}