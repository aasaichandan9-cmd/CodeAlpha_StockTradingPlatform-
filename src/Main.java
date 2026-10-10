import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static User user;
    private static Market market;

    public static void main(String[] args) {
        System.out.println("Welcome to the CodeAlpha Stock Trading Platform");
        
        market = new Market();
        if (!FileManager.loadMarket(market)) {
            System.out.println("No previous market data found. Starting fresh.");
        }

        user = FileManager.loadUser();
        if (user == null) {
            System.out.println("No previous user data found. Creating new user with $10,000 starting balance.");
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) name = "Trader";
            user = new User(name, 10000.0);
        } else {
            System.out.println("Welcome back, " + user.getName() + "!");
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    market.displayMarket();
                    break;
                case "2":
                    handleBuy();
                    break;
                case "3":
                    handleSell();
                    break;
                case "4":
                    displayPortfolio();
                    break;
                case "5":
                    System.out.println("=== Transaction History ===");
                    user.printTransactionHistory();
                    break;
                case "6":
                    market.simulatePriceMovement();
                    System.out.println("Market conditions have changed!");
                    market.displayMarket();
                    break;
                case "7":
                    FileManager.saveUser(user);
                    FileManager.saveMarket(market);
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. View Market");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio & Performance");
        System.out.println("5. View Transaction History");
        System.out.println("6. Simulate Market Movement");
        System.out.println("7. Save & Exit");
        System.out.print("Select an option: ");
    }

    private static void handleBuy() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = market.getStock(symbol);
        
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.printf(Locale.US, "Current price of %s is $%.2f. Available cash: $%.2f\n", 
                stock.getSymbol(), stock.getCurrentPrice(), user.getCashBalance());
        System.out.print("Enter quantity to buy: ");
        
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine().trim());
            if (qty <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        double totalCost = stock.getCurrentPrice() * qty;
        if (totalCost > user.getCashBalance()) {
            System.out.printf(Locale.US, "Insufficient funds! Required: $%.2f, Available: $%.2f\n", totalCost, user.getCashBalance());
            return;
        }

        System.out.printf(Locale.US, "Confirm purchase of %d shares of %s for $%.2f? (Y/N): ", qty, stock.getSymbol(), totalCost);
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y") || confirm.equals("YES")) {
            if (user.buyStock(stock, qty)) {
                System.out.println("Purchase successful!");
            } else {
                System.out.println("Purchase failed.");
            }
        } else {
            System.out.println("Purchase cancelled.");
        }
    }

    private static void handleSell() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();
        int owned = user.getPortfolio().getQuantity(symbol);
        
        if (owned == 0) {
            System.out.println("You don't own any shares of " + symbol);
            return;
        }

        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Stock not found in market.");
            return;
        }

        System.out.printf(Locale.US, "You own %d shares of %s. Current price: $%.2f\n", owned, symbol, stock.getCurrentPrice());
        System.out.print("Enter quantity to sell: ");
        
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine().trim());
            if (qty <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (qty > owned) {
            System.out.println("You don't own enough shares!");
            return;
        }

        double totalRevenue = stock.getCurrentPrice() * qty;
        System.out.printf(Locale.US, "Confirm sale of %d shares of %s for $%.2f? (Y/N): ", qty, symbol, totalRevenue);
        String confirm = scanner.nextLine().trim().toUpperCase();
        
        if (confirm.equals("Y") || confirm.equals("YES")) {
            if (user.sellStock(stock, qty)) {
                System.out.println("Sale successful!");
            } else {
                System.out.println("Sale failed.");
            }
        } else {
            System.out.println("Sale cancelled.");
        }
    }

    private static void displayPortfolio() {
        System.out.println("\n=========================================================");
        System.out.println("                       PORTFOLIO                         ");
        System.out.println("=========================================================");
        
        Map<String, Integer> holdings = user.getPortfolio().getHoldings();
        if (holdings.isEmpty()) {
            System.out.println("Your portfolio is currently empty.");
        } else {
            System.out.printf("%-6s | %-10s | %-12s | %-12s\n", "Symbol", "Quantity", "Current Px", "Total Value");
            System.out.println("---------------------------------------------------------");
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                String symbol = entry.getKey();
                int qty = entry.getValue();
                Stock stock = market.getStock(symbol);
                if (stock != null) {
                    double value = stock.getCurrentPrice() * qty;
                    System.out.printf(Locale.US, "%-6s | %-10d | $%11.2f | $%11.2f\n", 
                            symbol, qty, stock.getCurrentPrice(), value);
                }
            }
        }
        
        double pValue = user.getPortfolioValue(market.getMarketStocks());
        double totalAssets = user.getTotalAssetValue(market.getMarketStocks());
        
        System.out.println("---------------------------------------------------------");
        System.out.printf(Locale.US, "Portfolio Value:  $%12.2f\n", pValue);
        System.out.printf(Locale.US, "Cash Balance:     $%12.2f\n", user.getCashBalance());
        System.out.println("---------------------------------------------------------");
        System.out.printf(Locale.US, "Total Assets:     $%12.2f\n", totalAssets);
        System.out.println("=========================================================\n");
    }
}
