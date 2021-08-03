import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

public class UnitTests {
    @Test
    public void LoginShouldReturnLister() {
        Lister tester = new Lister("a", "b", "Daniel", "12345", true);
        StockExchangePlatform.registeredListers.add(tester);
        assertEquals(tester, StockExchangePlatform.Login(tester.getUsername(), tester.getPassword(), "Lister"));
        //StockExchangePlatform.registeredListers.remove(tester);
    }
    @Test
    public void LoginShouldReturnTrader() {
        Trader tester = new Trader("a", "b", "Daniel", "12345", true);
        StockExchangePlatform.registeredTraders.add(tester);
        assertEquals(tester, StockExchangePlatform.Login(tester.getUsername(), tester.getPassword(), "Trader"));
        //StockExchangePlatform.registeredTraders.remove(tester);
    }
    @Test
    public void LoginShouldReturnOperator() {
        Operator tester = new Operator("a", "b", "Daniel", "12345", true);
        StockExchangePlatform.registeredOperators.add(tester);
        assertEquals(tester, StockExchangePlatform.Login(tester.getUsername(), tester.getPassword(), "Operator"));
        //StockExchangePlatform.registeredOperators.remove(tester);
    }
}