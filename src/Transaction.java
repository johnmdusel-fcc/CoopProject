abstract class Transaction {

    private final String itemID;
    public String getItemID() { return itemID; }

    abstract void execute(RentalItem item);

    protected Transaction(String itemID) {
        if (itemID != null) this.itemID = itemID;
        else throw new IllegalArgumentException("Parameter id cannot be null.");
    }

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
            return new Rent(id);
        }
        else {
            return new Return(id);
        }
    }
}
