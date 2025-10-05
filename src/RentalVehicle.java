public class RentalVehicle extends RentalItem {

    public RentalVehicle(String id, String owner) {
        super(id, owner);
    }

    public RentalItemType getItemType() {return RentalItemType.VEHICLE;}

}
