abstract class RentalItem {

    // Attributes of a rental item that don't change
    private final String uid;
    private final String ownerName;

    // Availability state can be changed
    private RentalItemState itemState;

    /**
     * Factory method for constructing a RentalItem object from a CSV line. 
     * @param inputLine Eg, "tool,xf123456,Lorenzo de Medici, available"
     * @return new RentalItem from supplied values.
     * @throws IllegalArgumentException on null input.
     */
    public static RentalItem make(String inputLine) {
        if (inputLine == null) {
            throw new IllegalArgumentException(
                "Parameter inputLine cannot be null."
            );
        }
        String[] tokens = inputLine.split(",");
        // throws on invalid type or state
        RentalItemType type = RentalItemType.valueOf(tokens[0].toUpperCase());
        RentalItemState state = RentalItemState.valueOf(tokens[3].toUpperCase());
        String id = tokens[1];
        String owner = tokens[2];
        if (type == RentalItemType.TOOL) {
            return new RentalTool(id, owner, state);
        } else {
            return new RentalVehicle(id, owner, state);
        }
    }

    /**
     * Construct a new rental item
     * @param id Alphanumeric UID for this item.
     * @param owner Name of this item's owner.
     * @throws IllegalArgumentException On null pointer reference.
     */
    protected RentalItem(String id, String owner, RentalItemState state) {
        if (id != null) {
            uid = id;
        } else {
            throw new IllegalArgumentException(
                "Parameter id cannot be null."
            );
        }
        if (owner != null) {
            ownerName = owner;
        } else {
            throw new IllegalArgumentException(
                "Parameter owner cannot be null."
            );
        }
        if (state != null) {
            itemState = RentalItemState.AVAILABLE;
        } else {
            throw new IllegalArgumentException(
                "Parameter state cannot be null."
            );
        }
    }

    // Accessor methods

    /**
     * Accessor for availability state.
     * @return Availability state of this rental item.
     */
    public RentalItemState getAvailability() { return itemState; }

    /**
     * Accessor for owner name.
     * @return Name of owner of this rental item.
     */
    public String getOwner() { return ownerName; }

    /**
     * Accessor for item type.
     * @return Type of this rental item.
     */
    abstract public RentalItemType getItemType();

    /**
     * Accessor for UID.
     * @return Alphanumeric ID of this rental item.
     */
    public String getUid() { return uid; }

    // mutator methods

    private void setAvailability(RentalItemState newState) {
        itemState = newState;
    }
    
    public void checkOut() {
        setAvailability(RentalItemState.RENTED);
    }

    public void checkIn() {
        setAvailability(RentalItemState.AVAILABLE);
    }

    // other methods

    /**
     * CSV line holding this rental item's data. 
     * @return Eg, "tool,xf123456,Lorenzo de Medici".
     */
    public String toCSV() {
        return String.join(",", 
            getItemType().name().toLowerCase(),
            getUid(),
            getOwner(),
            getAvailability().name().toLowerCase()
        );
    }

} // end: RentalItem class
