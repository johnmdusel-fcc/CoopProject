public class RentalTool extends RentalItem {

    public RentalTool(String id, String owner, RentalItemState state) {
        super(id, owner, state);
    }

    @Override
    public RentalItemType getItemType() {return RentalItemType.TOOL;}

}
