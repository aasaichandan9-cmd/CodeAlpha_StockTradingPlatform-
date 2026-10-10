import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private double cashBalance;
    private Portfolio portfolio;
    private ArrayList<Transaction> transactionHistory;

    public User(String name, double startingBalance) {
        this.name = name;
        this.cashBalance = startingBalance;
        this.portfolio = new Portfolio();
        this.transactionHistory = new ArrayList<>();
    }
    
    public String getName() { return name; }
    public double getCashBalance() { return cashBalance; }
    public Portfolio getPortfolio() { return portfolio; }
    public ArrayList<Transaction> getTransactionHistory() { return transactionHistory; }

    public void setCashBalance(double balance) {
        this.cashBalance = balance;
    }

    public boolean buyStock(Stock stock, int qty) {
        double cost = stock.getCurrentPrice() * qty;
        if (cashBalance >= cost) {
            cashBalance -= cost;
            portfolio.buy(stock.getSymbol(), qty);
            Transaction tx = new Transaction("BUY", stock.getSymbol(), qty, stock.getCurrentPrice(), cost, LocalDateTime.now());
            transactionHistory.add(tx);
            return true;
        }
        return false;
    }

    public boolean sellStock(Stock stock, int qty) {
        if (portfolio.hasStock(stock.getSymbol(), qty)) {
            double revenue = stock.getCurrentPrice() * qty;
            portfolio.sell(stock.getSymbol(), qty);
            cashBalance += revenue;
            Transaction tx = new Transaction("SELL", stock.getSymbol(), qty, stock.getCurrentPrice(), revenue, LocalDateTime.now());
            transactionHistory.add(tx);
            return true;
        }
        return false;
    }

    public double getPortfolioValue(HashMap<String, Stock> marketStocks) {
        double value = 0.0;
        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            Stock stock = marketStocks.get(entry.getKey());
            if (stock != null) {
                value += stock.getCurrentPrice() * entry.getValue();
            }
        }
        return value;
    }

    public double getTotalAssetValue(HashMap<String, Stock> marketStocks) {
        return cashBalance + getPortfolioValue(marketStocks);
    }

    public void printTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        for (Transaction tx : transactionHistory) {
            System.out.println(tx.toString());
        }
    }

    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(",").append(cashBalance).append("\n");
        sb.append("---HOLDINGS---\n");
        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            sb.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        sb.append("---TRANSACTIONS---\n");
        for (Transaction tx : transactionHistory) {
            sb.append(tx.toCSV()).append("\n");
        }
        return sb.toString();
    }

    public static User fromCSV(String csvData) {
        String[] lines = csvData.split("\n");
        if (lines.length == 0) return null;
        
        String[] header = lines[0].split(",");
        if (header.length < 2) return null;
        
        User user = new User(header[0], Double.parseDouble(header[1]));
        
        int mode = 0;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            
            if (line.equals("---HOLDINGS---")) {
                mode = 1;
                continue;
            } else if (line.equals("---TRANSACTIONS---")) {
                mode = 2;
                continue;
            }
            
            if (mode == 1) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    user.portfolio.buy(parts[0], Integer.parseInt(parts[1]));
                }
            } else if (mode == 2) {
                Transaction tx = Transaction.fromCSV(line);
                if (tx != null) {
                    user.transactionHistory.add(tx);
                }
            }
        }
        return user;
    }
}
