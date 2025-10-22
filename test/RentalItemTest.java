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
            "Vernon",
            RentalItemState.AVAILABLE
        );
    }

    // phase 1

    @Test
    void testConstructorDataValidation() {
        // null uid
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> {new RentalTool(null, "Dusel", RentalItemState.AVAILABLE);}
        );
        assertEquals(
            "Parameter id cannot be null.",
            exception.getMessage()
        );

        // null owner
        exception = assertThrows(
            IllegalArgumentException.class,
            () -> {new RentalTool("0", null, RentalItemState.AVAILABLE);}
        );
        assertEquals(
            "Parameter owner cannot be null.",
            exception.getMessage()
        );

        // null availability
        exception = assertThrows(
            IllegalArgumentException.class,
            () -> {new RentalTool("0", "Dusel", null);}
        );
        assertEquals(
            "Parameter state cannot be null.",
            exception.getMessage()
        );
    } // end: testConstructorDataValidation

    // phase 2

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
        String inputLine = "tool,xf123456,Lorenzo de Medici,available";
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
        assertEquals(
            RentalItemState.AVAILABLE,
            rentalItem.getAvailability()
        );
    }

    @Test
    void testToCSV() {
        String expected = "tool,d437b9,Vernon,available";
        assertEquals(
            expected,
            tool.toCSV(),
            "tool's CSV string should be " + expected
        );
    }

    // phase 3

    @Test
    void testCheckInAndCheckOut() {
        // tool starts out being available
        assertEquals(
            RentalItemState.AVAILABLE,
            tool.getAvailability()
        );

        // checked-out tool is rented
        tool.checkOut();
        assertEquals(
            RentalItemState.RENTED,
            tool.getAvailability()
        );

        // checking in reverts to available
        tool.checkIn();
        assertEquals(
            RentalItemState.AVAILABLE,
            tool.getAvailability()
        );
    }
}
