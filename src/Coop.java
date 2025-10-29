import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Coop {

    final int INIT_ITEMS = 100;
    
    private RentalItem[] rentalItems;
    private int nextIndex;

    public Coop() {
        rentalItems = new RentalItem[INIT_ITEMS];
        nextIndex = 0;
    }

    /**
     * Add a rental item to this Co-op's inventory. 
     * @param item Rental item to be added.
     * @return true if item is successfully added, false otherwise.
     */
    public boolean addRentalItem(RentalItem item) {
        if (find(item.getUid()) == -1) {  // item absent from rentalItems
            if (nextIndex < rentalItems.length) {
                rentalItems[nextIndex] = item;
            } else if (nextIndex == rentalItems.length) {  // increase capacity
                RentalItem[] extendedItems = new RentalItem[
                    rentalItems.length + INIT_ITEMS
                ];
                for (int idx = 0; idx < rentalItems.length; idx++) {
                    extendedItems[idx] = rentalItems[idx];
                }
                extendedItems[nextIndex] = item;
                rentalItems = extendedItems;
            }
            nextIndex++;
            return true;
        } else {  // item present rentalItems
            return false;
        }
    }

    /**
     * Find index of *first occurrence* of the provided rental item in this Co-op's inventory.
     * @param item Rental item to search for.
     * @return Index of rental item, if present. Otherwise -1.
     */
    public int find(String id) {
        for (int idx = 0; idx < nextIndex; idx++) {
            if (
                rentalItems[idx].getUid().equals(id)
            ) {
                return idx;
            }
        } 
        return -1;
    }

    public int getInventoryCount() {
        return nextIndex;
    }

    // Provided because of the need to generate a file of transactions.
    public RentalItem[] getRentalItems() {
        return rentalItems;
    }

    /**
     * Fill this Coop's inventory with rental items, read from CSV file.
     * @param filename Name of source CSV file.
     */
    public boolean loadRentalItems(String filename) {
        File inputFile = new File(filename);
        Scanner scan;
        try{
            scan = new Scanner(inputFile);
            while (scan.hasNextLine()) {
                String csvString = scan.nextLine();
                RentalItem rentalItem = RentalItem.make(csvString);
                addRentalItem(rentalItem);
            }
            scan.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Write contents of this Coop's inventory to CSV file.
     * @param filename Name of destination CSV file.
     */
    public boolean writeRentalItems(String filename) {
        File file = new File(filename);
        FileWriter writer;
        try {
            writer = new FileWriter(file);  // throws IOException
            for (int idx = 0; idx < nextIndex; idx++) {
                RentalItem item = rentalItems[idx];
                String itemCsv = item.toCSV();
                writer.write(itemCsv + System.lineSeparator());
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // phase 3

    /**
     * Process transactions that are stored in a CSV file.
     * @param txFileName - Points to CSV file.
     * @param auditFileName - Points to audit trail file.
     * @return - Number of transactions that were processed.
     */
    public int processTransactions(String txFileName, String auditFileName) {
        int numTxProcessed = 0;
        try {
            Audit audit = new Audit(auditFileName);
            Scanner scan;
            try {
                scan = new Scanner(new File(txFileName));
                while (scan.hasNextLine()) {
                    Transaction tx = Transaction.make(scan.nextLine());
                    int targetIdx = find(tx.getItemID());
                    if (targetIdx >= 0) { // target item present
                        RentalItem target = rentalItems[find(tx.getItemID())];
                        if (tx.validate(target, audit)) { // transaction can execute
                            tx.execute(target, audit);
                        }
                    } else { // target item absent
                        audit.recordNoSuchItem(tx);
                    }
                    numTxProcessed++;
                }
                scan.close();
            } catch(FileNotFoundException e) {
                // bad fileName passed to Scanner constructor
                e.printStackTrace();
            }
            audit.close();
        } catch (IOException e) {
            // problem wile Audit constructor
            e.printStackTrace();
        }
        return numTxProcessed;
    }

}
