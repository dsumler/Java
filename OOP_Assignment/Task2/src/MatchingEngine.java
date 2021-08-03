import javax.swing.*;
import java.util.ArrayList;

class MatchingEngine {
    static int Match(){
        ArrayList<TraderSecurities> orders = MainMenu.orderbook.getOrders();
        ArrayList<ListerSecurities> lists = MainMenu.orderbook.getListed();
        for(int i = 0; i < orders.size(); i++){
            for(int i2 = 0; i2 < lists.size(); i2++){
                if(orders.get(i).getPrice() >= (lists.get(i2).getPrice()) && orders.get(i).getSupply() == lists.get(i2).getSupply()){
                    Trader buyer = orders.get(i).getTrader();
                    Lister seller = lists.get(i2).getLister();
                    Order msg = new Order(seller, buyer, lists.get(i2), orders.get(i));
                    buyer.AddMsg(msg);
                    seller.AddMsg(msg);
                    return 1;
                }
            }
        }
        return 0;
    }
    static int Buy(Order msg) {
        if (msg.getSelleragree() && msg.getBuyeragree() && msg.getOperatorAgree()) {
            ArrayList<TraderSecurities> orders = MainMenu.orderbook.getOrders();
            ArrayList<ListerSecurities> lists = MainMenu.orderbook.getListed();
            if (msg.getOrder().getSupply() >= msg.getListed().getSupply()) {
                lists.remove(msg.getListed());
                msg.getSeller().getListing(msg.getListed()).setListed(false);
            } else {
                ListerSecurities sec = msg.getListed();
                sec.setSupply(sec.getSupply() - msg.getOrder().getSupply());
                lists.remove(msg.getListed());
                lists.add(sec);
            }
            msg.getBuyer().getOrder(msg.getOrder()).setListed(false);
            orders.remove(msg.getOrder());
            return 1;
        }else{
            JOptionPane.showMessageDialog(null, "Waiting for other party to confirm or deny transaction. You will be notified.");
        }
        return 0;
    }

    static int noBuy(Order msg){
        try {
            msg.getBuyer().GetMsg().remove(msg);
            msg.getSeller().GetMsg().remove(msg);
            StockExchangePlatform.currentOrders.remove(msg);
            JOptionPane.showMessageDialog(null, "The transaction was cancelled.");
        }catch(Exception e){
            System.out.println("Something went wrong and the order could not be cancelled!\nPlease logout and try again.");
        }
        return 1;
    }
}
