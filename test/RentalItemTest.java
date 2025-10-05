import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RentalItemTest {

    private RentalItem tool;

    @BeforeEach
    void setUp() {
        tool = new RentalTool(
            "d437b9",
            "Vernon"
        );
    }

    @Test
    void testDefaultValues() {
        assertEquals(
            RentalItemState.AVAILABLE, // expected value
            tool.getAvailability(),  // actual value
            "New RentalItem should be AVAILABLE."
        );
    }

    @Test
    void testConstructorDataValidation() {
        // null uid
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> {new RentalTool(null, "Dusel");}
        );
        assertEquals(
            "Parameter id cannot be null.",
            exception.getMessage()
        );

        // null owner
        exception = assertThrows(
            IllegalArgumentException.class,
            () -> {new RentalTool("0", null);}
        );
        assertEquals(
            "Parameter owner cannot be null.",
            exception.getMessage()
        );
    } // end: testConstructorDataValidation

    @Test
    void testMakeThrowsOnNullInput() {
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> {RentalItem.make(null);}
        );
        assertEquals(
            "Parameter inputLine cannot be null.",
            exception.getMessage()
        );
    }

    @Test
    void testMakePreservesData() {
        String inputLine = "tool,xf123456,Lorenzo de Medici";
        RentalItem rentalItem = RentalItem.make(inputLine);
        assertEquals(
            "xf123456",
            rentalItem.getUid(),
            "Rental item UID must equal xf123456"
        );
        assertEquals(
            "Lorenzo de Medici",
            rentalItem.getOwner(),
            "Rental item owner must equal Lorenzo de Medici"
        );
        assertEquals(
            RentalItemType.TOOL,
            rentalItem.getItemType(),
            "Rental item type must equal RentalItemType.TOOL"
        );
    }

    @Test
    void testToCSV() {
        assertEquals(
            "tool,d437b9,Vernon",
            tool.toCSV(),
            "tool's CSV string should be tool,d437b9,Vernon."
        );
    }
}
