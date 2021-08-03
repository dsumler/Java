import java.util.ArrayList;

public class OrderBook {
    private ArrayList<ListerSecurities> listedSecurities = new ArrayList<>();
    private ArrayList<TraderSecurities> orderSecurities = new ArrayList<>();

    OrderBook(ArrayList<ListerSecurities> listed, ArrayList<TraderSecurities> orders){
        this.listedSecurities = listed;
        this.orderSecurities = orders;
    }
    OrderBook(){
        ArrayList<ListerSecurities> empty = new ArrayList<>();
        ArrayList<TraderSecurities> empty2 = new ArrayList<>();
        this.listedSecurities = empty;
        this.orderSecurities = empty2;
    }


    public void addOrder(TraderSecurities sec){
        this.orderSecurities.add(sec);
    }
    public ArrayList<TraderSecurities> getOrders(){
        return this.orderSecurities;
    }
    public void addList(ListerSecurities sec){
        this.listedSecurities.add(sec);
    }
    public ArrayList<ListerSecurities> getListed(){
        return this.listedSecurities;
    }
    public int sizeOrders(){
        return orderSecurities.size();
    }
    public int sizeLists(){
        return listedSecurities.size();
    }

}
