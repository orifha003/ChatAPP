/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progapp;
import com.mycompany.progapp.RegisterConsole;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 *
 * @author orifh
 */
public class RegisterConsoleTest {
     private RegisterConsole reg;

    @BeforeEach
    public void setUp() {
        reg = new RegisterConsole();
    }

    // assertEquals tests

    @Test
    public void testUsernameCorrectlyFormattedMessage() {
        assertEquals("Username successfully captured.", reg.usernameMessage("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormattedMessage() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                reg.usernameMessage("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexityMessage() {
        assertEquals("Password successfully captured.", reg.passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexityMessage() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                reg.passwordMessage("password"));
    }

    @Test
    public void testCellNumberCorrectlyFormattedMessage() {
        assertEquals("Cell number successfully captured.", reg.cellNumberMessage("+27838968976"));
    }

    @Test
    public void testCellNumberIncorrectlyFormattedMessage() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                reg.cellNumberMessage("08966553"));
    }

    @Test
    public void testRegisterUserSuccessMessage() {
        assertEquals("You have registered successfully.",
                reg.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testRegisterUserFailMessage() {
        assertEquals("Unable to register.",
                reg.registerUser("Kyle", "Smith", "kyle!!!!!!!", "password", "08966553"));
    }

    @Test
    public void testWelcomeMessageAfterLogin() {
        reg.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        boolean success = reg.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", reg.returnLoginStatus(success));
    }

    @Test
    public void testFailedLoginMessage() {
        reg.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        boolean success = reg.loginUser("kyl_1", "wrongpass");
        assertEquals("Username or password incorrect, please try again.", reg.returnLoginStatus(success));
    }

    // assertTrue / assertFalse tests 

    @Test
    public void testLoginSuccessful() {
        reg.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(reg.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        reg.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(reg.loginUser("kyl_1", "wrongpass"));
    }

    @Test
    public void testUsernameCorrectlyFormatted() {
        assertTrue(reg.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        assertFalse(reg.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity() {
        assertTrue(reg.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        assertFalse(reg.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellNumberCorrectlyFormatted() {
        assertTrue(reg.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellNumberIncorrectlyFormatted() {
        assertFalse(reg.checkCellPhoneNumber("08966553"));
    }
}

