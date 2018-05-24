package handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;

/**
 * Tests for the information handler class.
 */
public class InformationHandlerTest {

    /**
     * Test if the constructor does what it needs to do.
     */
    @Test
    void testConstructor() {
        InformationHandler handler = new InformationHandler();
        assertNotNull(handler.getQueue());
    }

    /**
     * Test empty information queue will return empty.
     */
    @Test
    void testGetInformationEmpty() {
        InformationHandler handler = new InformationHandler();
        assertEquals("empty", handler.getInformation());
    }

    /**
     * Test get information gets added information.
     */
    @Test
    void testGetInformation() {
        InformationHandler handler = new InformationHandler();
        handler.addInformation("ajax");
        assertEquals("ajax", handler.getInformation());
    }
}
