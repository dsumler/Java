import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MainMenu {

    static OrderBook orderbook = new OrderBook();

    public static void main(String args[]) {
        Lister listy = new Lister("dsumler1", "12345", "Daniela", "12345", true);
        Operator op = new Operator("Operator1", "12345", "Daniel", "12345", true);
        Trader tradey = new Trader("dsumler", "12345", "Daniel", "123456", true);
        StockExchangePlatform.registeredListers.add(listy);
        StockExchangePlatform.registeredTraders.add(tradey);
        StockExchangePlatform.registeredOperators.add(op);
        displayMenu();
    }

    private static void displayMenu() {
        boolean quit = false;
        do {
            int choice;
            Scanner scn = new Scanner(System.in);
            System.out.println("Welcome to the Stock Exchange Platform!");
            System.out.println("Press 1 to Register");
            System.out.println("Press 2 to Login");
            System.out.println("Press 3 to view Order Book");
            try {
                choice = scn.nextInt();

            switch (choice) {
                case 1:
                    JTextField field1 = new JTextField("");
                    JPasswordField field2 = new JPasswordField();
                    JTextField field3 = new JTextField("");
                    JTextField field4 = new JTextField("");
                    String[] types = {"Trader", "Lister", "Operator"};
                    JComboBox<String> type = new JComboBox<>(types);

                    Object[] options = {"Register", "Exit"};

                    Object[] fields = {"Username: ", field1, "Password: ", field2, "First Name: ", field3, "ID Card No.: ", field4, "Type: ", type};
                    int n = JOptionPane.showOptionDialog(null, fields, "Register", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if(n == 0 && ((field1.getText()).equals("")||field2.getText().equals("")|| field3.getText().equals("") || field4.getText().equals(""))){
                        JOptionPane.showMessageDialog(null, "One or more fields left blank", "Please fill in fields again.", JOptionPane.ERROR_MESSAGE);
                    } else if(n != 0){
                        JOptionPane.showMessageDialog(null, "Registration Failed.", "Registration exited.", JOptionPane.ERROR_MESSAGE);
                    } else {
                        StockExchangePlatform.Register(field1.getText(), field2.getText(), field3.getText(), field4.getText(), Objects.requireNonNull(type.getSelectedItem()).toString());
                    }
                    break;
                case 2:
                    Object[] buttons = {"Trader", "Lister", "Operator"};
                    int n1 = JOptionPane.showOptionDialog(null, "", "Choose to login as a trader or lister."
                            , JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
                    JTextField loginfield1 = new JTextField();
                    JPasswordField loginfield2 = new JPasswordField();

                    Object[] loginoptions = {"Login", "Exit"};

                    Object[] loginfields = {"Username: ", loginfield1, "Password: ", loginfield2};
                    int n2 = JOptionPane.showOptionDialog(null, loginfields, "Login", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, loginoptions, loginoptions[0]);

                    if(n2 == 0 && ((loginfield1.getText()).equals("")||loginfield2.getText().equals(""))){
                        JOptionPane.showMessageDialog(null, "One or more fields left blank", "Please fill in fields again.", JOptionPane.ERROR_MESSAGE);
                        break;
                    } else if(n2 != 0){
                        JOptionPane.showMessageDialog(null, "Login Failed.", "Login exited.", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    if (n1 == 0) {
                        Trader user;
                        if ((user = StockExchangePlatform.Login(loginfield1.getText(), loginfield2.getText(),"Trader")) != null) {
                            if (user.getApprove()) {
                                ClassMenus.displayTraderMenu(user);
                            } else {
                                JOptionPane.showMessageDialog(null, "This user has not been approved yet.\n Please contact an Operator.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if(n1 == 1) {
                        Lister user;
                        if ((user = StockExchangePlatform.Login(loginfield1.getText(), loginfield2.getText(),"Lister")) != null) {
                            if (user.getApprove()) {
                                ClassMenus.displayListerMenu(user);
                            } else {
                                JOptionPane.showMessageDialog(null, "This user has not been approved yet.\n Please contact an Operator.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if(n1 == 2){
                        Operator user;
                        if((user = StockExchangePlatform.Login(loginfield1.getText(), loginfield2.getText(),"Operator"))!=null){
                            if(user.getApprove()){
                                ClassMenus.displayOperatorMenu(user);
                            } else{
                                JOptionPane.showMessageDialog(null, "This user has not been approved yet.\n Please contact an Operator.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    break;
                case 3:
                    int n3;
                    do {
                        Object[] button = {"Orders", "Lists", "Exit"};
                        n3 = JOptionPane.showOptionDialog(null, "Choose whether you want to see order securities or listed securities.", "Order Book"
                                , JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, button, button[0]);
                        if (n3 == 0) {
                            ArrayList<TraderSecurities> orders = orderbook.getOrders();
                            for (int i = 0; i < orderbook.sizeOrders(); ) {
                                Object[] buttons2 = {"Next", "Exit"};
                                int n4 = JOptionPane.showOptionDialog(null, "PRICE PER UNIT : " + orders.get(i).getPrice() + "\nSUPPLY WANTED : " + orders.get(i).getSupply() + "\nORDER TIME : " + orders.get(i).getTime(),
                                        "ORDERS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons2, buttons2[0]);
                                if (n4 == 0) {
                                    i++;
                                } else {
                                    break;
                                }
                            }
                        } else if (n3 == 1) {
                            ArrayList<ListerSecurities> lists = orderbook.getListed();
                            for (int i = 0; i < orderbook.sizeLists(); ) {
                                Object[] buttons2 = {"Next", "Exit"};
                                int n4 = JOptionPane.showOptionDialog(null, "DESCRIPTION: " + lists.get(i).getDescription() + "\nPRICE PER UNIT : " + lists.get(i).getPrice() + "\nSUPPLY WANTED : " + lists.get(i).getSupply(),
                                        "ORDERS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons2, buttons2[0]);
                                if (n4 == 0) {
                                    i++;
                                } else {
                                    break;
                                }
                            }
                        }
                    }while(n3 == 0 || n3 == 1);
                        break;
                default:
                    System.out.println("Not an option! Please select another option!");
                        quit = true;
                        break;
                }
            }catch(Exception e){
                System.out.println("Not a valid option! Please try again!");
            }

            } while (!quit);
        }



}