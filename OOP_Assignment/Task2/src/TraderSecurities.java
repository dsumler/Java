import java.sql.Timestamp;

class TraderSecurities extends Securities{
    Timestamp timestamps;
    //ArrayList<Securities> traders = new ArrayList<>();
    Trader trader;

    TraderSecurities(String description, Double price, int ts, Trader trader, Timestamp timestamp) {
        super(description, price, ts);
        this.trader = trader;
        this.timestamps = timestamp;
    }
    TraderSecurities(Securities sec, Trader trader, Timestamp timestamp){
        super(sec.getDescription(), sec.getPrice(), sec.getSupply());
        this.trader = trader;
        this.timestamps = timestamp;
    }

    Trader getTrader(){
        return this.trader;
    }
    Timestamp getTime(){
        return this.timestamps;
    }
}