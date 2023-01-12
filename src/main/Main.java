package src.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import src.main.model.Trade;
import src.main.model.Trade.Stock;
import src.main.model.Trade.Type;
import src.main.model.account.Account;
import src.main.model.account.Personal;
import src.main.model.account.TFSA;
import src.main.utils.Color;

public class Main {

    static Account account; 
    static final double INITIAL_DEPOSIT = 4000;
    static Scanner scanner = new Scanner(System.in);
  
    public static void main(String[] args) {    
        explainApp();
        account = accountChoice().equals("a") ? new Personal(INITIAL_DEPOSIT) : new TFSA(INITIAL_DEPOSIT);
        initialBalance();
        
        for (int i = 1; i <= 2160; i++) {
            displayPrices(i);
            String stock = chooseStock();
            if (stock == null) continue;
            Type buyOrSell = buyOrSell().equals("buy") ?  Trade.Type.MARKET_BUY : Trade.Type.MARKET_SELL;
            String choice = buyOrSell.equals(Type.MARKET_BUY) ? "buy" : "sell";
            if (account.makeTrade(new Trade(
                Stock.valueOf(stock), 
                buyOrSell,
                Double.parseDouble(getPrice(Stock.valueOf(stock), i)), 
                numShares(choice)))
            == true) {
                tradeStatus("successful");
            }
            else {
                tradeStatus("a failure");
            }
        }
    }

    public static void tradeOrPass() {
        System.out.println(Color.YELLOW + "");
    }

    public static void explainApp() {
        System.out.print(Color.BLUE + "\n - PERSONAL: ");
        System.out.println(Color.YELLOW + "Every sale made in a personal account is charged a 5% fee.");
        System.out.print(Color.BLUE + "\n - TFSA: ");
        System.out.println(Color.YELLOW + "Every trade (buy/sell) made from a TFSA is charged a 1% fee.\n");
        System.out.println(Color.BLUE + " - Neither account has a limit on the amount of trades that can be made." + Color.RESET);
    }
    
    public static void initialBalance() {
        System.out.print("\n\n  You created a " + Color.YELLOW + account.getClass().getSimpleName() + Color.RESET + " account.");
        System.out.println(" Your account balance is " + Color.GREEN + "$" + account.getFunds() + Color.RESET);
        System.out.print("\n  Enter anything to start trading: ");
        scanner.nextLine();
    }
    
    public static String accountChoice() {
        System.out.print("\n  Respectively, type 'a' or 'b' to create a Personal account or TFSA: ");
        String choice = scanner.nextLine();
        while (!choice.equals("a") && !choice.equals("b")) {
            System.out.print("  Respectively, type 'a' or 'b' to create a Personal account or TFSA: ");
            choice = scanner.nextLine();
        }
        return choice;
    }
    
    public static String buyOrSell() {
        System.out.print("\n\n  Would you like to 'buy' or 'sell': ");
        String choice = scanner.nextLine();
        while (!choice.equals("buy") && !choice.equals("sell")) {
            System.out.print("  Would you like to 'buy' or 'sell': ");
            choice = scanner.nextLine();
        }
        return choice;
    }

    public static String chooseStock() {
        System.out.println("  A blank input will skip the day.");
        System.out.print("  Choose a stock: ");
        String stock = scanner.nextLine();
        if (stock.isBlank()) return null;
        // String stock = scanner.nextLine();
        while (!stock.equals("AAPL") && !stock.equals("FB") && !stock.equals("GOOG") && !stock.equals("TSLA") ) {
            System.out.print("  Choose a stock: ");
            stock = scanner.nextLine();
        }
        return stock;
    }

    public static int numShares(String choice) {
        System.out.print("  Enter the number of shares you'd like to " + choice + ": ");
        int shares = scanner.nextInt(); 
        scanner.nextLine(); //throwaway nextLine
        while (shares <= 0) {
            System.out.print("  Enter the number of shares you'd like to " + choice + ": ");
            shares = scanner.nextInt();
            scanner.nextLine(); //throwaway nextLine
        }
        return shares;
    }
    
    public static void tradeStatus(String result) {
        System.out.println("\n  The trade was " + (result.equals("successful") ? Color.GREEN : Color.RED) + result + Color.RESET + ". Here is your portfolio:");
        System.out.println(account);
        System.out.print("\n  Press anything to continue");
        scanner.nextLine();
    }

    // public static Path getPath(Stock stock) {
    //     String fileName = "src/main/data/" + stock.toString() + ".csv";
    //     Path path = Paths.get(fileName);
    //     return path;
    // }

    public static Path getPath(Stock stock) {
        try {
            return Paths.get(Thread.currentThread().getContextClassLoader().getResource("src/main/data/"+stock+".csv").toURI());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String getPrice(Stock stock, int day) {
        try {
            File file = new File(getPath(stock).toString());
            try (Scanner scan = new Scanner(file)) {
                scan.nextLine();
                while (scan.hasNextLine()) {
                    String[] data = scan.nextLine().split(",");
                    if (data[0].equals(Integer.toString(day))) {
                        return data[1];
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public static void displayPrices(int day) {
        System.out.println("\n\n\t  DAY " + day + " PRICES\n");

        for (Stock stock : Stock.values()) {
            System.out.println("  " + Color.BLUE + stock + "\t\t" + Color.GREEN + roundToString(Double.parseDouble(getPrice(stock, day))));
        }
        System.out.println(Color.RESET);
    }

    private static String roundToString(double value) {
        DecimalFormat formatter = new DecimalFormat("#.###");
        return formatter.format(value);
    }

}
