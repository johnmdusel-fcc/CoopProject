public class RentalVehicle extends RentalItem {

    public RentalVehicle(String id, String owner, RentalItemState state) {
        super(id, owner, state);
    }

    @Override
    public RentalItemType getItemType() {return RentalItemType.VEHICLE;}

}
