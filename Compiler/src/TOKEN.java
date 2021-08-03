public class TOKEN {
    TKN token;
    String stringvalue;

    public TOKEN(){ }

    public void setString(String stringval){
        this.stringvalue = stringval;
    }

    public void setToken(TKN token){
        this.token = token;
    }

    public TKN getToken(){
        return this.token;
    }
    public String getString() { return this.stringvalue; }

    boolean isEmpty(){
        if(this.token == null){
            return true;
        } else{
            return false;
        }
    }

}
