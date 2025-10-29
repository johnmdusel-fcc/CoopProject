import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    
    public static String timestamp() {
        return LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss")
        );
    }

}
