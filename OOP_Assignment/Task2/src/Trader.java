import javax.swing.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
    class Trader extends Users {

        private ArrayList<TraderSecurities> securities = new ArrayList<>();

        Trader(String username, String password, String name, String id, boolean approve) {
            super(username, password, name, id, approve);
        }


        static void Buy(Trader user){
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            JTextField field2 = new JTextField();
            Object[] buttons = {"Place Order", "Cancel"};

            JTextField field1 = new JTextField();
            Object[] fields = {"Order quantity : ", field1, "Price per unit : ", field2};

            int n1 = JOptionPane.showOptionDialog(null, fields,"Place an order.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, buttons, buttons[0]);
            if(n1 == 0 && !field1.toString().equals("") && !field2.toString().equals("")){
                try {
                    TraderSecurities order = new TraderSecurities("N/A", Double.parseDouble(field2.getText()), Integer.parseInt(field1.getText()), user, ts);
                    MainMenu.orderbook.addOrder(order);
                    user.addOrder(order);
                } catch(Exception e){
                    System.out.println("An error occurred! Please check your input and try again!");
                }
                if(MatchingEngine.Match()==1){
                    JOptionPane.showMessageDialog(null, "Matching listed Security found!\nClick 'Check Orders' to see your matches!");
                }
            }else if(field1.toString().equals("") || field2.toString().equals("")){
                JOptionPane.showMessageDialog(null, "ERROR. PLEASE FILL IN ALL FIELDS");
            } else{
                JOptionPane.showMessageDialog(null, "No order were placed.");
            }

        }
        //@Override
        private void addOrder(TraderSecurities order){
            this.securities.add(order);
        }
        public void addOrder(Securities sec, Trader trader){
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            TraderSecurities tradersec = new TraderSecurities(sec, trader, ts);
            this.securities.add(tradersec);
        }

        TraderSecurities getOrder(TraderSecurities sec){
            for(int i = 0; i < this.securities.size(); i++){
                if(this.securities.get(i).equals(sec)){
                    return this.securities.get(i);
                }
            }
            return null;
        }
    }

