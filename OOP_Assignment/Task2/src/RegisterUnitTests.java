import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RegisterUnitTests {
    @Test
    public void RegisterShouldAddTraderToArrayList(){
        //    static void Register(String username, String password, String name, String idno, String type ){
        StockExchangePlatform.Register("Daniel1234", "1234", "Daniel", "1325", "Trader");
        assertEquals(1, StockExchangePlatform.registeredTraders.size());
        StockExchangePlatform.registeredTraders.remove(StockExchangePlatform.registeredTraders.get(0));
    }
    @Test
    public void RegisterShouldAddListerToArrayList(){
        //    static void Register(String username, String password, String name, String idno, String type ){
        StockExchangePlatform.Register("Daniel1234", "1234", "Daniel", "1325", "Lister");
        assertEquals(1, StockExchangePlatform.registeredListers.size());
        StockExchangePlatform.registeredListers.remove(StockExchangePlatform.registeredListers.get(0));
    }
    @Test
    public void RegisterShouldAddOperatorToArrayList() {
        //    static void Register(String username, String password, String name, String idno, String type ){
        StockExchangePlatform.Register("Daniel1234", "1234", "Daniel", "1325", "Operator");
        assertEquals(1, StockExchangePlatform.registeredOperators.size());
        StockExchangePlatform.registeredOperators.remove(StockExchangePlatform.registeredOperators.get(0));
    }
}