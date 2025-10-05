public class RentalTool extends RentalItem {

    public RentalTool(String id, String owner) {
        super(id, owner);
    }

    public RentalItemType getItemType() {return RentalItemType.TOOL;}

}
