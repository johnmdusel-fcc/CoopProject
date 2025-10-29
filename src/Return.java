public class Return extends Transaction {

    /**
     * Construct a new Return transaction.
     * @param itemID - Unique id of target rental item.
     */
    public Return(String itemID) { super(itemID); }

    /**
     * Change a rental item's status from RENTED to AVAILABLE.
     * @param item - Target rental item.
     */
    @Override
    public void execute(RentalItem item, Audit audit) {
        item.checkIn();
        audit.recordExecute(this, item);
    }

    @Override
    public boolean validate(RentalItem item, Audit audit) {
        if (item.getAvailability() == RentalItemState.RENTED) {
            return true;
        } else {
            audit.recordNonsufficientAvailability(this, item);
            return false;
        }
    }

    @Override
    public String toString() {
        return "return item " + getItemID();
    }

}
