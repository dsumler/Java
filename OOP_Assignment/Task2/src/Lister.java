import javax.swing.*;
import java.util.ArrayList;

class Lister extends Users {
    ArrayList<ListerSecurities> securities = new ArrayList<>();

    Lister(String username, String password, String name, String id, boolean approve) {
        super(username, password, name, id, approve);
    }

    static void List(Lister user){
        JTextField field2 = new JTextField("");
        JTextField field3 = new JTextField("");
        JTextField field4 = new JTextField("");
        Object[] options = {"List", "Cancel"};

        Object[] fields = {"Security Description : ", field2, "Price: ", field3, "Total Supply : ", field4};
        int n = JOptionPane.showOptionDialog(null, fields, "List Security", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(n == 0) {
            try {
                ListerSecurities newsec = new ListerSecurities(field2.getText(), Double.parseDouble(field3.getText()), Integer.parseInt(field4.getText()), user);
                MainMenu.orderbook.addList(newsec);
                user.addListing(newsec);
                if(MatchingEngine.Match()==1){
                    JOptionPane.showMessageDialog(null, "Matching Security Found!\nClick 'Check Lists' to view!");
                }
            }catch(Exception e){
                System.out.println("An error occurred! Please check your input and try again!");
            }
            //return new listSecurities(field2.getText(), Double.parseDouble(field3.getText()), Integer.parseInt(field4.getText()), lister);
        }
    }
    ArrayList<ListerSecurities> getListings(){
        return this.securities;
    }

    private void addListing(ListerSecurities sec){
        this.securities.add(sec);
    }

    ListerSecurities getListing(ListerSecurities sec){
        for(int i = 0; i < this.securities.size(); i++){
            if(this.securities.get(i).equals(sec)){
                return this.securities.get(i);
            }
        }
        return null;
    }

}