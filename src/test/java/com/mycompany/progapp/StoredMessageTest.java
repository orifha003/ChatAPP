/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progapp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Part 3: the message arrays, searches, delete and report.
 * The test data comes from the assignment.
 */
public class StoredMessageTest {

    private Message message;

    // test data messages 1 to 5 from the assignment
    // (the IDs are made up, except message 4 where the assignment gives 0838884567)
    private final String[] ids = {"1000000001", "1000000002", "1000000003", "0838884567", "1000000005"};
    private final String[] recipients = {"+27834557896", "+27838884567", "+27834484567", "Developer", "+27838884567"};
    private final String[] texts = {
        "Did you get the cake?",
        "Where are you? You are late! I have asked you to be on time.",
        "Yohoooo, I am at your gate.",
        "It is dinner time!",
        "Ok, I am leaving without you."
    };
    private final String[] flags = {Message.SENT, Message.STORED, Message.DISREGARD, Message.SENT, Message.STORED};
    private final String[] hashes = new String[5];

    // the arrays are filled from the test data with a loop, nothing is hard-coded in the app
    @BeforeEach
    public void setUp() {
        message = new Message("kyl_1");
        for (int i = 0; i < ids.length; i++) {
            hashes[i] = message.createMessageHash(ids[i], i, texts[i]);
            message.addMessage(ids[i], hashes[i], recipients[i], texts[i], flags[i]);
        }
    }

    //  arrays correctly populated 

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(Arrays.asList("Did you get the cake?", "It is dinner time!"),
                message.getSentMessages());
    }

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        assertEquals(Arrays.asList("Yohoooo, I am at your gate."), message.getDisregardedMessages());
    }

    @Test
    public void testStoredMessagesArrayPopulated() {
        assertEquals(Arrays.asList(texts[1], texts[4]), message.getStoredMessages());
    }

    @Test
    public void testMessageIDArrayPopulated() {
        assertEquals(Arrays.asList(ids), message.getMessageIDs());
    }

    @Test
    public void testMessageHashArrayPopulated() {
        assertEquals(Arrays.asList(hashes), message.getMessageHashes());
        // each hash starts with the first two digits of its message ID
        for (int i = 0; i < ids.length; i++) {
            assertTrue(message.getMessageHashes().get(i).startsWith(ids[i].substring(0, 2)));
        }
    }

    // longest message, search, delete, report

    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                message.returnLongestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        assertEquals("It is dinner time!", message.searchMessageByID("0838884567"));
        assertEquals("Developer", message.searchRecipientByID("0838884567"));
    }

    @Test
    public void testSearchForUnknownMessageID() {
        assertEquals("Message ID not found.", message.searchMessageByID("9999999999"));
    }

    @Test
    public void testSearchMessagesForRecipient() {
        ArrayList<String> found = message.searchByRecipient("+27838884567");
        assertEquals(Arrays.asList(
                "Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."), found);
    }

    @Test
    public void testSearchRecipientWithNoMessages() {
        assertEquals(0, message.searchByRecipient("+27000000000").size());
    }

    @Test
    public void testDisregardedMessagesAreNotInRecipientSearch() {
        // message 3 was disregarded, so it must not be found
        assertEquals(0, message.searchByRecipient("+27834484567").size());
    }

    @Test
    public void testDeleteMessageUsingHash() {
        String result = message.deleteByMessageHash(hashes[1]);
        assertEquals("Message \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                result);

        // the message is gone from every array and the others are untouched
        assertEquals(1, message.getStoredMessages().size());
        assertEquals(2, message.getSentMessages().size());
        assertEquals(4, message.getMessageHashes().size());
        assertEquals(4, message.getMessageIDs().size());
    }

    @Test
    public void testDeleteMessageWithUnknownHash() {
        assertEquals("Message hash not found.", message.deleteByMessageHash("00:0:NOTHING"));
        assertEquals(5, message.getMessageHashes().size());
    }

    @Test
    public void testDisplayReportOfSentMessages() {
        String report = message.displayReport(Message.SENT);
        assertTrue(report.contains(hashes[0]));
        assertTrue(report.contains(recipients[0]));
        assertTrue(report.contains(texts[0]));
        assertTrue(report.contains(hashes[3]));
        assertTrue(report.contains(texts[3]));
        // stored and disregarded messages are not in the sent report
        assertFalse(report.contains(texts[1]));
        assertFalse(report.contains(texts[2]));
    }

    @Test
    public void testSenderAndRecipientOfStoredMessages() {
        assertEquals("Sender: kyl_1, Recipient: +27838884567\nSender: kyl_1, Recipient: +27838884567",
                message.displayStoredSenderAndRecipient());
    }

    //  JSON file 

    // stores the two stored test messages in a JSON file
    private Message writeStoredTestMessages(String fileName) {
        Message writer = new Message("kyl_1");
        writer.jsonFileName = fileName;
        writer.storeMessage(ids[1], hashes[1], recipients[1], texts[1]);
        writer.storeMessage(ids[4], hashes[4], recipients[4], texts[4]);
        return writer;
    }

    @Test
    public void testReadJsonFileIntoStoredArray() {
        String fileName = "test_read_messages.json";
        writeStoredTestMessages(fileName);

        Message reader = new Message("kyl_1");
        reader.jsonFileName = fileName;
        reader.loadStoredMessages();

        assertEquals(Arrays.asList(texts[1], texts[4]), reader.getStoredMessages());
        assertEquals(Arrays.asList(hashes[1], hashes[4]), reader.getMessageHashes());
        assertEquals(Arrays.asList(ids[1], ids[4]), reader.getMessageIDs());

        new File(fileName).delete();
    }

    @Test
    public void testJsonKeepsQuotesInMessage() {
        String fileName = "test_quotes_messages.json";
        Message writer = new Message("kyl_1");
        writer.jsonFileName = fileName;
        writer.storeMessage("1000000007", "10:0:SAYHI", "+27838884567", "Say \"hi\" to Sam");

        Message reader = new Message("kyl_1");
        reader.jsonFileName = fileName;
        reader.loadStoredMessages();

        assertEquals(Arrays.asList("Say \"hi\" to Sam"), reader.getStoredMessages());
        new File(fileName).delete();
    }

    @Test
    public void testReadMissingJsonFileGivesEmptyArray() {
        Message reader = new Message("kyl_1");
        reader.jsonFileName = "file_that_does_not_exist.json";
        reader.loadStoredMessages();
        assertEquals(0, reader.getStoredMessages().size());
    }

    @Test
    public void testDeletingStoredMessageUpdatesJsonFile() {
        String fileName = "test_delete_messages.json";
        Message writer = writeStoredTestMessages(fileName);
        writer.deleteByMessageHash(hashes[1]);

        Message reader = new Message("kyl_1");
        reader.jsonFileName = fileName;
        reader.loadStoredMessages();

        assertEquals(Arrays.asList(texts[4]), reader.getStoredMessages());
        new File(fileName).delete();
    }
}