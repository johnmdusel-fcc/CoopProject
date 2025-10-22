import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    private RentalItem tool;
    private Transaction myRent, myReturn;

    @BeforeEach
    void setUp() {
        String id = "d437b9";
        tool = new RentalTool(
            id,
            "Vernon",
            RentalItemState.AVAILABLE
        );
        myRent = new Rental(id);
        myReturn = new Return(id);
    }

    // phase 3

    @Test 
    void testConstructorDataValidation() {
        Exception e = assertThrows(
            IllegalArgumentException.class,
            () -> {new Rental(null);}
        );
        assertEquals(
            "Parameter id cannot be null.",
            e.getMessage()
        );
    }

    @Test
    void testMakeThrowsOnNullInput() {
        Exception e = assertThrows(
            IllegalArgumentException.class,
            () -> {Transaction.make(null);}
        );
        assertEquals(
            "Parameter inputLine cannot be null.",
            e.getMessage()
        );
    }

    @Test
    void testMakePreservesData() {
        String inputLine = "rental,rp332960";
        Transaction newTrans = Transaction.make(inputLine);
        assertEquals(
            "rp332960",
            newTrans.getItemID()
        );
    }


    @Test
    void testValidationAndExecuteCorrectness() {
        // tool is initialized as available
        assertEquals( // check rental can execute on tool
            true, // expected
            myRent.validate(tool) // actual
        );
        assertEquals( // check return cannot execute on tool
            false,
            myReturn.validate(tool)
        );

        myRent.execute(tool); // tool is now rented
        assertEquals( // check rental cannot execute on tool
            false,
            myRent.validate(tool)
        );
        assertEquals( // check return can execute on tool
            true,
            myReturn.validate(tool)
        );
        assertEquals( // check tool is now rented
            RentalItemState.RENTED,
            tool.getAvailability()
        );

        myReturn.execute(tool);
        assertEquals( // check tool is now available
            RentalItemState.AVAILABLE,
            tool.getAvailability(),
            "Tool has been returned."
        );
    }

} // end: class TransactionTest
