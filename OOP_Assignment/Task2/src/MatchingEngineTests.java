import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class MatchingEngineTests {

    @Test
    public void MatchingEngineMatches(){
        Trader tester = new Trader("a", "b", "Daniel", "12345", true);
        StockExchangePlatform.registeredTraders.add(tester);
        Lister tester2 = new Lister("a", "b", "Daniel", "12345", true);
        StockExchangePlatform.registeredListers.add(tester2);

        Date d = new Date();
        Timestamp ts = new Timestamp(d.getTime());
        ListerSecurities l = new ListerSecurities("Description", 12.0, 10, StockExchangePlatform.registeredListers.get(0));
        TraderSecurities t = new TraderSecurities("Description", 12.0, 10, StockExchangePlatform.registeredTraders.get(0), ts);
        //Order o = new Order(StockExchangePlatform.registeredListers.get(0), StockExchangePlatform.registeredTraders.get(0), l, t);
        //OrderBook ob = new OrderBook(StockExchangePlatform., StockExchangePlatform.currentOrders);
        MainMenu.orderbook.addOrder(t);
        MainMenu.orderbook.addList(l);
        assertEquals(1, MatchingEngine.Match());
    }
    @Test
    public void MatchingEngineBuys(){
        Date d = new Date();
        Timestamp ts = new Timestamp(d.getTime());
        ListerSecurities l = new ListerSecurities("Description", 12.0, 10, StockExchangePlatform.registeredListers.get(0));
        TraderSecurities t = new TraderSecurities("Description", 12.0, 10, StockExchangePlatform.registeredTraders.get(0), ts);
        StockExchangePlatform.registeredListers.get(0).addListing(l);
        StockExchangePlatform.registeredTraders.get(0).addOrder(t);
        Order o = new Order(StockExchangePlatform.registeredListers.get(0), StockExchangePlatform.registeredTraders.get(0), l, t);
        o.setBuyeragree(true);
        o.setSelleragree(true);
        o.setOperatorAgree(true);
        assertEquals(1, MatchingEngine.Buy(o));
    }
    @Test
    public void MatchingEngineNoBuys(){
        Date d = new Date();
        Timestamp ts = new Timestamp(d.getTime());
        ListerSecurities l = new ListerSecurities("Description", 12.0, 10, StockExchangePlatform.registeredListers.get(0));
        TraderSecurities t = new TraderSecurities("Description", 12.0, 10, StockExchangePlatform.registeredTraders.get(0), ts);
        StockExchangePlatform.registeredListers.get(0).addListing(l);
        StockExchangePlatform.registeredTraders.get(0).addOrder(t);
        Order o = new Order(StockExchangePlatform.registeredListers.get(0), StockExchangePlatform.registeredTraders.get(0), l, t);
        assertEquals(1, MatchingEngine.noBuy(o));
    }

}
