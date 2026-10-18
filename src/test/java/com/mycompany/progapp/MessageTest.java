/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.progapp;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    private Message message;

    // test data from the assignment
    private final String messageOne = "Hi Mike, can you join us for dinner tonight?";
    private final String messageTwo = "Hi Keegan, did you receive the payment?";
    private final String knownID = "0012345678";

    @BeforeEach
    public void setUp() {
        message = new Message();
    }

    //  message length 
    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message.checkMessageLength(messageOne));
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "a".repeat(255);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                message.checkMessageLength(longMessage));
    }

    //  recipient number 
    @Test
    public void testRecipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.",
                message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell("08575975889"));
    }

    //  message hash 

    @Test
    public void testMessageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", message.createMessageHash(knownID, 0, messageOne));
    }

    @Test
    public void testMessageHashesInLoop() {
        String[] messages = {messageOne, messageTwo};
        String[] expectedHashes = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};

        // the loop counter is the message number
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], message.createMessageHash(knownID, i, messages[i]));
        }
    }

    //  message ID
    @Test
    public void testMessageIDCreated() {
        String messageID = message.generateMessageID();
        assertEquals("Message ID generated: " + messageID, message.returnMessageIDStatus(messageID));
    }

    @Test
    public void testMessageIDsInLoop() {
        // every generated ID must be exactly ten digits
        for (int i = 0; i < 50; i++) {
            String messageID = message.generateMessageID();
            assertEquals(10, messageID.length());
            assertTrue(messageID.matches("[0-9]+"));
            assertTrue(message.checkMessageID(messageID));
        }
    }

    @Test
    public void testMessageIDTooLong() {
        assertFalse(message.checkMessageID("12345678901"));
    }

    // send, disregard, store 
    @Test
    public void testSentMessageSend() {
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    public void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
    }

    @Test
    public void testSentMessageStore() {
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    @Test
    public void testSentMessageInvalidOption() {
        assertEquals("Invalid option.", message.sentMessage(9));
    }

    //  total messages and message details 
    @Test
    public void testTotalMessagesSent() {
        // message 1 is sent, message 2 is discarded, so only one is counted
        message.addSentMessage(knownID, "00:0:HITONIGHT", "+27718693002", messageOne);
        assertEquals(1, message.returnTotalMessages());
    }

    @Test
    public void testTotalMessagesWhenNoneSent() {
        assertEquals(0, message.returnTotalMessages());
    }

    @Test
    public void testPrintMessagesOrder() {
        message.addSentMessage(knownID, "00:0:HITONIGHT", "+27718693002", messageOne);
        String expected = "Message ID: 0012345678\n"
                + "Message Hash: 00:0:HITONIGHT\n"
                + "Recipient: +27718693002\n"
                + "Message: " + messageOne;
        assertEquals(expected, message.printMessages());
    }

    // JSON file 
    @Test
    public void testStoreMessageWritesJsonFile() throws IOException {
        message.jsonFileName = "test_messages.json";
        message.storeMessage(knownID, "00:0:HITONIGHT", "+27718693002", messageOne);

        File jsonFile = new File("test_messages.json");
        assertTrue(jsonFile.exists());

        String content = Files.readString(jsonFile.toPath());
        assertTrue(content.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(content.contains("\"recipient\": \"+27718693002\""));

        jsonFile.delete();
    }
}