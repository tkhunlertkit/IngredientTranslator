package Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import core.KeyMaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class KeyMasterTest {

    private KeyMaster k;
    private String tempFileName = "blah.txt";

    @Before
    public void setup() {
        try {
            new File(tempFileName).createNewFile();
            k = new KeyMaster(tempFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        new File(tempFileName).delete();
    }

    @Test
    public void testAddNonDublyAddsOneKey() {
        assertTrue("Cannot add to empty KeyMaster", k.addNewKey("fist", "blah", false));
        assertEquals(1, k.getAllKeys().size());
    }

    @Test
    public void testAddExistedKeys() {
        assertTrue("Cannot add to empty KeyMaster", k.addNewKey("fist", "blah", false));
        assertFalse("Duplicate Add Successful", k.addNewKey("fist", "blah", false));

    }

    @Test
    public void testImportFile() {
        try {
            k.importFile("test1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals("Not all keys imported", 171, k.getAllKeys().size());
    }

    @Test
    public void testImportAddOneThenExport() {
        try {
            k.importFile("test1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        k.addNewKey("test", "resteasete", false);
        k.export(tempFileName);
        try {
            k = new KeyMaster(tempFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals("new value is not correct", "resteasete", k.translate("test"));
        assertEquals("new value is not exported", 172, k.getAllKeys().size());
    }

    @Test
    public void testExport() {
        KeyMaster defaultKeyMaster = null;
        try {
            defaultKeyMaster = new KeyMaster("test1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        defaultKeyMaster.export(tempFileName);
        assertTrue("Not importing correctly from test1.txt", defaultKeyMaster.getAllKeys().size() > 0);
        try {
            k = new KeyMaster(tempFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals("Export to a different file not working",defaultKeyMaster.getAllKeys().size(), k.getAllKeys().size());
    }

    @Test
    public void testUpdateAddToFile() {
        KeyMaster km = null;
        try {
            km = new KeyMaster("test1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        km.export(tempFileName);
        KeyMaster km2 = null;
        try {
            km2 = new KeyMaster(tempFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int countBeforeUpdate = km2.getAllKeys().size();
        assertTrue("Cannot add to database", km2.addNewKey("newKey", "newVal", true));
        KeyMaster km3 = null;
        try {
            km3 = new KeyMaster(tempFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals("Did not write out to dict", countBeforeUpdate + 1, km3.getAllKeys().size());
    }

    @Test
    public void testValidImportFormatInvalidKeyInvalidValue() {
        String text = ",";
        try(Scanner s = new Scanner(text)) {
            k.importWithScanner(s, false);
        }
        assertEquals("Added empty key and empty value", 0, k.getAllKeys().size());
    }

    @Test
    public void testValidImportFormatInvalidKeyValidValue() {
        String text = ",value";
        try(Scanner s = new Scanner(text)) {
            k.importWithScanner(s, false);
        }
        assertEquals("Added empty key and non-empty value", 0, k.getAllKeys().size());
    }

    @Test
    public void testValidImportFormatValidKeyInvalidValue() {
        String text = "key,";
        try(Scanner s = new Scanner(text)) {
            k.importWithScanner(s, false);
        }
        assertEquals("Added empty key and non-empty value", 0, k.getAllKeys().size());
    }

    @Test
    public void testIllFormattedEntryThatWorks() {
        String text = " key , value    ";
        k.importWithScanner(new Scanner(text), false);
        assertEquals("Space after comma does not add to dictionary", 1, k.getAllKeys().size());
        assertEquals("Space after comma is included as part of the value", "value", k.translate("key"));
        assertTrue("key is not added to the dictionary", k.getAllKeys().contains("key"));
    }

    @Test
    public void testIllFormattedEntryBreaks() {
        String text = ", key , value    ";
        k.importWithScanner(new Scanner(text), false);
        assertEquals("2 commas: one in front of key, one after, in an entry and still adds", 0, k.getAllKeys().size());
        text = "key,,values";
        assertEquals("2 commas: two in between key and value in an entry and still adds", 0, k.getAllKeys().size());
        text = "key,values, ";
        assertEquals("2 commas: one int between and one after value in an entry and still adds", 0, k.getAllKeys().size());
    }

}
