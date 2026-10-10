import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private static final String USER_FILE = "user_data.csv";
    private static final String MARKET_FILE = "market_data.csv";

    public static void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            bw.write(user.toCSV());
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    public static User loadUser() {
        File file = new File(USER_FILE);
        if (!file.exists()) return null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return User.fromCSV(sb.toString());
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            return null;
        }
    }

    public static void saveMarket(Market market) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MARKET_FILE))) {
            bw.write(market.toCSV());
        } catch (IOException e) {
            System.err.println("Error saving market data: " + e.getMessage());
        }
    }

    public static boolean loadMarket(Market market) {
        File file = new File(MARKET_FILE);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            market.loadFromCSV(sb.toString());
            return true;
        } catch (IOException e) {
            System.err.println("Error loading market data: " + e.getMessage());
            return false;
        }
    }
}
