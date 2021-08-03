import javax.swing.*;

class ClassMenus {
    static void displayTraderMenu(Trader user){
        boolean quit = false;
        do {
            Object[] options = {"Buy", "Check Orders", "Logout"};

            int n = JOptionPane.showOptionDialog(null,
                    "Logged in as " + user.getUsername(),
                    "Trader Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (n == 0) {
                //buy
                Trader.Buy(user);
            } else if (n == 1) {
                //transaction history
                if (user.GetMsg().size() != 0) {
                    for (int i = 0; i < user.GetMsg().size(); i++) {
                        if(!user.GetMsg().get(i).getBuyeragree()) {
                            if (Order.displayMsg(user.GetMsg().get(i)) == 0) {
                                user.GetMsg().get(i).setBuyeragree(true);
                                MatchingEngine.Buy(user.GetMsg().get(i));
                            } else {
                                MatchingEngine.noBuy(user.GetMsg().get(i));
                            }
                        }
                    }
                }
            } else {
                //logout/closed
                quit = true;
            }
        }while(!quit);
    }
    static void displayListerMenu(Lister user) {
        Object[] options = {"List", "Transaction History", "Check Lists", "Logout"};
        boolean quit = false;
        do{
            int n = JOptionPane.showOptionDialog(null,
                    "Logged in as " + user.getUsername(),
                    "Lister Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            System.out.println(n);
            if (n == 0) {
                //list
                //Securities newSecurity;
                Lister.List(user);
            } else if (n == 1) {
                //transaction history
                for (int i = 0; i < user.getListings().size(); ) {
                    ListerSecurities newSecurity = user.getListings().get(i);
                    Object[] buttons = {"Previous", "Next", "Cancel"};
                    int n1 = JOptionPane.showOptionDialog(null, "Security Description : " + newSecurity.getDescription() + "\nPrice per unit : "
                                    + newSecurity.getPrice() + "\nTotal Supply : " + newSecurity.getSupply() + "\nListed? : " + newSecurity.getListed(), "View Previously Listed Securities.", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, buttons, buttons[0]);
                    if (n1 == 0) {
                        i--;
                    } else if (n1 == 1) {
                        i++;
                    } else {
                        break;
                    }
                }
            } else if(n == 2) {
                if (user.GetMsg().size() != 0) {
                    for (int i = 0; i < user.GetMsg().size(); i++) {
                        if(!user.GetMsg().get(i).getSelleragree()) {
                            if (Order.displayMsg(user.GetMsg().get(i)) == 0) {
                                user.GetMsg().get(i).setSelleragree(true);
                                MatchingEngine.Buy(user.GetMsg().get(i));
                            } else {
                                MatchingEngine.noBuy(user.GetMsg().get(i));
                            }
                        }
                    }
                }
            }else{
                //logout/closed
                quit = true;
            }
        }while(!quit);
    }

    static void displayOperatorMenu(Operator user) {
        boolean quit = false;
        do {
            Object[] options = {"Approve Users", "Approve Orders", "Logout"};

            int n = JOptionPane.showOptionDialog(null,
                    "Logged in as " + user.getUsername(),
                    "Operator Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (n == 0) {
                user.Approve();
            } else if(n == 1){
                user.ApproveOrders();
            }else{
                quit = true;
            }
        }while(!quit);
    }
}
