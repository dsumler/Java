import javax.swing.*;
import java.util.ArrayList;

class StockExchangePlatform {
    static ArrayList<Trader> registeredTraders = new ArrayList<>();
    static ArrayList<Lister> registeredListers = new ArrayList<>();
    static ArrayList<Operator> registeredOperators = new ArrayList<>();
    static ArrayList<Users> nonapprovedUsers = new ArrayList<>();
    static ArrayList<Order> currentOrders = new ArrayList<>();

    static void Register(String username, String password, String name, String idno, String type ){
        switch (type) {
            case "Trader":
                Trader t = new Trader(username, password, name, idno, false);
                nonapprovedUsers.add(t);
                registeredTraders.add(t);
                break;
            case "Lister":
                Lister l = new Lister(username, password, name, idno, false);
                nonapprovedUsers.add(l);
                registeredListers.add(l);
                break;
            case "Operator":
                Operator o = new Operator(username, password, name, idno, false);
                nonapprovedUsers.add(o);
                registeredOperators.add(o);
                break;
        }
    }
    static <T> T Login(String username, String password, String type){

        switch (type) {
            case "Trader":
                Trader t;
                for (Trader registeredTraders : registeredTraders) {
                    t = registeredTraders;
                    if (t.getUsername().equals(username) && t.getPassword().equals(password)) {
                        return (T) t;
                    }
                }
                break;
            case "Lister":
                Lister l;
                for (Lister registeredListers : registeredListers) {
                    l = registeredListers;
                    if (l.getUsername().equals(username) && l.getPassword().equals(password)) {
                        return (T) l;
                    }
                }
                break;
            case "Operator":
                Operator op;
                for (Operator registeredOperators : registeredOperators) {
                    op = registeredOperators;
                    if (op.getUsername().equals(username) && op.getPassword().equals(password)) {
                        return (T) op;
                    }
                }
                break;
        }
        JOptionPane.showMessageDialog(null, "Error, Login Failed. User not found.");
        return null;
    }

}
