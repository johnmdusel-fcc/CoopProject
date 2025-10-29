import java.io.FileWriter;
import java.io.IOException;

public class Audit {

    private FileWriter writer;

    /**
     * Create a new output stream. Stays open during this instance's lifepan.
     * 
     * Output file is TXT with lines like this:
     * Timestamp Level Account ID Transaction Type Amount Balance Forward
     * 
     * @param fileName Points to desired log file. Overwrites existing file.
     * @throws IllegalArgumentException If fileName is null.
     * @throws IOException If the default file is not usable.
     */
    public Audit(String fileName) throws IOException{
        if (fileName == null) {
            throw new IllegalArgumentException("fileName cannot be null.");
        }
        writer = new FileWriter(fileName); // throws IOException
    }

    /**
     * Close this audit's output file stream.
     */
    public void close() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a line to the audit file.
     * @param s - Content to be written to output file.
     */
    private void write(String s) {
        try {
            writer.write(s + System.lineSeparator());
        }
        catch (IOException e) { // fall back to console output
            e.printStackTrace();
        }
    }

    /**
     * Write audit line for No Such Item error.
     * @param t - Transaction causing the error.
     */
    public void recordNoSuchItem(Transaction t) {
        write(
            String.format(
                "%s ERROR no such item: %s", 
                Utils.timestamp(), 
                t.toString()
            )
        );
    }

    /**
     * Write audit line for Nonsufficient Availability error.
     * @param t - Transaction causing the error.
     * @param ri - Target rental item.
     */
    public void recordNonsufficientAvailability(Transaction t, RentalItem ri) {
        write(
            String.format(
                "%s ERROR nonsufficient availability: %s, but item is %s",
                Utils.timestamp(),
                t.toString(),
                ri.getAvailability().name()
            )            
        );
    }

    /**
     * Write audit line for transaction execution.
     * @param t - Transaction being executed.
     * @param ri - Target rental item.
     */
    public void recordExecute(Transaction t, RentalItem ri) {
        write(
            String.format(
                "%s INFO: %s, item is now %s",
                Utils.timestamp(),
                t.toString(),
                ri.getAvailability().name()
            )            
        );
    }

}
