
class ListerSecurities extends Securities{
    private Lister lister;

    ListerSecurities(String description, Double price, int ts, Lister lister) {
        super(description, price, ts);
        this.lister = lister;
    }

    Lister getLister(){
        return this.lister;
    }
    public void setLister(Lister lister){
        this.lister = lister;
    }

}