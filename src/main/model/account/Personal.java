package src.main.model.account;

import src.main.model.Trade;

public class Personal extends Account {

    private static final double SELL_FEE = 0.05;

    public Personal(double funds) {
        super(funds);
    }

    public Personal(Personal source) {
        super(source);
    }

    @Override
    public boolean makeTrade(Trade trade) {
        return trade.getType() == Trade.Type.MARKET_BUY ? executePurchase(trade, 0) : executeSale(trade, SELL_FEE);
    }

   
}
