public class XML{
    static int indent = 0;
    //function used to add tabs to the XML output
    static void addTab(int indent){
        for(int i = 0; i < indent; i++){
            System.out.print("    ");
        }
    }

    //function used to generate the XML
    static void GenerateXML(ASTNode root){
        int childcount = 0;

        System.out.println("<PROGRAM>");
        indent++;

        //for every child stemming from the node, generate an XML statement
        while(childcount < root.childrenNumber()) {

            genStatement(root.getChild(childcount));
            childcount++;
        }
        System.out.println("</PROGRAM>");

    }
    //function used to generate the XML of a statement
    static void genStatement(ASTNode node){

        int childcount = 0;
        int statementindent = indent;
        addTab(indent);
        //iterate through all the children and choose which function to call depending on the type of node
        while(childcount < node.childrenNumber()) {
            System.out.println("<STATEMENT>");
            indent++;

            if(node.getChildData(childcount).equals("Identifier")){
                genAssignment(node);
                break;
            } else {
                switch (node.getChild(childcount).getData()) {
                    case "LET":
                        genVarDecl(node.getChild(childcount));
                        break;
                    case "PRINT":
                        //PRINT EXPRESSION
                        genPrint(node.getChild(0));
                        break;
                    case "RETURN":
                        //RETURN EXPRESSION
                        genReturn(node.getChild(0));
                        break;
                    case "IF":
                        genIfStatement(node.getChild(0));
                        break;
                    case "FOR":
                        genForStatement(node.getChild(0));
                        break;
                    case "WHILE":
                        genWhileStatement(node.getChild(0));
                        break;
                    case "FF":
                        genFunctionCall(node.getChild(0));
                        break;
                    default:
                        genAssignment(node.getChild(0));
                        break;
                }
            }
            childcount++;

        }
        //return the indent to normal and output the closing tag
        indent = statementindent;
        addTab(statementindent);
        System.out.println("</STATEMENT>");
    }

    //function used to generate the if statement XML
    static void genIfStatement(ASTNode node){

        addTab(indent);
        int ifindent = indent;

        System.out.println("<IfStatement>");
        indent++;
        //display the Expression XML
        genExpression(node.getChild(1));

        indent--;
        //display the Block XML
        genBlock(node.getChild(3));
        //if an else statement is present,
        if(node.childrenNumber() >= 5){
            if(node.getChildData(4).equals("else")){
                int elseindent = indent;
                addTab(indent);
                //display the else tag
                System.out.println("<Else>");
                indent ++;
                //display the block XML
                genBlock(node.getChild(5));
                indent = elseindent;
                addTab(indent);
                System.out.println("<Else>");
            }
        }

        indent=ifindent;
        addTab(ifindent);
        //close the if statement tag
        System.out.println("</IfStatement>");
    }

    static void genFunctionCall(ASTNode node){
        int functionindent = indent;
        addTab(indent);
        //open the functioncall tag and extract the data needed from the node
        System.out.println("<FunctionCall Type = \""+ node.getChild(5).getChildData(0) +"\">");
        indent++;

        addTab(indent);

        indent++;
        //open tag
        System.out.println("<FunctionName>");
        addTab(indent);
        //display the required information from the node
        System.out.println("<Id>"+node.getChild(0).getChildData(0)+"</Id>");
        indent--;
        addTab(indent);
        //close tag
        System.out.println("</FunctionName>");
        //display the formalparams
        genFormalParams(node.getChild(2));

        indent++;
        //display the block
        genBlock(node.getChild(6));
        indent--;

        addTab(functionindent);
        indent = functionindent;
        //close tag
        System.out.println("</FunctionCall>");

    }
    static void genFormalParams(ASTNode node){
        int childcount = 0;
        int formalindent = indent;
        addTab(indent);
        //open tag
        System.out.println("<FormalParams>");
        indent++;
        //while there are formalparams present in the node, call the function to display them in XML format
        while(childcount < node.childrenNumber() && node.getChildData(childcount).equals("FormalParam")){
            genFormalParam(node.getChild(childcount));
            childcount++;
        }
        //return the indent to normal
        addTab(formalindent);
        indent = formalindent;
        //close tag
        System.out.println("</FormalParams>");
    }

