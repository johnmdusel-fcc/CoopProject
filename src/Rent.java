public class Rent extends Transaction {

    /**
     * Construct a new Rent transaction.
     * @param itemID - Unique ID of target rental item.
     */
    public Rent(String itemID) { super(itemID); }

    /** 
     * Change a rental item's status from AVAILABLE to RENTED.
     * @param item - Target rental item.
     */
    @Override
    public void execute(RentalItem item) {
        item.checkOut();
    }

}
