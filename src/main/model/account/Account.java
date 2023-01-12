package src.main.model.account;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import src.main.model.Trade;
import src.main.model.Trade.Stock;
import src.main.utils.Color;

public abstract class Account {

    private HashMap<Stock, Integer> portfolio;
    private double funds;

    public Account(double funds) {
        if (funds <= 0) {
            throw new IllegalArgumentException("INVALID PARAM");
        }
        this.funds = funds;
        this.portfolio = new HashMap<>();
        this.portfolio.put(Trade.Stock.AAPL, 0);
        this.portfolio.put(Trade.Stock.FB, 0);
        this.portfolio.put(Trade.Stock.GOOG, 0);
        this.portfolio.put(Trade.Stock.TSLA, 0);
    }

    public Account(Account source) {
        this.funds = source.funds;
        this.portfolio = new HashMap<>(source.portfolio);
    }

    public double getFunds() {
        return round(funds);
    }

    public void setFunds(double funds) {
        if (funds <= 0) {
            throw new IllegalArgumentException("INVALID PARAM");
        }
        this.funds = funds;
    }

    public String displayPortfolio() {
        String temp = "";
        for (Map.Entry<Stock, Integer> elem : portfolio.entrySet()) {
            temp += "  " + elem.getKey() + "\t\t" + elem.getValue() + "\n";
        }
        return temp;
    }

    public int getShares(Stock stock) {

        return portfolio.get(stock);
    }

    public void setShares(Stock stock, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("INVALID PARAM");
        }
        portfolio.put(stock, amount);
    }

    public static double round(double amount) {
        DecimalFormat formatter = new DecimalFormat("#.##");
        return Double.parseDouble(formatter.format(amount));
    }

    public abstract boolean makeTrade(Trade trade);

    protected boolean executePurchase(Trade trade, double fee) {
        if (checkSubtractFunds(trade, fee)) {
            subtractFunds(trade, fee);
            addShares(trade);
            return true;
        }
        return false;
    }

    protected boolean executeSale(Trade trade, double fee) {
        if (checkSubtractShares(trade)) {
            this.addFunds(trade, fee);
            this.subtractShares(trade);
            return true;
        }
        return false;
    }

    private boolean checkSubtractShares(Trade trade) {
        if (this.getShares(trade.getStock()) >= trade.getShares()) {
            return true;
        }
        return false;
    }

    private boolean subtractShares(Trade trade) {
        if (checkSubtractShares(trade)) {
            int currentShares = this.getShares(trade.getStock());
            this.setShares(trade.getStock(), currentShares - trade.getShares());
            return true;
        }
        return false;
    }

    private void addShares(Trade trade) {
        int currentShares = this.getShares(trade.getStock());
        this.setShares(trade.getStock(), currentShares + trade.getShares());
    }

    private boolean checkSubtractFunds(Trade trade, double fee) {
        if (trade.getPrice() * trade.getShares() * (1 + fee) <= getFunds()) {
            return true;
        }
        return false;
    }

    private boolean subtractFunds(Trade trade, double fee) {
        if (checkSubtractFunds(trade, fee)) {
            setFunds(getFunds() - trade.getPrice() * trade.getShares() * (1 + fee));
            return true;
        }
        return false;
    }

    private void addFunds(Trade trade, double fee) {
        double total = trade.getPrice() * trade.getShares();
        double tax = total * fee;
        this.setFunds(this.getFunds() + total + tax);
    }

    public String toString() {
        return "\n  Stock\t\t"  + Color.RESET + "Shares" +
        "\n\n" + displayPortfolio() + Color.RESET +
        "\n  Funds Left\t" + Color.GREEN + "$" + round(this.getFunds()) + Color.RESET;
    }

}
