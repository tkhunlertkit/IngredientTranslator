package Tests;

import core.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testTypicalPattern() {
        String test = "English | Thai 3/4 Cups";
        assertEquals("Did not get Thai word right", "Thai", Parser.getThaiWord(test));
        assertEquals("Did not get English Word", "English", Parser.getEnglishWord(test));
        assertEquals("Did not get Quantity", "3/4", Parser.getQuantity(test));
        assertEquals("Did not get Unit", "Cups", Parser.getUnit(test));
    }

    @Test
    public void testNoEnglishWord() {
        String test = "Thai";
        assertEquals("Did not get Thai word from Thai only phrase", "Thai", Parser.getThaiWord(test));
        assertTrue("English word not null", Parser.getEnglishWord(test).isEmpty());
        assertTrue("Unit not null", Parser.getUnit(test).isEmpty());
        assertTrue("Quantity not null", Parser.getQuantity(test).isEmpty());
    }

    @Test
    public void testEnglighOnly() {
        String test = "English |";
        assertEquals("Did not get English", "English", Parser.getEnglishWord(test));
        assertTrue("Thai word not null", Parser.getThaiWord(test).isEmpty());
        assertTrue("Unit not null", Parser.getUnit(test).isEmpty());
        assertTrue("Quantity not null", Parser.getQuantity(test).isEmpty());
    }

    @Test
    public void testQuantityOnly() {
        String test = "1 1/4";
        assertEquals("Did not get Quantity", "1 1/4", Parser.getQuantity(test));
        assertTrue("Thai word not null", Parser.getThaiWord(test).isEmpty());
        assertTrue("Unit not null", Parser.getUnit(test).isEmpty());
        assertTrue("English not null", Parser.getEnglishWord(test).isEmpty());
    }

    @Test
    public void testQuantityAndUnits() {
        String test = "1 1/4 Tbsp";
        assertEquals("Did not get Quantity", "1 1/4", Parser.getQuantity(test));
        assertEquals("Unit incorrect", "Tbsp", Parser.getUnit(test));
        assertTrue("Thai word not null", Parser.getThaiWord(test).isEmpty());
        assertTrue("English not null", Parser.getEnglishWord(test).isEmpty());
    }

    @Test
    public void testThaiQuantityUnit() {
        String test = "Thai 1 1/4 Cups";
        assertEquals("Did not get Quantity", "1 1/4", Parser.getQuantity(test));
        assertEquals("Did not get Thai", "Thai", Parser.getThaiWord(test));
        assertEquals("Did not get unit", "Cups", Parser.getUnit(test));
        assertTrue("English not null", Parser.getEnglishWord(test).isEmpty());
    }

    @Test
    public void testEnglishQuantityUnit() {
        String test = "English | 1 1/4 Cups";
        assertEquals("Did not get Quantity", "1 1/4", Parser.getQuantity(test));
        assertEquals("Did not get English", "English", Parser.getEnglishWord(test));
        assertEquals("Did not get unit", "Cups", Parser.getUnit(test));
        assertTrue("Thai not null", Parser.getThaiWord(test).isEmpty());
    }

    @Test
    public void testTrailingSpace() {
        String test = "   Eng    |        Thai  1.23                            tsp   ";
        assertEquals("English word not correct", "Eng", Parser.getEnglishWord(test));
        assertEquals("Thai word not correct", "Thai", Parser.getThaiWord(test));
        assertEquals("Quantity not correct", "1.23", Parser.getQuantity(test));
        assertEquals("Unit not correct", "tsp", Parser.getUnit(test));
    }

}
