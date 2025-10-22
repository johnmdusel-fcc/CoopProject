import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoopTest {

    private Coop coop;
    private RentalItem tool;
    private RentalItem vehicle;

    @BeforeEach
    void setUp() {
        coop = new Coop();
        tool = new RentalTool(
            "d437b9",
            "Vernon",
            RentalItemState.AVAILABLE
        );
        vehicle = new RentalVehicle(
            "a9dad5",
            "Molly",
            RentalItemState.AVAILABLE
        );
    }

    // phase 1

    @Test
    void testAddRentalItem() {
        // add item tht's absent from coop rentalItems
        boolean addItemResult = coop.addRentalItem(tool);
        assertEquals(
            true,  // expected value
            addItemResult,  // actual value
            "addRentalItem should return true"
        );
        assertEquals(
            1,
            coop.getInventoryCount(),
            "Invetory count should be 1."
        );

        // add item that's present in coop rentalItems
        addItemResult = coop.addRentalItem(tool);
        assertEquals(
            false,
            addItemResult,
            "addRentalItem should return false"
        );
        assertEquals(
            1,
            coop.getInventoryCount(),
            "Invetory count should still be 1."
        );
    }

    @Test
    void testAddRentalItemOverflow() {
        for (
            int idx = 0; 
            idx <= 100; // rentalItems array will overflow at idx == 100
            idx++
        ) {
            Integer uid = idx;  // need unique id for each
            coop.addRentalItem(
                new RentalTool(
                    uid.toString(),
                    "Dusel",
                    RentalItemState.AVAILABLE
                )
            );
        }
        assertEquals(
            101,
            coop.getInventoryCount(),
            "Inventory count should be 101."
        );
        assertEquals(
            coop.getRentalItems().length,
            200,
            "Extended rentalItems should have length 200."
        );
    }

    @Test
    void testFind() {
        // find items present in coop's rentalItems
        coop.addRentalItem(tool);
        coop.addRentalItem(vehicle);
        assertEquals(
            coop.find(tool.getUid()),
            0,
            "tool should be at index 0."
        );
        assertEquals(
            coop.find(vehicle.getUid()),
            1,
            "vehicle should be at index 1."
        );

        // find item absent from coop's rentalItems
        // --> implicitly tested by testAddRentalItem
        assertEquals(
            coop.find("0"),
            -1,
            "absent item should return index -1."
        );
    }

    // phase 2

    @Test
    void testLoadRentalItems() {
        String rentalItemsFilename = "data/testrentalitems.csv";
        boolean result = coop.loadRentalItems(rentalItemsFilename);
        assertEquals(
            true,
            result,
            "Result of loading inventory should be true."
        );
        RentalItem[] rentalItems = coop.getRentalItems();
        assertEquals(
            2,
            coop.getInventoryCount(),
            "Inventory should contain 2 items."
        );

        // check validity of stored rental item
        RentalItem rentalItem = rentalItems[0];
        assertEquals(
            "rs599476",
            rentalItem.getUid(),
            "Rental item UID must equal rs599476"
        );
        assertEquals(
            "Al Davis",
            rentalItem.getOwner(),
            "Rental item owner must equal Al Davis"
        );
        assertEquals(
            RentalItemType.TOOL,
            rentalItem.getItemType(),
            "Rental item type must equal RentalItemType.TOOL"
        );

        rentalItem = rentalItems[1];
        assertEquals(
            "ca696497",
            rentalItem.getUid(),
            "Rental item UID must equal ca696497"
        );
        assertEquals(
            "Thundercat",
            rentalItem.getOwner(),
            "Rental item owner must equal Thundercat"
        );
        assertEquals(
            RentalItemType.VEHICLE,
            rentalItem.getItemType(),
            "Rental item type must equal RentalItemType.VEHICLE"
        );
    } // end: testLoadRentalItems

    @Test
    void testWriteRentalItems() {
        String rentalItemsFilename = "test/testrentalitems.csv";
        coop.loadRentalItems(rentalItemsFilename); // already tested
        
        // write
        rentalItemsFilename = "test/testrentalitems-out.csv";
        coop.writeRentalItems(rentalItemsFilename);

        // reload and compare
        Coop coopReload = new Coop();
        coopReload.loadRentalItems(rentalItemsFilename);
        
        assertEquals(
            coop.getInventoryCount(),
            coopReload.getInventoryCount(),
            "Inventory counts should match."
        );

        RentalItem[] coopInventory = coop.getRentalItems();
        RentalItem[] coopReloadInventory = coop.getRentalItems();
        for (int idx = 0; idx < coop.getInventoryCount(); idx++) {
            assertEquals(
                coopInventory[idx],
                coopReloadInventory[idx],
                String.format("Rental item %s should match.", idx)
            );
        }
    }

    // phase 3

    @Test 
    void testProcessTransactionsFailure() {
        assertEquals(
            0,
            coop.processTransactions("invalid.csv")
        );
    }

    @Test
    void testProcessTransactionsSuccess() {
        assertEquals(
            3,
            coop.processTransactions("data/testtransactions.csv")
        );
    }

}  // end: class CoopTest
