import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
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
        if (find(item) == -1) {  // item absent from rentalItems
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
    public int find(RentalItem item) {
        for (int idx = 0; idx < nextIndex; idx++) {
            if (
                rentalItems[idx].getUid() == item.getUid()
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
        boolean result = true;
        File inputFile = new File(filename);
        Scanner scan;
        try{
            scan = new Scanner(inputFile);
            while (scan.hasNextLine()) {
                String csvString = scan.nextLine();
                RentalItem rentalItem = RentalItem.make(csvString);
                addRentalItem(rentalItem);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * Write contents of this Coop's inventory to CSV file.
     * @param filename Name of destination CSV file.
     */
    public boolean writeRentalItems(String filename) {
        boolean result = true;
        File file = new File(filename);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(
                new FileWriter(file)  // throws IOException
            );
            for (int idx = 0; idx < nextIndex; idx++) {
                RentalItem item = rentalItems[idx];
                String itemCsv = item.toCSV();
                writer.write(itemCsv);  // throws IOException
                writer.newLine();
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

}
