/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progapp;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author orifh
 */



public class MessageTest {

    private Message msg;

    @BeforeEach
    public void setUp() {
        msg = new Message();
    }

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.",
                msg.checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "a".repeat(255);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                msg.checkMessageLength(longMessage));
    }

    @Test
    public void testRecipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.",
                msg.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.checkRecipientCell("08575975889"));
    }

    @Test
    public void testMessageHashTestCase1() {
        assertEquals("00:0:HITONIGHT",
                msg.createMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessageHashesInLoop() {
        String[] messages = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"
        };
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], msg.createMessageHash("0012345678", i, messages[i]));
        }
    }

    @Test
    public void testMessageIDCreated() {
        String id = msg.generateMessageID();
        assertEquals(10, id.length());
        assertTrue(msg.checkMessageID(id));
        assertEquals("Message ID generated: " + id, msg.returnMessageIDStatus(id));
    }

    @Test
    public void testSentMessageSend() {
        assertEquals("Message successfully sent.", msg.sentMessage(1));
    }

    @Test
    public void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
    }

    @Test
    public void testSentMessageStore() {
        assertEquals("Message successfully stored.", msg.sentMessage(3));
    }

    @Test
    public void testTotalMessagesSent() {
        // message 1 is sent, message 2 is discarded, so only one is counted
        String hash = msg.createMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight?");
        msg.addSentMessage("0012345678", hash, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals(1, msg.returnTotalMessages());
    }
}
