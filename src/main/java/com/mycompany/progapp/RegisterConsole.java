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
   boolean checkUserName(String username) {
        boolean hasUnderscore = false;
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (c == '_') hasUnderscore = true;
        }
        boolean validLength = username.length() <= 5;
        return hasUnderscore && validLength;
    }

    boolean checkPassword(String password) {
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

    boolean checkCellNumber(String number) {
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
   
}
