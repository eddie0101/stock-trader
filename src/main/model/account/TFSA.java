package src.main.model.account;

import src.main.model.Trade;

public class TFSA extends Account {
    
    private static final double TRADE_FEE = 0.01;

    public TFSA(double funds) {
        super(funds);
    }

    public TFSA(TFSA source) {
        super(source);
    }

    @Override
    public boolean makeTrade(Trade trade) {
        return trade.getType() == Trade.Type.MARKET_BUY ? executePurchase(trade, TRADE_FEE) : executeSale(trade, TRADE_FEE);
    }

    
}
