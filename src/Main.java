public class Main {

    public static void main(String[] args) {
        phase2();
    }

    private static void phase2() {
        // path with forward slashes bc dev container FROM ubuntu
        String rentalItemsFilename = "data/rentalitems.csv";
        Coop coop = new Coop();
        boolean result = coop.loadRentalItems(rentalItemsFilename);
        System.out.println("Result of loading rental items: " + result);
        System.out.println("Number of rental items: " + coop.getInventoryCount());

        rentalItemsFilename = "data/rentalitems-output.csv";
        coop.writeRentalItems(rentalItemsFilename);

        Coop coopReload = new Coop();
        coopReload.loadRentalItems(rentalItemsFilename);
        
    }

} // end class: Main
