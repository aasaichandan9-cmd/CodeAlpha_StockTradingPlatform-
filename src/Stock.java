import java.util.Locale;

public class Stock {
    private String symbol;
    private String name;
    private double currentPrice;
    private double previousPrice;
    private double changePercent;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = price;
        this.previousPrice = price;
        this.changePercent = 0.0;
    }

    public Stock(String symbol, String name, double currentPrice, double previousPrice, double changePercent) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.previousPrice = previousPrice;
        this.changePercent = changePercent;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getCurrentPrice() { return currentPrice; }
    public double getPreviousPrice() { return previousPrice; }

    public void updatePrice(double newPrice) {
        this.previousPrice = this.currentPrice;
        this.currentPrice = newPrice;
        this.changePercent = ((this.currentPrice - this.previousPrice) / this.previousPrice) * 100.0;
    }

    public double getPriceChange() {
        return currentPrice - previousPrice;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public String getDisplayString() {
        String sign = changePercent >= 0 ? "+" : "";
        return String.format(Locale.US, "%-6s | %-12s | $%8.2f | %s%.2f (%.2f%%)", 
                symbol, name, currentPrice, sign, getPriceChange(), changePercent);
    }

    @Override
    public String toString() {
        return getDisplayString();
    }

    public String toCSV() {
        return symbol + "," + name + "," + currentPrice + "," + previousPrice + "," + changePercent;
    }

    public static Stock fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 5) {
            return new Stock(parts[0], parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
        }
        return null;
    }
}
