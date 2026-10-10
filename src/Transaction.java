import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Transaction {
    private String type;
    private String symbol;
    private int quantity;
    private double price;
    private double total;
    private LocalDateTime timestamp;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Transaction(String type, String symbol, int quantity, double price, double total, LocalDateTime timestamp) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s | %-4s | %-6s | Qty: %4d | Price: $%8.2f | Total: $%10.2f",
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), type, symbol, quantity, price, total);
    }

    public String toCSV() {
        return type + "," + symbol + "," + quantity + "," + price + "," + total + "," + timestamp.format(formatter);
    }

    public static Transaction fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 6) {
            return new Transaction(parts[0], parts[1], Integer.parseInt(parts[2]), 
                    Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), 
                    LocalDateTime.parse(parts[5], formatter));
        }
        return null;
    }
}
