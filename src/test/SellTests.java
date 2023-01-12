package src.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.Before;
import org.junit.Test;

import src.main.Main;
import src.main.model.Trade;
import src.main.model.Trade.Stock;
import src.main.model.Trade.Type;
import src.main.model.account.Account;
import src.main.model.account.Personal;
import src.main.model.account.TFSA;

public class SellTests {
    
    Account[] accounts;

    @Before
    public void setup() {
        accounts = new Account[] {
            new Personal(3000),
            new TFSA(2500)
        };
    }

    @Test
    public void sellMoreSharesThanYouOwn() {
        assertFalse(accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_SELL, Double.parseDouble(Main.getPrice(Stock.AAPL, 6)), 10)));
    }

    @Test
    public void fundsTest() {
        accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_BUY, Double.parseDouble(Main.getPrice(Stock.AAPL, 8)) /*15.674286 */, 9)); // funds after buy : 2357.52074
        accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_SELL, Double.parseDouble(Main.getPrice(Stock.AAPL, 9)) /*15.517857 */, 5)); // funds after sell : 2435.88591
        assertEquals(2435.89, accounts[1].getFunds());
    }

    @Test
    public void portfolioSharesTest() {
        accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_BUY, Double.parseDouble(Main.getPrice(Stock.AAPL, 8)) /*15.674286 */, 9)); // funds after buy : 2357.52074
        accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_SELL, Double.parseDouble(Main.getPrice(Stock.AAPL, 9)) /*15.517857 */, 5)); // funds after sell : 2435.88591
        assertEquals(4, accounts[1].getShares(Stock.AAPL));
    }
}
