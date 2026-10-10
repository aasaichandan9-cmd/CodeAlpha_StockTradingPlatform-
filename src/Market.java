import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Market {
    private HashMap<String, Stock> marketStocks;
    private Random random;

    public Market() {
        marketStocks = new HashMap<>();
        random = new Random();
        loadInitialStocks();
    }

    private void loadInitialStocks() {
        marketStocks.put("AAPL", new Stock("AAPL", "Apple", 180.0));
        marketStocks.put("GOOGL", new Stock("GOOGL", "Alphabet", 140.0));
        marketStocks.put("MSFT", new Stock("MSFT", "Microsoft", 380.0));
        marketStocks.put("AMZN", new Stock("AMZN", "Amazon", 185.0));
        marketStocks.put("TSLA", new Stock("TSLA", "Tesla", 250.0));
        marketStocks.put("META", new Stock("META", "Meta", 520.0));
        marketStocks.put("NVDA", new Stock("NVDA", "Nvidia", 875.0));
        marketStocks.put("JPM", new Stock("JPM", "JPMorgan", 200.0));
        marketStocks.put("V", new Stock("V", "Visa", 280.0));
        marketStocks.put("WMT", new Stock("WMT", "Walmart", 70.0));
        marketStocks.put("NFLX", new Stock("NFLX", "Netflix", 680.0));
        marketStocks.put("BABA", new Stock("BABA", "Alibaba", 85.0));
    }

    public void simulatePriceMovement() {
        for (Stock stock : marketStocks.values()) {
            double movementPercent = (random.nextDouble() * 10) - 5.0; // -5.0 to +5.0 percent
            double changeMultiplier = 1.0 + (movementPercent / 100.0);
            stock.updatePrice(stock.getCurrentPrice() * changeMultiplier);
        }
    }

    public Stock getStock(String symbol) {
        return marketStocks.get(symbol.toUpperCase());
    }

    public HashMap<String, Stock> getMarketStocks() {
        return marketStocks;
    }

    public void displayMarket() {
        System.out.println("=========================================================================");
        System.out.println("                             MARKET SUMMARY                              ");
        System.out.println("=========================================================================");
        for (Stock stock : marketStocks.values()) {
            System.out.println(stock.getDisplayString());
        }
        System.out.println("=========================================================================");
    }

    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        for (Stock stock : marketStocks.values()) {
            sb.append(stock.toCSV()).append("\n");
        }
        return sb.toString();
    }

    public void loadFromCSV(String csvData) {
        String[] lines = csvData.split("\n");
        marketStocks.clear();
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                Stock stock = Stock.fromCSV(line);
                if (stock != null) {
                    marketStocks.put(stock.getSymbol(), stock);
                }
            }
        }
        if (marketStocks.isEmpty()) {
            loadInitialStocks();
        }
    }
}
