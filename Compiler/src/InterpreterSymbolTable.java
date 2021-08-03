import java.util.ArrayList;

public class InterpreterSymbolTable {

    ArrayList<Row> stable = new ArrayList<>();


    public static class Row{
        ASTNode func = new ASTNode();


        String classtype = "";
        String type = "";
        String variablename = "";
        String value = "";
        String retval = "";
        int scope;

        public Row(String classtype, String type, String variablename, int scope, String value){
            this.classtype = classtype;
            this.variablename = variablename;
            this.type = type;
            this.value = value;
            this.scope = scope;
        }
        public Row(String classtype, String type, String variablename, int scope, ASTNode node){
            this.classtype = classtype;
            this.variablename = variablename;
            this.type = type;
            this.func = node;
            this.scope = scope;
        }
        public Row(){}

        void addFunc(ASTNode node){
            this.func = node;
        }

        boolean lookup(String classtype, String variablename, int scope){
            return this.classtype.equals(classtype) && variablename.equals(this.variablename) && this.scope <= scope;
        }

    }
    void addFuncRow(String classtype, String type, String variablename, int scope, ASTNode node){
        Row r = new Row(classtype, type, variablename, scope, node);
        this.stable.add(new Row(classtype, type, variablename, scope, node));
    }

    void addRow(String classtype, String type, String variablename, int scope, String value){
        this.stable.add(new Row(classtype, type, variablename, scope, value));
    }



    Row symboltablelookup(String classtype, String variablename, int scope){
        for(int i = 0; i < this.stable.size(); i++){
            if(this.stable.get(i).lookup(classtype, variablename, scope)){
                return this.stable.get(i);
            }
        }
        return null;
    }
    void editValue(String classtype, String variablename, int scope, String value){
        int index = 0;
        for(int i = 0; i < this.stable.size(); i++){
            if(this.stable.get(i).lookup(classtype, variablename, scope)){
                index = i;
                break;
            }
        }
        Row r = this.stable.get(index);
        int s = r.scope;
        String type = r.type;
        this.stable.remove(index);
        this.stable.add(new Row(classtype, type, variablename, s, value));
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
    Row getSymbol(int scope, String classtype, String variablename){
        if(classtype.equals("Function")) {
            for (int i = 0; i < this.stable.size(); i++) {
                if (this.stable.get(i).scope == scope && this.stable.get(i).classtype.equals(classtype)) {
                    return this.stable.get(i);
                }
            }
        } else if(classtype.equals("Var")){
            for (int i = 0; i < this.stable.size(); i++) {
                if (this.stable.get(i).scope <= scope && this.stable.get(i).classtype.equals(classtype) && this.stable.get(i).variablename.equals(variablename)){
                    return this.stable.get(i);
                }
            }
        }
        return null;
    }




}
