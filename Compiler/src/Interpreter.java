public class Interpreter{
    static int scope = 0;
    static InterpreterSymbolTable st = new InterpreterSymbolTable();

    static void Interpret(ASTNode root) throws Exception {
        int childcount = 0;
        //get all children from the root node
        while(childcount < root.childrenNumber()) {
            runStatement(root.getChild(childcount));
            childcount++;
        }
        System.out.println("Program run complete!");
    }

    static void runStatement(ASTNode node) throws Exception {
        int childcount = 0;
        while(childcount < node.childrenNumber()) {

            switch (node.getChild(childcount).getData()) {
                case "LET":
                    runVarDecl(node.getChild(childcount));
                    break;
                case "PRINT":
                    runPrint(node.getChild(0));
                    break;
                case "RETURN":
                    runReturn(node.getChild(0));
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
                    runFunctionDecl(node.getChild(0));
                    break;
                default:
                    //must be an identifier
                    runAssignment(node.getChild(0));
                    break;
            }
            childcount++;

        }
    }

    static void scanIfStatement(ASTNode node) throws Exception {
        //in a new scope
        scope++;
        //if the condition of the if statement evaluated to true,
        //run the contents of the block
        if(runExpression(node.getChild(1)).equals("true")){
            runBlock(node.getChild(3));
            //if the condition of the if statement evaluated to false
            //and there exists an else statement, run the else block
        } else if(node.childrenNumber() >= 5){
            if(node.getChildData(4).equals("else")) {
                runBlock(node.getChild(5));
            }
        }
        //delete the current scope from the symboltable
        st.deleteScope(scope);
        scope--;
    }


    static void runFunctionDecl(ASTNode node){
        //add the function to the symbol table, with information extracted from the AST node
        st.addFuncRow("Function", node.getChild(5).getChildData(0), node.getChild(0).getChildData(0), scope, node.getChild(6));
    }
    static void runFunctionCall(int scope, String variablename) throws Exception {
        scope++;
        //obtain the function data from the symboltable using the name supplied by the FunctionCall
        InterpreterSymbolTable.Row r = st.getSymbol(scope, "Function", variablename);
        //generate the previously stored block
        runBlock(r.func);
        //delete the current scope
        st.deleteScope(scope);
        scope--;
    }
    static void runFormalParams(ASTNode node){
        int childcount = 0;

        if(node.childrenNumber() != 1) {
            while (childcount < node.childrenNumber() && node.getChildData(childcount).equals("FormalParam")) {
                runFormalParam(node.getChild(childcount));
                childcount++;
            }
        }
    }

    static void runFormalParam(ASTNode node){

        System.out.println("<Var Type = \""+ node.getChild(2).getChildData(0) +"\">"+node.getChild(0).getChildData(0)+"</Id>");
        System.out.println("var name: "+node.getChild(0).getChildData(0));

    }

    static void scanWhileStatement(ASTNode node) throws Exception {
        scope++;
        //while the condition of the while statement evaluates to true,
        //run the block node
        while(runExpression(node.getChild(1)).equals("true")) {
            runBlock(node.getChild(3));
        }
        //delete the current scope
        st.deleteScope(scope);
        scope--;
    }


    static void scanForStatement(ASTNode node) throws Exception {
        scope++;
        //if there exists a variable declaration, execute it
        if(node.getChildData(1).equals("LET"))
            runVarDecl(node.getChild(1));
        //while the for condition evaluates to true, execute the block and the assignment statement
        while(runExpression(node.getChild(3)).equals("true")){
            runBlock(node.getChild(7));
            runAssignment(node.getChild(5));
        }
        //delete the current scope
        st.deleteScope(scope);
        scope--;
    }

    static void runPrint(ASTNode node) throws Exception {
        //print the executed expression node
        System.out.println("Print : "+runExpression(node.getChild(0)));

    }

    static void runAssignment(ASTNode node) throws Exception {
        //calculate the value of the expression node
        String val = runExpression(node.getChild(2));
        //edit the value of the existing variable, setting the value to the one obtained from the expression evaluation
        st.editValue("Var", node.getChild(0).getChildData(0), scope, val);
    }

    static void runVarDecl(ASTNode node) throws Exception {
        //calculate the value of the expression node from the variable declaration
        String val = runExpression(node.getChild(4));
        //add the variable to the symbol table, using the previously evaluated value
        st.addRow("Var", node.getChild(2).getChildData(1), node.getChild(0).getChildData(1), scope, val);
    }

    static void runReturn(ASTNode node) throws Exception {
        String type;
        InterpreterSymbolTable.Row r;

        r = st.getSymbol(scope, "Function", "");

        type = runExpression(node.getChild(0));

    }


    static String runExpression(ASTNode node) throws Exception {
        int childcount = 0;
        //create a string array. One index will be used to store the TYPE
        //of the value, and the other will be used to store the VALUE
        String[] t = new String[2];
        String total = "";
        while(childcount < node.childrenNumber()) {
            if(childcount == 0) {
                //evaluate the expression node and store the value in t
                t = runSimpleExpression(node.getChild(childcount));
            }
            //if there are no multiplicative ops in the expression, return it
            if(node.childrenNumber() == 1) {
                return t[0];
            } else if(childcount < node.childrenNumber() - 1){
                //store the first value
                String value2 = t[0];
                //get the multiplicative op
                String m = node.getChild(childcount+1).getChildData(0);
                childcount+=2;
                //evaluate the second value in the expression
                t = runSimpleExpression(node.getChild(childcount));

                //add the first value and second value using the addition function
                //and store the value in the total variable
                if(!total.equals("")){
                    t[0] = addition(m, total, t[0], t[1]);
                } else {
                    t[0] = addition(m, value2, t[0], t[1]);
                }
                total = t[0];
            } else {
                //return the value once all values have been evaluated
                return t[0];
            }
        }
        return t[0];
    }

    static String[] runSimpleExpression(ASTNode node) throws Exception {
        int childcount = 0;
        String[] t = new String[2];
        String total = "";

        while(childcount < node.childrenNumber()) {
            if(childcount == 0)
            t = runTerm(node.getChild(childcount));

            if(node.childrenNumber() == 1) {
                return t;
            } else if(childcount < node.childrenNumber() - 1){
                String value2 = t[0];
                //get the multiplicative op
                String m = node.getChild(childcount+1).getChildData(0);
                childcount+=2;
                t = runTerm(node.getChild(childcount));


                if(!total.equals("")){
                    t[0] = addition(m, total, t[0], t[1]);
                } else {
                    t[0] = addition(m, value2, t[0], t[1]);
                }
                total = t[0];
            } else{
                return t;
            }
        }
        return t;
    }


    static String[] runTerm(ASTNode node) throws Exception {
        int childcount = 0;
        String t = "";
        String value = "";
        String tot = "";
        String[] total = new String[2];
        while(childcount < node.childrenNumber()){
            if(childcount == 0) {
                //get the type of the factor from the factor method
                t = runFactor(node.getChild(childcount));
            }
            if (t.equals("Identifier")) {
                InterpreterSymbolTable.Row r;
                //if the type is an identifier, extract the type and current value of the variable
                //from the symbol table
                r = st.getSymbol(scope, "Var", node.getChild(childcount).getChild(0).getChildData(0));
                value = r.value;
                t = r.type;
            } else{
                //else, extract the value manually
                value = node.getChild(childcount).getChild(0).getChildData(0);
            }
            //if there is only one child, return the type and value in string form
            if(node.childrenNumber() == 1) {
                total[0] = value;
                total[1] = t;
                return total;
            } else if(childcount < node.childrenNumber() - 1){
                total[0] = value;
                total[1] = t;

                //get the multiplicative op
                String m = node.getChild(childcount+1).getChildData(0);
                childcount+=2;
                //manually extract the value
                value = node.getChild(childcount).getChild(0).getChildData(0);
                //use the addition function to perform the arithmetic operation
                //on the two values and add them to the total variable
                if(!tot.equals("")){
                    total[0] = addition(m, tot, total[0], total[1]);
                } else {
                    total[0] = addition(m, total[0], value, total[1]);
                }
                tot = total[0];

            } else{
                //return the type and value
                return total;
            }
        }
        return total;
    }

    //function used to perform arithmetic operations on two strings
    //by parsing them into their intended type and performing the arithmetic operation on them
    static String addition(String operator, String v1, String v2, String type){
        switch(operator){
            case "*":
                switch(type){
                    case "int":
                        int vi = Integer.parseInt(v1) * Integer.parseInt(v2);
                        return Integer.toString(vi);
                    case "float":
                        double vd = Double.parseDouble(v1) * Double.parseDouble(v2);
                        return Double.toString(vd);
                }
                break;
            case "/":
                switch(type){
                    case "int":
                        int vi = Integer.parseInt(v1) / Integer.parseInt(v2);
                        return Integer.toString(vi);
                    case "float":
                        double vd = Double.parseDouble(v1) / Double.parseDouble(v2);
                        return Double.toString(vd);
                }
                break;
            case "+":
                switch(type){
                    case "int":
                        int vi = Integer.parseInt(v1) + Integer.parseInt(v2);
                        return Integer.toString(vi);
                    case "float":
                        double vd = Double.parseDouble(v1) + Double.parseDouble(v2);
                        return Double.toString(vd);
                }
                break;
            case"-":
                switch(type){
                    case "int":
                        int vi = Integer.parseInt(v1) - Integer.parseInt(v2);
                        return Integer.toString(vi);
                    case "float":
                        double vd = Double.parseDouble(v1) - Double.parseDouble(v2);
                        return Double.toString(vd);
                }
                break;
            case "<":
                switch(type){
                    case "int":
                        boolean vi = Integer.parseInt(v1) < Integer.parseInt(v2);
                        return Boolean.toString(vi);
                    case "float":
                        boolean vd = Double.parseDouble(v1) < Double.parseDouble(v2);
                        return Boolean.toString(vd);
                }
                break;
            case ">":
                switch(type){
                    case "int":
                        boolean vi = Integer.parseInt(v1) > Integer.parseInt(v2);
                        return Boolean.toString(vi);
                    case "float":
                        boolean vd = Double.parseDouble(v1) > Double.parseDouble(v2);
                        return Boolean.toString(vd);
                }
                break;
            case "==":
                switch(type){
                    case "int":
                        boolean vi = Integer.parseInt(v1) == Integer.parseInt(v2);
                        return Boolean.toString(vi);
                    case "float":
                        boolean vd = Double.parseDouble(v1) == Double.parseDouble(v2);
                        return Boolean.toString(vd);
                }
                break;
            case "<=":
                switch(type){
                    case "int":
                        boolean vi = Integer.parseInt(v1) <= Integer.parseInt(v2);
                        return Boolean.toString(vi);
                    case "float":
                        boolean vd = Double.parseDouble(v1) <= Double.parseDouble(v2);
                        return Boolean.toString(vd);
                }
                break;
            case ">=":
                switch(type){
                    case "int":
                        boolean vi = Integer.parseInt(v1) >= Integer.parseInt(v2);
                        return Boolean.toString(vi);
                    case "float":
                        boolean vd = Double.parseDouble(v1) >= Double.parseDouble(v2);
                        return Boolean.toString(vd);
                }
                break;
        }
        return null;
    }

    //function used to find the type of factor and return it in the form of a string
    static String runFactor(ASTNode node) throws Exception {
            switch(node.getChild(0).getData()){

                case "Identifier":
                    return "Identifier";
                case "FunctionCall":
                    //execute the functioncall
                    runFunctionCall(scope, node.getChild(0).getChild(0).getChildData(0));
                    return "FunctionCall";
                case "BooleanLiteral":
                    return "BooleanLiteral";

                case "FloatLiteral":
                    return "float";

                case "IntegerLiteral":
                    return "int";

                case "SubExpression":
                    return runExpression(node.getChild(0).getChild(0));

                case "NOT":
                    return "Unary";
                case "Digit":
                    return "int";

                case "Letter":
                    return "Identifier";

                default:
                    System.exit(0);
            }

        return "";
    }
    //execute the block from the node given
    static void runBlock(ASTNode node) throws Exception {
        int childcount = 1;
        //while there are still children stemming from the block node
        //which have not been executed, execute them.
        while(childcount < node.childrenNumber()){

            if(!node.getChildData(childcount).equals("}")) {
                runStatement(node.getChild(childcount));
            }
            childcount ++;
        }
    }

}