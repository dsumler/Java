public class SemanticAnalyser{
    static int scope = 0;
    static SymbolTable st = new SymbolTable();
    static boolean ret = false;
    static void Semantic(ASTNode root) throws Exception {
        int childcount = 0;
        //get all children from the root node
        while(childcount < root.childrenNumber()) {
            scanStatement(root.getChild(childcount));
            childcount++;
        }
        System.out.println("Statement is semantically correct!");
    }

    static void scanStatement(ASTNode node) throws Exception {
        int childcount = 0;
        while(childcount < node.childrenNumber()) {

            switch (node.getChild(childcount).getData()) {
                    case "LET":
                        scanVarDecl(node.getChild(childcount));
                        break;
                    case "PRINT":
                        //PRINT EXPRESSION
                        scanPrint(node.getChild(0));
                        break;
                    case "RETURN":
                        //RETURN EXPRESSION
                        scanReturn(node.getChild(0));
                        break;
                    case "IF":
                        scanIfStatement(node.getChild(0));
                        break;
                    case "FOR":
                        scanForStatement(node.getChild(0));
                        break;
                    case "WHILE":
                        scanWhileStatement(node.getChild(0));
                        break;
                    case "FF":
                        scanFunctionCall(node.getChild(0));
                        break;
                    default:
                        //must be an identifier
                        scanAssignment(node.getChild(0));
                        break;
                }
            childcount++;

        }
    }

    static void scanIfStatement(ASTNode node) throws Exception {
        scope++;

        //scan the semantics of Expression node
        scanExpression(node.getChild(1));

        //scan the semantics of block node
        scanBlock(node.getChild(3));

        //delete the scope
        st.deleteScope(scope);
        scope--;
    }

    static void scanFunctionCall(ASTNode node) throws Exception {


        st.addRow("Function", node.getChild(5).getChildData(0), node.getChild(0).getChildData(0), scope);

        scope++;
        //scan the semantics of formalparams
        scanFormalParams(node.getChild(2));
        //scan the semantics of block node
        scanBlock(node.getChild(6));
        //if there is no return function in the function, throw and Exception
        if(!ret){
            throw new Exception("Function requires a return statement!");
        }
        ret = false;

        st.deleteScope(scope);
        scope--;
    }
    static void scanFormalParams(ASTNode node){
        int childcount = 0;

        if(node.getChild(0) != null) {
            while (childcount < node.childrenNumber() && node.getChildData(childcount).equals("FormalParam")) {
                //scan the semantics of formal param
                scanFormalParam(node.getChild(childcount));
                childcount++;
            }
        }
    }

    static void scanFormalParam(ASTNode node){

        if(node.childrenNumber() != 0) {
            //add the variable to the symbol table for reference later
            st.addRow("Var", node.getChild(2).getChildData(0), node.getChild(0).getChildData(0), scope);
        }
    }

    static void scanWhileStatement(ASTNode node) throws Exception {
        scope++;
        //scan the semantics of expression node
        scanExpression(node.getChild(1));
        //scan the semantics of block node
        scanBlock(node.getChild(3));
        //delete the current scope from the symbol table
        st.deleteScope(scope);
        scope--;
    }


    static void scanForStatement(ASTNode node) throws Exception {
        int childcount = 1;
        scope++;

        //if a variable declaration is present, scan the semantics of it
        while(childcount < node.childrenNumber() && node.getChild(childcount).getData().equals("LET")) {
            scanVarDecl(node.getChild(childcount));
            childcount+=2;
        }
        //scan the semantics of expression node
        scanExpression(node.getChild(childcount));
        childcount+=2;
        //if an assignment is present, scan the semantics of it
        while(childcount < node.childrenNumber() && node.getChild(childcount).getChildData(0).equals("Identifier")) {
            scanAssignment(node.getChild(childcount));
            childcount+=2;
        }
        //scan the semantics of block node
        scanBlock(node.getChild(childcount));
        //delete the current scope
        st.deleteScope(scope);
        scope--;
    }

    static void scanPrint(ASTNode node) throws Exception {
        //scan the semantics of expression node
        scanExpression(node.getChild(0));
    }

    static void scanAssignment(ASTNode node) throws Exception {
        SymbolTable.Row r;
        //if the variable doesnt exist in the symbol table, throw an Exception
        //because a variable that has not been declared cannot be assigned to
        if((r = st.symboltablelookup("Var", node.getChild(0).getChildData(0), scope)) == null){
            throw new Exception("Variable "+node.getChild(0).getChildData(0)+" does not exist!");
        }
        //check if the type generated by the expression
        //can be put into the variable. i.e. they are of the same type
        //if not, throw an Exception
        if(!r.type.equals(scanExpression(node.getChild(2)))){
            throw new Exception("Wrong type assignment! Cannot assign type "+r.type+" to variable!");
        }
    }

    static void scanVarDecl(ASTNode node) throws Exception {
        //check if the variable has already been declared. If it has, throw an Exception
        if(st.symboltablelookup("Var", node.getChild(0).getChildData(1), scope) != null){
            throw new Exception("Variable "+node.getChild(0).getChildData(1)+" already exists!");
        }

        //add the variable to the symboltable with its name and variable type
        st.addRow("Var", node.getChild(2).getChildData(1), node.getChild(0).getChildData(1), scope);

        //check if the value can be put into the variable (they are the sam type)
        //else, throw an Exception
        if(node.getChild(2).getData().equals("Type")){
            if(!node.getChild(2).getChildData(1).equals(scanExpression(node.getChild(4)))){
                throw new Exception("Variable type error! Expected "+node.getChild(2).getChildData(1)+ " Found : "+scanExpression(node.getChild(4)));
            }
            //if the variable type is "auto", change the type of the variable in the
            //symbol table and AST.
        } else{
            //setting the Auto type to the scanned variable type
            node.getChild(2).getChild(1).setData(scanExpression(node.getChild(4)));
            System.out.println("Variable type has been changed!");
            st.editType("Var", node.getChild(0).getChildData(1), scope, node.getChild(2).getChildData(1));
        }

    }

    static void scanReturn(ASTNode node) throws Exception {
        String type;
        SymbolTable.Row r;

        //if the return statement is not inside a function, throw an exception
        if((r = st.getSymbol(scope, "Function")) == null){
            throw new Exception("Error! Cannot have a return statement outside of a function!");
        }

        //scan the semantics of expression node
        type = scanExpression(node.getChild(0));

        //change the type of the function to the type of the return expression, if the function's type is "auto"
        if(r.type.equals("auto")) {
            st.editType("Function", r.variablename, scope, type);
            node.getChild(0).getChild(0).getChild(0).setData(type);
            System.out.println("Changed function type to " + type);
            ret = true;
        //check that the return type and the function type match
        //if not, throw an exception
        } else if(!r.type.equals(type)){
            throw new Exception("Error! Return statement contains " + type + " while function requires "+r.type);
        }
        //set the return boolean to true
        ret = true;
    }


    //the purpose of this function is to check whether the expression semantics make sense
    //i.e. 5 + 5 is valid, but 5 + true isnt.
    //the returned String is a String detailing the TYPE of the expression.
    static String scanExpression(ASTNode node) throws Exception {
        int childcount = 0;
        String type = "";

        //while there remain some unprocessed children nodes, repeat this
        while(childcount < node.childrenNumber()) {
            if(type.equals("")) {
                ////scan the semantics of simple expression node
                type = scanSimpleExpression(node.getChild(childcount));
                //if an identifier is returned, search for it in the symboltable
                if(type.equals("Identifier")){
                    SymbolTable.Row r;
                    //extract the type from the variable in the symbol table
                    if((r = st.getSymbol(scope, "Var"))!=null){
                        type = r.type;
                    }
                }
            } else{
                //scan the semantics of simple expression
                String type2 = scanSimpleExpression(node.getChild(childcount));
                if(type2.equals("Identifier")){
                    SymbolTable.Row r;
                    if((r = st.getSymbol(scope, "Var"))!=null){
                        type2 = r.type;
                    }
                }
                //if the current type and the type returned from the simple expression function
                //dont match, throw an exception
                if(!type.equals(type2)){
                    throw new Exception("Incorrect types! Cannot combine " + type + " and "+type2 + "!");
                }
            }
            childcount+=2;
        }
        //return the type
        switch(type){
            case "FloatLiteral":
            case "Float":
                return "float";
            case "IntegerLiteral":
            case "Integer":
                return "int";
            case "BooleanLiteral":
            case "Boolean":
                return "bool";
        }
        return type;
    }

    static String scanSimpleExpression(ASTNode node) throws Exception {
        int childcount = 0;
        String type = "";
        while(childcount < node.childrenNumber()) {

            if(type.equals("")) {
                type = scanTerm(node.getChild(childcount));
                if(type.equals("Identifier")){
                    SymbolTable.Row r;
                    if((r = st.getSymbol(scope, "Var"))!=null){
                        type = r.type;
                    }
                }
            } else{
                String type2 = scanTerm(node.getChild(childcount));
                if(type2.equals("Identifier")){
                    SymbolTable.Row r;
                    if((r = st.getSymbol(scope, "Var"))!=null){
                        type2 = r.type;
                    }
                }
                if(!type.equals(type2)){
                    throw new Exception("Incorrect types! Cannot combine " + type + " and "+type2 + "!");
                }
            }
            childcount+=2;
        }
        return type;
    }


    static String scanTerm(ASTNode node) throws Exception {
        int childcount = 0;
        String type = "";
        while(childcount < node.childrenNumber()){

            if(type.equals("")) {
                type = scanFactor(node.getChild(childcount));
                //if the returned string is an identifier, find its variable in the symbol table
                if(type.equals("Identifier")){
                    SymbolTable.Row r;
                    //extract the type from the variable found in the symbol table
                    if((r = st.symboltablelookup("Var", node.getChild(childcount).getChild(0).getChildData(0), scope)) == null){
                        throw new Exception ("Variable "+node.getChild(childcount).getChild(0).getChildData(0)+" does not exist!");
                    } else{
                        type = r.type;
                    }
                    //if the returned string is functioncall
                }else if(type.equals("FunctionCall")){
                    SymbolTable.Row r;
                    //find the function that the functioncall refers to in the symbol table
                    if((r = st.symboltablelookup("Function", node.getChild(childcount).getChild(0).getChild(0).getChildData(0), scope)) == null){
                        throw new Exception ("Function "+node.getChild(childcount).getChild(0).getChild(0).getChildData(0)+" does not exist!");
                    //record the type of the function stored in the symbol table
                    } else{
                        type = r.type;
                    }

                }
            } else{
                //scan the semantics of the factor node
                String type2 = scanFactor(node.getChild(childcount));
                //if the returned string is an identifier, find the corresponding variable
                //in the symbol table and extract its type and place it into the 'type' variable
                if(type2.equals("Identifier")){
                    SymbolTable.Row r;
                    if((r = st.symboltablelookup("Var", node.getChild(childcount).getChild(0).getChildData(0), scope)) == null){
                        throw new Exception ("Variable "+node.getChild(childcount).getChild(0).getChildData(0)+" does not exist!");
                    } else{
                        type2 = r.type;
                    }
                 //if the returned string is FunctionCall, find the corresponding function
                    // in the symbol table and extract its type and place it into the 'type' variable
                } else if(type2.equals("FunctionCall")){
                    SymbolTable.Row r;
                    if((r = st.symboltablelookup("Function", node.getChild(childcount).getChild(0).getChildData(0), scope)) == null){
                        throw new Exception ("Function "+node.getChild(childcount).getChild(0).getChild(0).getChildData(0)+" does not exist!");
                    } else{
                        type2 = r.type;
                    }

                }
                //if the original type and the new type dont match, throw an Exception
                if(!type.equals(type2)){
                    throw new Exception("Incorrect types! Cannot combine " + type + " and "+type2 + "!");
                }
            }

            childcount+=2;

        }

        return type;
    }

    //this function is used to return a string detailing what the TYPE is
    static String scanFactor(ASTNode node) throws Exception {
        int childcount = 0;
        while(childcount < node.childrenNumber()){
            switch(node.getChild(childcount).getData()){


                case "Identifier":

                case "Letter":
                    return "Identifier";
                    //scan the parameters of the functioncall
                case "FunctionCall":
                    int childf = 2;
                    if(node.getChild(childcount).childrenNumber() > 4){
                        scanExpression(node.getChild(childcount).getChild(childf));
                    }
                    childf++;
                    while(childf < node.getChild(childcount).childrenNumber() &&
                            node.getChild(childcount).getChildData(childf).equals(",")){
                        childf++;
                        scanExpression(node.getChild(childcount).getChild(childf));
                        childf++;
                    }
                    return "FunctionCall";
                case "BooleanLiteral":
                    return "BooleanLiteral";

                case "FloatLiteral":
                    return "float";

                case "IntegerLiteral":
                    return "int";

                case "SubExpression":
                    return scanExpression(node.getChild(0).getChild(0));

                case "NOT":
                    return "Unary";
                case "Digit":
                    return "int";

                default:
                    System.exit(0);
            }

            childcount++;


        }
        return "";
    }

    static void scanBlock(ASTNode node) throws Exception {
        int childcount = 0;
        //while there are still unprocessed children stemming from the node,
        //check the semantics of them
        while(childcount < node.childrenNumber()){

            if(node.getChildData(childcount).equals("STATEMENT")) {
                scanStatement(node.getChild(childcount));
            }
            childcount ++;
        }
    }

}