    static void genFormalParam(ASTNode node){
        int formalindent = indent;
        addTab(indent);
        //open tag
        System.out.println("<FormalParam>");
        indent++;
        //display the required information by exracting it from the node
        addTab(indent);
        System.out.println("<Var Type = \""+ node.getChild(2).getChildData(0) +"\">"+node.getChild(0).getChildData(0)+"</Id>");
        //return the indent to normal
        addTab(formalindent);
        indent = formalindent;
        //close tag
        System.out.println("</FormalParam>");

    }

    static void genWhileStatement(ASTNode node){
        addTab(indent);
        int whileindent = indent;
        //open tag
        System.out.println("<WhileStatement>");
        indent++;
        //display the expression
        genExpression(node.getChild(1));
        indent--;
        indent++;
        //display the block
        genBlock(node.getChild(3));
        indent--;
        //return indent to normal
        indent=whileindent;
        addTab(whileindent);
        //close tag
        System.out.println("</WhileStatement>");
    }


    static void genForStatement(ASTNode node){
        int childcount = 1;
        addTab(indent);
        //open tag
        System.out.println("<ForStatement>");
        int forindent = indent;

        //if there is a variable declaration present, display it
        if(childcount < node.childrenNumber() && node.getChild(childcount).getData().equals("LET")) {
            genVarDecl(node.getChild(childcount));
            indent--;
            childcount+=2;
        }

        indent++;
        addTab(indent);
        //expression tag
        System.out.println("<Expression>");
        indent++;
        //display expression
        genExpression(node.getChild(childcount));
        childcount+=2;
        indent --;
        addTab(indent);
        //close expression tag
        System.out.println("</Expression>");

        //if there is an assignment present, display it
        if(childcount < node.childrenNumber() && node.getChild(childcount).getData().equals("Assignment")) {
            genAssignment(node.getChild(childcount));
            indent-=2;
            childcount+=2;
        }

        indent++;
        //display the block
        genBlock(node.getChild(childcount));
        indent--;

        //return indent to normal
        indent = forindent;
        addTab(indent);
        //close tag
        System.out.println("</ForStatement>");
    }

    static void genPrint(ASTNode node){
        addTab(indent);
        int printindent = indent;
        //open tag
        System.out.println("<Print>");
        indent++;
        //display expression
        genExpression(node.getChild(0));
        indent--;
        //close tag
        addTab(printindent);
        System.out.println("</Print>");

    }

    static void genAssignment(ASTNode node){
        addTab(indent);
        int assignmentindent = indent;
        //open tag
        System.out.println("<Assignment>");
        indent++;
        addTab(indent);
        //output the require information acquired through the node
        System.out.println("<Id>"+node.getChild(0).getChildData(0)+"</Id>");
        addTab(indent);
        int assignmentopindent = indent;
        //output the require information acquired through the node
        System.out.println("<AssignmentOp = \""+node.getChildData(1)+"\">");

        indent++;

        genExpression(node.getChild(2));

        indent--;
        addTab(assignmentopindent);
        //close tags
        System.out.println("</AssignmentOp>");
        addTab(assignmentindent);
        System.out.println("</Assignment>");
    }

    static void genVarDecl(ASTNode node){
        indent++;
        addTab(indent);
        //open tag
        System.out.println("<VarDecl>");
        int declindent = indent;
        indent++;
        //extract the required information from the node
        String identifier = node.getChild(0).getChildData(1);
        String type = node.getChild(2).getChildData(1);
        addTab(indent);
        //display the information from the node
        System.out.println("<Var Type = \""+type+"\">"+identifier+"</Id>");
        //display the expression
        genExpression(node.getChild(4));
        //return indent to normal
        indent = declindent;
        addTab(declindent);
        //close tag
        System.out.println("</VarDecl>");
    }

    static void genReturn(ASTNode node){
        addTab(indent);
        int returnindent = indent;
        //open tag
        System.out.println("<Return>");
        indent++;
        //display expression
        genExpression(node.getChild(0));
        //return indent to normal
        indent = returnindent;
        addTab(returnindent);
        //close tag
        System.out.println("</Return>");
    }


