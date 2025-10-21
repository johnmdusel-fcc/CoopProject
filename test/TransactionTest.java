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
            "Vernon"
        );
        myRent = new Rent(id);
        myReturn = new Return(id);
    }

    @Test 
    void testConstructorDataValidation() {
        Exception e = assertThrows(
            IllegalArgumentException.class,
            () -> {new Rent(null);}
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
    void testExecuteCorrectness() {
        myRent.execute(tool);
        assertEquals(
            RentalItemState.RENTED, // expected
            tool.getAvailability() // actual
        );
        myReturn.execute(tool);
        assertEquals(
            RentalItemState.AVAILABLE,
            tool.getAvailability()
        );
    }

} // end: class TransactionTest
