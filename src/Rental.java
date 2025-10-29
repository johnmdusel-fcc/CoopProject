public class Rental extends Transaction {

    /**
     * Construct a new Rent transaction.
     * @param itemID - Unique ID of target rental item.
     */
    public Rental(String itemID) { super(itemID); }

    /** 
     * Change a rental item's status from AVAILABLE to RENTED.
     * @param item - Target rental item.
     */
    @Override
    public void execute(RentalItem item, Audit audit) {
        item.checkOut();
        audit.recordExecute(this, item);
    }

    @Override
    public boolean validate(RentalItem item, Audit audit) {
        if (item.getAvailability() == RentalItemState.AVAILABLE) {
            return true;
        } else {
            audit.recordNonsufficientAvailability(this, item);
            return false;
        }
    }

    @Override
    public String toString() {
        return "rent item " + getItemID();
    }

}
