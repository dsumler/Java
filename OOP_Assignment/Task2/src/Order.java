import javax.swing.*;

public class Order {
    Lister seller;
    Trader buyer;
    ListerSecurities listedsec;
    TraderSecurities ordersec;
    boolean selleragree = false;
    boolean buyeragree = false;
    boolean operatoragree = false;

    public Order(Lister seller, Trader buyer, ListerSecurities listedsec, TraderSecurities ordersec){
        this.seller = seller;
        this.buyer = buyer;
        this.listedsec = listedsec;
        this.ordersec = ordersec;
    }
//CHANGED
    Lister getSeller(){
        return this.seller;
    }
    Trader getBuyer(){
        return this.buyer;
    }
    void setSelleragree(boolean agree){
        this.selleragree = agree;
    }
    boolean getSelleragree(){
        return this.selleragree;
    }
    void setBuyeragree(boolean agree){
        this.buyeragree = agree;
    }
    boolean getBuyeragree() {
        return this.buyeragree;
    }
    boolean getOperatorAgree(){
        return this.operatoragree;
    }
    void setOperatorAgree(boolean agree){
        this.operatoragree = agree;
    }
    ListerSecurities getListed(){
        return this.listedsec;
    }
    TraderSecurities getOrder(){
        return this.ordersec;
    }

    static int displayMsg(Order msg){
        Object[] buttons = {"Continue", "Cancel"};

        int n = JOptionPane.showOptionDialog(null, "Offer from " + msg.getBuyer().getUsername() + " to buy from " + msg.getSeller().getUsername() +
                "\nSecurity Description : " +msg.getListed().getDescription()+ "\nPrice per unit : " +msg.getListed().getPrice()+"\nSupply Available : "+msg.getListed().getSupply() +
                "\nSupply Wanted : " +msg.getOrder().getSupply(), "BUY/SELL REQUEST UPDATE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
        return n;
    }
}
