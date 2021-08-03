import java.util.ArrayList;

abstract class Users {
    private String username;
    private String password;
    private String name;
    private String idno;
    private boolean approved = false;
    private ArrayList<Order> msg = new ArrayList<>();

    Users(String username, String password, String name, String id, boolean approve){
        this.setUsername(username);
        this.setPassword(password);
        this.setName(name);
        this.setID(id);
        this.Approve(approve);
    }

    private void setUsername(String uname){
        this.username = uname;
    }
    String getUsername(){
        return this.username;
    }
    private void setPassword(String pass){
        this.password = pass;
    }
    String getPassword(){
        return this.password;
    }
    private void setName(String firstname){
        this.name = firstname;
    }
    String getName(){
        return this.name;
    }
    private void setID(String idnum){
        this.idno = idnum;
    }
    String getID(){
        return this.idno;
    }
    void Approve(boolean app){
        this.approved = app;
    }
    boolean getApprove(){
        return this.approved;
    }
    void AddMsg(Order msg){
        this.msg.add(msg);
    }
    ArrayList<Order> GetMsg(){
        return this.msg;
    }
}