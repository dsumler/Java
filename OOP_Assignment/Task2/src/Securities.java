import java.sql.Time;
import java.util.ArrayList;
import java.sql.Timestamp;

class Securities {
    boolean listed = true;
    boolean sold = false;
    String description;
    Double price;
    int total_supply;
    //Users creator;

    Securities(String description, Double price, int ts){
        this.description = description;
        this.price = price;
        this.total_supply = ts;
        //this.creator = creator;
    }
    String getDescription(){

        return this.description;
    }
    Double getPrice(){

        return this.price;
    }
    void setListed(boolean listed){
        this.listed = listed;
    }
    boolean getListed(){
        return this.listed;
    }
    void setSupply(int supply){
        this.total_supply = supply;
    }
    int getSupply(){
        return this.total_supply;
    }
}
