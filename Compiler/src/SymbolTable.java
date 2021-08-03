import java.util.ArrayList;

public class SymbolTable {
    ArrayList<Row> stable = new ArrayList<>();


    public static class Row{
        String classtype = "";
        String type = "";
        String variablename = "";
        int scope;

        public Row(String classtype, String type, String variablename, int scope){
            this.classtype = classtype;
            this.variablename = variablename;
            this.type = type;
            this.scope = scope;
        }

        boolean lookup(String classtype, String variablename, int scope){
            return this.classtype.equals(classtype) && variablename.equals(this.variablename) && this.scope <= scope;
        }

    }

    void addRow(String classtype, String type, String variablename, int scope){
        this.stable.add(new Row(classtype, type, variablename, scope));
    }

    Row symboltablelookup(String classtype, String variablename, int scope){
        for(int i = 0; i < this.stable.size(); i++){
            if(this.stable.get(i).lookup(classtype, variablename, scope)){
                return this.stable.get(i);
            }
        }
        return null;
    }
    void editType(String classtype, String variablename, int scope, String type){
        int index = 0;
        for(int i = 0; i < this.stable.size(); i++){
            if(this.stable.get(i).lookup(classtype, variablename, scope)){
                index = i;
                break;
            }
        }
        this.stable.remove(index);
        this.stable.add(new Row(classtype, type, variablename, scope));
    }
    void deleteScope(int scope){
        ArrayList<Row> remove = new ArrayList<>();
        for(int i = 0; i < this.stable.size(); i++){
            if(this.stable.get(i).scope == scope){
                remove.add(this.stable.get(i));
            }
        }
        for(int i = 0; i < remove.size(); i++){
            this.stable.remove(remove.get(i));
        }

    }
    Row getSymbol(int scope, String classtype){
        for(int i = 0; i < this.stable.size(); i++){
            if(this.stable.get(i).scope == scope && this.stable.get(i).classtype.equals(classtype)){
                return this.stable.get(i);
            }
        }
        return null;
    }




}
