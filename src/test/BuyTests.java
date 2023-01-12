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

public class BuyTests {
    
    Account[] accounts;

    @Before
    public void setup() {
        accounts = new Account[2];
        accounts[0] = new Personal(3000);
        accounts[1] = new TFSA(2500);
    }

    @Test
    public void portfolioSharesTest() {
        accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_BUY, Double.parseDouble(Main.getPrice(Stock.AAPL/*15.551071 */, 6)), 10));
        assertEquals(10, accounts[1].getShares(Stock.AAPL));
    }

    @Test
    public void fundsTest() {
        accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_BUY, Double.parseDouble(Main.getPrice(Stock.AAPL, 6)), 10));
        assertEquals(2342.93, accounts[1].getFunds());
    }

    @Test
    public void fundsExceededTest() {
        assertFalse(accounts[1].makeTrade(new Trade(Stock.AAPL, Type.MARKET_BUY, Double.parseDouble(Main.getPrice(Stock.AAPL, 7)), 1000)));
    }

}
