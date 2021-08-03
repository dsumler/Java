import javax.swing.*;
import java.util.ArrayList;

class Operator extends Users {
    Operator(String username, String password, String name, String id, boolean approve) {
        super(username, password, name, id, approve);
    }

    void Approve() {
        Object[] buttons = {"Approve", "Next", "Exit"};
        int count = 0;
        for (int i = 0; i < StockExchangePlatform.nonapprovedUsers.size(); ) {
            Users newuser = StockExchangePlatform.nonapprovedUsers.get(i);
            if (!newuser.getApprove()) {
                count++;
                int n1 = JOptionPane.showOptionDialog(null, "Name : " + newuser.getName() + "\nUsername : "
                                + newuser.getUsername() + "\nType of account : " + newuser.getClass().getName(), "Approve the user or skip.", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, buttons, buttons[0]);
                System.out.println(n1);
                if (n1 == 0) {
                    newuser.Approve(true);
                    i++;
                } else if (n1 == 1) {
                    i++;
                } else {
                    break;
                }
            } else {
                i++;
            }
        }
        if (count == 0) {
            JOptionPane.showMessageDialog(null, "No more users are pending approval.", "No users found", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    void ApproveOrders() {
        Object[] buttons = {"Approve", "Deny", "Next", "Exit"};
        int size;
        ArrayList<Order> orders = StockExchangePlatform.currentOrders;
        if ((size = orders.size()) == 0) {
            JOptionPane.showMessageDialog(null, "No more orders are pending approval.", "No orders found", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (int i = 0; i < size; ) {
                Order order = orders.get(i);
                int n1 = JOptionPane.showOptionDialog(null, "Buyer ID : " + order.getBuyer().getID() + "\nSeller ID : "
                                + order.getSeller().getID() + "\nSecurity Description : " + order.getListed().getDescription() +
                                "\nSupply : " + order.getListed().getSupply() + "\nPrice per unit : " + order.getListed().getPrice(),
                        "Approve the user or skip.", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, buttons, buttons[0]);
                if (n1 == 0) {
                    MatchingEngine.Buy(order);
                } else if (n1 == 1) {
                    MatchingEngine.noBuy(order);
                } else if(n1 == 2){
                    i ++;
                }
            }
        }
    }
}
