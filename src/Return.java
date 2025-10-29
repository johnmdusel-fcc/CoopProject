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
    public void execute(RentalItem item) {
        item.checkIn();
    }

    @Override
    public boolean validate(RentalItem item) {
        if (item.getAvailability() == RentalItemState.RENTED) {
            return true;
        } else {
            System.out.println("Cannot return item " + item.getUid());
            return false;
        }
    }

    @Override
    public String toString() {
        return "return item " + getItemID();
    }

}
