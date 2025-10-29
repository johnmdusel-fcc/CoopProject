import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Scanner;

public class AuditTest {

    private String fileName;
    private Audit audit;

    @BeforeEach
    void setup() {
        fileName = "test/audittest.log";
        try {
            audit = new Audit(fileName);
        } catch (IOException e) {e.printStackTrace();}
    }

    @Test
    void testRecordNoSuchItem() {
        Transaction t = new Return("id");
        audit.recordNoSuchItem(t);
        audit.close();

        // compare audit file to expected result
        Scanner scan;
        try {
            scan = new Scanner(new File(fileName));

            // read the transaction line
            assertEquals(true, scan.hasNextLine());
            String line = scan.nextLine();
            assertEquals(
                true,
                // avoiding timestamps
                line.contains("ERROR no such item: " + t.toString())
            );

            // confirm end of file
            assertEquals(false, scan.hasNextLine());
            scan.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    @Test
    void testRecordNonsufficientAvailability() {
        Transaction t = new Rental("id");
        RentalItem ri = new RentalTool(
            "id", "owner", RentalItemState.RENTED
        );
        audit.recordNonsufficientAvailability(t, ri);
        audit.close();

        Scanner scan;
        try {
            scan = new Scanner(new File(fileName));

            assertEquals(true, scan.hasNextLine());
            String line = scan.nextLine();
            assertEquals(
                true,
                line.contains(
                    String.format(
                        "ERROR nonsufficient availability: %s, but item is %s",
                        t.toString(),
                        ri.getAvailability().name()
                    )
                )
            );

            assertEquals(false, scan.hasNextLine());
            scan.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    @Test
    void testRecordExecute() {
        Transaction t = new Rental("id");
        RentalItem ri = new RentalVehicle(
            "id", "owner", RentalItemState.AVAILABLE
        );
        audit.recordExecute(t, ri);
        audit.close();

        Scanner scan;
        try {
            scan = new Scanner(new File(fileName));

            assertEquals(true, scan.hasNextLine());
            String line = scan.nextLine();
            assertEquals(
                true,
                line.contains(
                    String.format(
                    "INFO: %s, item is now %s",
                    t.toString(),
                    ri.getAvailability().name()
                )
                )
            );

            assertEquals(false, scan.hasNextLine());
            scan.close();
        } catch (IOException e) {e.printStackTrace();}
    }

}
