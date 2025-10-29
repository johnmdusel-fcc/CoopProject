abstract class Transaction {

    private final String itemID;
    public String getItemID() { return itemID; }

    abstract void execute(RentalItem item, Audit audit);
    
    /**
     * Check that this transaction's execution is compatible with item's
     * availability state.
     * @param item - Target rental item for this transaction.
     * @return - true if and only if the execute operation is possible.
     */
    abstract boolean validate(RentalItem item, Audit audit);

    protected Transaction(String itemID) {
        if (itemID != null) this.itemID = itemID;
        else throw new IllegalArgumentException("Parameter id cannot be null.");
    }

    /**
     * Factory method for constructing a Transaction object from a CSV line. 
     * @param inputLine - Eg, "rental,rp332960"
     * @return - Transaction from supplied values.
     */
    protected static Transaction make(String inputLine) {
        String[] tokens;
        try {
            tokens = inputLine.split(",");
        } catch(NullPointerException e) {
            throw new IllegalArgumentException(
                "Parameter inputLine cannot be null."
            );
        }

        // throws IllegalArgumentException if tokens[0] is invalid
        TransactionType type = TransactionType.valueOf(tokens[0].toUpperCase());
        
        String id = tokens[1];
        
        if (type == TransactionType.RENTAL) {
            return new Rental(id);
        }
        else {
            return new Return(id);
        }
    }
}
