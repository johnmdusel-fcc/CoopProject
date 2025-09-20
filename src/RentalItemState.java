/**
 * Represents the possible states a RentalItem can be in.
 * 
 * AVAILABLE: Can be rented. Default state.
 * 
 * UNAVAILABLE: Cannot be rented, currently under rental.
 * 
 * DAMAGED: Cannot be rented, needs repair.
 * 
 * https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 */
public enum RentalItemState {
    AVAILABLE, 
    UNAVAILABLE, 
    DAMAGED
}
