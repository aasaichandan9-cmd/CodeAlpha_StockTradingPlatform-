import java.util.HashMap;

public class Portfolio {
    private HashMap<String, Integer> holdings;

    public Portfolio() {
        this.holdings = new HashMap<>();
    }

    public void buy(String symbol, int qty) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
    }

    public boolean sell(String symbol, int qty) {
        int currentQty = holdings.getOrDefault(symbol, 0);
        if (currentQty >= qty) {
            int newQty = currentQty - qty;
            if (newQty == 0) {
                holdings.remove(symbol);
            } else {
                holdings.put(symbol, newQty);
            }
            return true;
        }
        return false;
    }

    public HashMap<String, Integer> getHoldings() {
        return holdings;
    }

    public int getQuantity(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }

    public boolean hasStock(String symbol, int qty) {
        return getQuantity(symbol) >= qty;
    }
}