    static void genExpression(ASTNode node){
        int childcount = 0;
        //while there are children present in the node
        while(childcount < node.childrenNumber()) {

            //if there is more than one child in the node, this signifies that there is an operator.
            if (node.childrenNumber() > 1) {
                for(int i = childcount; i < node.childrenNumber(); i++){
                    //display the operator
                    if(node.getChildData(i).equals("RelationalOp")){
                        addTab(indent);
                        System.out.println("<BinExprNode Op = \"" + node.getChild(i).getChildData(0) + "\">");
                        indent++;
                        break;
                    }
                }
            }
            //display the SimpleExpression
            genSimpleExpression(node.getChild(childcount));
            childcount+=2;
        }
        //if an operator tag was opened, close it
        if(node.childrenNumber() > 1){
            indent --;
            addTab(indent);
            System.out.println("</BinExpNode>");
        }
    }

    static void genSimpleExpression(ASTNode node){
        int childcount = 0;
        while(childcount < node.childrenNumber()) {
            //if the node has more than one child, this signifies that there is an operator
            if(node.childrenNumber() > 1) {
                for (int i = childcount; i < node.childrenNumber(); i++) {
                    //display the operator
                    if (node.getChildData(i).equals("AdditiveOp")) {
                        addTab(indent);
                        System.out.println("<BinAddNode Op = \"" + node.getChild(i).getChildData(0) + "\">");
                        indent++;
                        break;
                    }
                }

            }
            //display the term
            genTerm(node.getChild(childcount));
            childcount+=2;

        }
        //if an operator tag was opened, close it
        if(node.childrenNumber() > 1){
            indent --;
            addTab(indent);
            System.out.println("</BinAddNode>");
        }
    }


    static void genTerm(ASTNode node){
        int childcount = 0;
        while(childcount < node.childrenNumber()){

            //if the node has more than one child, this signifies that there is an operator
            for(int i = childcount; i < node.childrenNumber(); i++){
                if(node.getChildData(i).equals("MultiplicativeOp")){
                    addTab(indent);
                    System.out.println("<BinMultNode Op = \"" + node.getChild(i).getChildData(0) + "\">");
                    indent++;

                    break;
                }

            }
            //display the factor
            genFactor(node.getChild(childcount));
            childcount+=2;
        }
        //if an operator tag was opened, close it
        if(node.childrenNumber() > 1){
            indent --;
            addTab(indent);
            System.out.println("</BinMultNode>");
        }
    }
    //function used to display factors
    static void genFactor(ASTNode node) {
        int childcount = 0;
            while(childcount < node.childrenNumber()){
                switch(node.getChild(childcount).getData()){

                    case "Identifier":
                        addTab(indent);
                        System.out.println("<Id>\""+node.getChild(childcount).getChildData(0)+"\"</Id>");
                        break;
                    case "BooleanLiteral":
                        addTab(indent);
                        System.out.println("<BooleanLiteral>\""+node.getChild(childcount).getChildData(0)+"\"</BooleanLiteral>");
                        break;
                    case "FloatLiteral":
                        addTab(indent);
                        System.out.println("<FloatLiteral>\""+node.getChild(childcount).getChildData(0)+"\"</FloatLiteral>");
                        break;
                    case "IntegerLiteral":
                        addTab(indent);
                        System.out.println("<IntegerLiteral>\""+node.getChild(childcount).getChildData(0)+"\"</IntegerLiteral>");
                        break;
                    case "OPN_BRACKET":
                        addTab(indent);
                        genExpression(node.getChild(childcount+1));
                        //SubExpression
                        break;
                    case "NOT":
                        addTab(indent);
                        System.out.println("<Unary>\""+node.getChild(childcount).getChildData(0)+"\"</Unary>");
                        break;
                    case "Digit":
                        addTab(indent);
                        System.out.println("<Digit>\""+node.getChild(childcount).getChildData(0)+"\"</Digit>");
                        break;
                    case "Letter":
                        addTab(indent);
                        System.out.println("<Letter>\""+node.getChild(childcount).getChildData(0)+"\"</Letter>");
                        break;
                    default:
                        System.exit(0);
                }
                childcount++;
            }
    }

    static void genBlock(ASTNode node){
        int childcount = 0;
        int blockindent = indent;
        addTab(indent);
        //open tag
        System.out.println("<Block>");
        indent ++;
        //while there are still children nodes stemming from the block node,
        //display them using the genStatement function
        while(childcount < node.childrenNumber()){

            if(node.getChildData(childcount).equals("STATEMENT")) {
                genStatement(node.getChild(childcount));
            }

            childcount ++;
        }
        addTab(blockindent);
        //close tag
        System.out.println("</Block>");
    }

}