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

    //DECLARATION OF VARIABLES 
        String storedUsername;
        String storedPassword;
        String storedCellNumber;
        String storedFirstName;
        String storedLastName;
    
    //CHECKING USERNAME REQUIREMENTS
        boolean checkUserName(String username) {
            boolean hasUnderscore = false;
            for (int i = 0; i < username.length(); i++) {
                char c = username.charAt(i);
                if (c == '_') hasUnderscore = true;
            }
            boolean validLength = username.length() <= 5;
            return hasUnderscore && validLength;
    }
        
    //CHECKING PASSWORD REQUIREMENTS
        boolean checkPasswordComplexity(String password) {
            boolean hasUpper = false;
            boolean hasLower = false;
            boolean hasDigit = false;
            boolean hasSpecial = false;
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (Character.isUpperCase(c)) hasUpper = true;
                if (Character.isLowerCase(c)) hasLower = true;
                if (Character.isDigit(c)) hasDigit = true;
                if (c == '@' || c == '.' || c == '!' || c == '_') hasSpecial = true;
            }
        return password.length() >= 8 && hasUpper && hasLower && hasDigit && hasSpecial;
    }
        
        //CHECK CELL NUMBER REQUIREMENTS  
            boolean checkCellPhoneNumber(String number) {
                boolean startsWithPlus = number.length() > 0 && number.charAt(0) == '+';
                boolean allDigits = true;
                for (int i = 1; i < number.length(); i++) {
                    char c = number.charAt(i);
                    if (!Character.isDigit(c)) {
                        allDigits = false;
                    }
                }
                boolean validLength = (number.length() - 1) <= 10;
                return startsWithPlus && allDigits && validLength;
            }
           
        //DISPLAY OUTPUT MESSAGES FOR REGISTRATION 
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
            
         //DISPLAY OUTPUT MESSAGES FOR LOGIN 
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