import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Parser {
    private static TOKEN currentToken;
    //creating an instance of the lexer
    static Lexer l;
    //this function takes the next token from the lexer and makes sure its ready to be parsed
    static TOKEN getNextToken() throws Exception {
        currentToken = l.NextWord();
        //if the token is invalid, throw an exception
        if(currentToken.getToken() == TKN.Invalid){
            throw new Exception("Unidentified symbol found! "+currentToken.getString() +" is not a valid symbol!");
        }
        //if there are no more tokens, set the current string to empty
        if(currentToken.getToken() == TKN.EPSILON){
            currentToken.setString("");
        }
        //return the token
        return currentToken;
    }

    static ASTNode parseSubExpression() throws Exception {
        ASTNode n = new ASTNode();
        n.data = "SubExpression";

        n.addChild(parseExpression());
        //currentToken = getNextToken();
        //System.out.println(currentToken.getToken());
        if(currentToken.getToken() == TKN.CLOSE_BRACKET){
            return n;
        } else{

            throw new Exception("Expected ')'!");
        }

    }


    //function to parse the ActualParams
    static ASTNode parseActualParams() throws Exception {
        ASTNode node = new ASTNode();
        //set the node's data to ActualParams for the AST
        node.data = "ActualParams";

        currentToken = getNextToken();
            //checking the syntax. if the syntax is incorrect, throw an Exception
            if(currentToken.getToken() == TKN.CLOSE_BRACKET){
                node.addChild(new ASTNode(")", null));
                return node;
            } else {
                node.addChild(parseExpression());
                while (currentToken.getToken() == TKN.COMMA) {
                    node = parseExpression();
                }
                //if the syntax is correct, return the node
                return node;
            }
    }

    //function of creating a new functioncall node and adding it to the AST
    static ASTNode parseFunctionCall(String name) throws Exception {
        ASTNode node = new ASTNode();
        //naming the node FunctionCall and adding the function's name as a child
        node.data = "FunctionCall";
        node.addChild(new ASTNode("Identifier", new ASTNode(name, null)));

        //checking the syntax of the function call. If the syntax is incorrect, throw an Exception
        if(currentToken.getToken() == TKN.OPN_BRACKET) {
            node.addChild(new ASTNode("(", null));
            node.addChild(parseActualParams());
            if (currentToken.getToken() == TKN.CLOSE_BRACKET) {
                node.addChild(new ASTNode(")", null));
            } else throw new Exception("Missing ')'");
        }


        return node;
    }

    //function to parse an Expression and add it to the AST
    static ASTNode parseExpression() throws Exception {
        ASTNode node = new ASTNode();
        //naming the node Expression and adding it as a child to the AST
        node.data = "Expression";
        //call the SimpleExpression function, as per the EBNF
        node.addChild(parseSimpleExpression());
        //if there is a RelationalOp node, repeat the process again
        while(currentToken.getToken() == TKN.RelationalOp){
            node.addChild(new ASTNode("RelationalOp", new ASTNode(currentToken.getString(), null)));
            node.addChild(parseSimpleExpression());
        }
        return node;
    }

    //function used to parse SimpleExpressions and add them to the AST
    static ASTNode parseSimpleExpression() throws Exception {
        ASTNode n = new ASTNode();
        //calling the new node SimpleExpression
        n.data = "SimpleExpression";
        //adding the Term node to the AST by calling the parseTerm function
        n.addChild(parseTerm());

        //if there is an AdditiveOp present in the SimpleExpressio, repeat the process again
        while(currentToken.getToken() == TKN.AdditiveOp) {
            n.addChild(new ASTNode("AdditiveOp", new ASTNode(currentToken.getString(), null)));
            n.addChild(parseTerm());
        }

        return n;
    }

    //function used to parse the Term and add to the AST
    static ASTNode parseTerm() throws Exception {
        //name the new node Term
        ASTNode node = new ASTNode();
        node.data = "Term";

        currentToken = getNextToken();
        //call the factor function and add the result of that function as a child to the Term node
        node.addChild(factor());

        //if the current token is not an operator, get the next token.
        //this is so that any operators that may be pointed to at the moment are not ignored.
        if(currentToken.getToken() != TKN.MultiplicativeOp &&
                currentToken.getToken() != TKN.AdditiveOp &&
                currentToken.getToken() != TKN.RelationalOp &&
                currentToken.getToken() != TKN.Delimeter
        && currentToken.getToken() != TKN.CLOSE_BRACKET) {
            currentToken = getNextToken();
        }
        //if the current token is a MultiplicativeOp, repeat the process
        while(currentToken.getToken() == TKN.MultiplicativeOp) {
            ASTNode multiply = new ASTNode();
            multiply.data = "MultiplicativeOp";
            multiply.addChild(new ASTNode(currentToken.getString(), null));
            node.addChild(multiply);

            currentToken = getNextToken();
            node.addChild(factor());
            if(currentToken.getToken() != TKN.MultiplicativeOp &&
                    currentToken.getToken() != TKN.AdditiveOp &&
                    currentToken.getToken() != TKN.RelationalOp &&
                    currentToken.getToken() != TKN.Delimeter
                    && currentToken.getToken() != TKN.CLOSE_BRACKET) {
                currentToken = getNextToken();
            }
        }
        //return the Term node
        return node;
    }

    //function used to parse the FormalParameters of a function
    static ASTNode ParseFormalParam() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "FormalParam";
        //checking the syntax of every token. if a token does not match the syntax, throw an Exception with a detailed error message.
        if(currentToken.getToken() == TKN.Identifier){
            node.addChild(new ASTNode("Identifier", new ASTNode(currentToken.getString(), null)));
            currentToken = getNextToken();
            if(currentToken.getString().equals(":")){
                node.addChild(new ASTNode(":", null));
                currentToken = getNextToken();
                if(currentToken.getToken() == TKN.Type){
                    node.addChild(new ASTNode("Type", new ASTNode(currentToken.getString(), null)));
                    return node;
                } else{
                    throw new Exception("Incorrect Syntax! Type expected!");
                }
            } else throw new Exception("':' expected! Found "+currentToken.getString());
        } else{
            return null;
        }


    }

    //function to parse the formal parameters of a function
    static ASTNode ParseFormalParams() throws Exception{
        ASTNode node = new ASTNode();
        node.data = "FormalParams";
        node.addChild(ParseFormalParam());
        //check the syntax of the tokens and return the node once all the parameters have been parsed.
        if(currentToken.getToken() != TKN.CLOSE_BRACKET) {
            currentToken = getNextToken();
        }
        while(currentToken.getToken() == TKN.COMMA){
            currentToken = getNextToken();
            node.addChild(ParseFormalParam());
            currentToken = getNextToken();
        }
        return node;
    }

    //function used to parse variable declaration statements
    static ASTNode parseVarDecl() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "LET";
        currentToken = getNextToken();

        //check the syntax of the tokens. If the syntax is not as expected, throw an Exception with a detailed error message.
        //each time the syntax is satisfactory, add the token as a child to the LET node.
        ASTNode id = new ASTNode("Identifier", null);
        if(currentToken.getToken() == TKN.Identifier){
            id.addChild(new ASTNode(currentToken.getString(), null));
            node.addChild(id);
            currentToken = getNextToken();

            if(currentToken.getString().equals(":")){
                node.addChild(new ASTNode(":", null));
                currentToken = getNextToken();

                if(currentToken.getToken() == TKN.Type) {
                    ASTNode type = new ASTNode("Type", null);
                    type.addChild(new ASTNode(currentToken.getString(), null));
                    node.addChild(type);
                    currentToken = getNextToken();
                } else if(currentToken.getToken() == TKN.Auto) {
                    ASTNode auto = new ASTNode("Auto", null);
                    auto.addChild(new ASTNode(currentToken.getString(), null));
                    node.addChild(auto);
                    currentToken = getNextToken();
                } else
                    throw new Exception("Syntax Error! TYPE|AUTO expected after ':'.");

                    if(currentToken.getString().equals("=")){
                        node.addChild(new ASTNode("=", null));
                        node.addChild(parseExpression());
                        return node;
                    }
                }
            }

        throw new Exception("Syntax error! Unexpected symbol: " + currentToken.getString());
    }

    //function used to parse an if statement
    static ASTNode parseIfStatement() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "IF";
        currentToken = getNextToken();

        //checking the syntax of the if statement token by token.
        //if an unexpected token arises and does not follow the syntax, throw a detailed Exception.
        //else, every time the syntax is correct, add that token as a child to the IF node.
        if(currentToken.getToken() == TKN.OPN_BRACKET){
            node.addChild(new ASTNode("(", null));

            node.addChild(parseExpression());

            if(currentToken.getToken() == TKN.CLOSE_BRACKET){
                node.addChild(new ASTNode(")", null));
                node.addChild(parseBlock());
                currentToken = getNextToken();

                //optional else statement
                if(currentToken.getString().equals("else")){
                    node.addChild(new ASTNode("else", null));
                    node.addChild(parseBlock());
                }
                return node;
            } else {
                throw new Exception("Syntax Error! Expected ')', Found: "+currentToken.getToken());
            }
        } else {
            throw new Exception("Syntax Error! Expected '(', Found: "+currentToken.getToken());
        }

    }

    //function used to parse the For statement and add it to the AST
    static ASTNode parseForStatement() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "FOR";

        currentToken = getNextToken();

        //checking the syntax of the for statement token by token.
        //if an unexpected token arises and does not follow the syntax, throw a detailed Exception.
        //else, every time the syntax is correct, add that token as a child to the FOR node.
        if(currentToken.getToken() == TKN.OPN_BRACKET){
            node.addChild(new ASTNode("(", null));
            currentToken = getNextToken();

            while(currentToken.getString().equals("let")){
                node.addChild(parseVarDecl());

                if(currentToken.getString().equals(",")){
                    node.addChild(new ASTNode(",", null));
                    currentToken = getNextToken();
                }

            }
            if(currentToken.getToken() == TKN.Delimeter){
                node.addChild(new ASTNode(";", null));

                node.addChild(parseExpression());
                if(currentToken.getToken() == TKN.Delimeter){
                    node.addChild(new ASTNode(";", null));

                    currentToken = getNextToken();
                    if(currentToken.getToken() == TKN.Identifier) {

                        while(currentToken.getToken() == TKN.Identifier) {
                            node.addChild(parseAssignment());

                            if(currentToken.getString().equals(",")){
                                node.addChild(new ASTNode(",", null));
                                currentToken = getNextToken();
                            }

                        }
                        if (currentToken.getToken() == TKN.CLOSE_BRACKET) {
                            node.addChild(new ASTNode(")", null));
                            node.addChild(parseBlock());
                            return node;

                        } else {
                            throw new Exception("')' expected! Found " + currentToken.getString());
                        }
                    } else{
                        throw new Exception("Syntax error! Identifier expected!");
                    }
                }else{
                    throw new Exception("';' expected! Found "+currentToken.getString());
                }
            } else{
                throw new Exception("';' expected! Found "+currentToken.getString());
            }
        }
        return node;
    }


    //function used to parse a while statement and add it to the AST
    static ASTNode parseWhileStatement() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "WHILE";
        currentToken = getNextToken();

        //checking the syntax of the while statement token by token.
        //if an unexpected token arises and does not follow the syntax, throw a detailed Exception.
        //else, every time the syntax is correct, add that token as a child to the WHILE node.
        if(currentToken.getToken() == TKN.OPN_BRACKET){
            node.addChild(new ASTNode("(", null));
            node.addChild(parseExpression());
            if(currentToken.getToken() != TKN.CLOSE_BRACKET){
                currentToken = getNextToken();
            }

            if(currentToken.getToken() == TKN.CLOSE_BRACKET){
                node.addChild(new ASTNode(")", null));
                node.addChild(parseBlock());
                return node;

            } else{
                throw new Exception("Expected ')' after expression.");
            }
        } else{
            throw new Exception("Expected '(' after while declaration.");
        }
    }

    //function used to calculate which function is to be called, by using a switch-case on the current node.
    static ASTNode parseKeyword() throws Exception {
        ASTNode node = new ASTNode();

        switch (currentToken.getString()) {
            case "let":
                node = (parseVarDecl());
                break;
            case "print":
                node.data = "PRINT";
                node.addChild(parseExpression());
                break;
            case "return":
                node.data = "RETURN";
                node.addChild(parseExpression());
                break;
            case "if":
                node.data = "IF";
                node = parseIfStatement();
                break;
            case "for":
                node.data = "FOR";
                node = parseForStatement();
                break;
            case "while":
                node.data = "WHILE";
                node = (parseWhileStatement());
                break;
            case "ff":
                node.data = "FF";
                node = (parseFunctionDecl());
                break;
            default:
                throw new Exception("An error occured! Unexpected symbol: " + currentToken.getString());
        }

        return node;
    }

    //function used to parse the Assignment statement and add it to the AST
    static ASTNode parseAssignment() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "Assignment";

        //checking the syntax of the assignment statement token by token.
        //if an unexpected token arises and does not follow the syntax, throw a detailed Exception.
        //else, every time the syntax is correct, add that token as a child to the IF node.
        node.addChild(new ASTNode("Identifier", new ASTNode(currentToken.getString(), null)));
        currentToken = getNextToken();

        if(currentToken.getString().equals("=")){
            node.addChild(new ASTNode("=", null));
            node.addChild(parseExpression());
            return node;

        } else {
        throw new Exception("Incorrect syntax. '=' expected, found " + currentToken.getString());
    }

    }

    static ASTNode parseFunctionDecl() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "FF";
        currentToken = getNextToken();

        //checking the syntax of the function declaration statement token by token.
        //if an unexpected token arises and does not follow the syntax, throw a detailed Exception.
        //else, every time the syntax is correct, add that token as a child to the FF node.
        if(currentToken.getToken() == TKN.Identifier){
            node.addChild(new ASTNode("Identifier", new ASTNode(currentToken.getString(), null)));
            currentToken = getNextToken();

            if(currentToken.getToken() == TKN.OPN_BRACKET){
                node.addChild(new ASTNode("(", null));
                currentToken = getNextToken();
                node.addChild(ParseFormalParams());

                if(currentToken.getToken() == TKN.CLOSE_BRACKET){
                    node.addChild(new ASTNode(")", null));
                    currentToken = getNextToken();

                    if(currentToken.getString().equals(":")){
                        node.addChild(new ASTNode(":", null));
                        currentToken = getNextToken();

                        if(currentToken.getToken() == TKN.Type){
                            node.addChild(new ASTNode("Type", new ASTNode(currentToken.getString(), null)));
                            node.addChild(parseBlock());
                            return node;

                        }else if(currentToken.getToken() == TKN.Auto){
                            node.addChild(new ASTNode("Auto", new ASTNode(currentToken.getString(), null)));
                            node.addChild(parseBlock());
                            return node;

                        } else throw new Exception("Type expected after ':', Found "+currentToken.getString());

                    } else throw new Exception("':' symbol expected!");

                } else throw new Exception("')' symbol expected! Found " +currentToken.getString());

            } else throw new Exception("'(' symbol expected!");

        } else
            throw new Exception("Identifier expected!");
    }


    //function used to determine what statement should be parsed, and making sure
    //that each statement's syntax is correct, as per the statement EBNF.
    static ASTNode ParseStatement() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "STATEMENT";

        switch (currentToken.getToken()) {
            case RESERVED_KEYWORD:
                if(currentToken.getString().equals("let") ||
                   currentToken.getString().equals("print") ||
                   currentToken.getString().equals("return")) {

                    node.addChild(parseKeyword());

                    //check that each of these statements has a delimeter at the end
                    //if they dont, throw an Exception
                    if (currentToken.getToken() != TKN.Delimeter) {
                        throw new Exception("Syntax error! ';' expected! Found " +currentToken.getString());
                    } else {
                        return node;
                    }
                    //else, if the statement is an if statement, function declaration,
                    //while statement of for statement, a.k.a statements which dont require
                    //a ';' symbol at the end
                } else{
                    node.addChild(parseKeyword());
                    return node;
                }
                //else the statement is an assignment
            case Identifier:
               node.addChild(parseAssignment());
                if (currentToken.getToken() != TKN.Delimeter) {
                    throw new Exception("Syntax error! ';' expected! Found " +currentToken.getString());
                }
               return node;
        }
        return node;
    }

    //function to parse a Block and add it to the AST
    static ASTNode parseBlock() throws Exception {
        ASTNode node = new ASTNode();
        node.data = "Block";

        currentToken = getNextToken();
        //checking the syntax of the block token by token.
        //if an unexpected token arises and does not follow the syntax, throw a detailed Exception.
        //else, every time the syntax is correct, add that token as a child to the Block node.
        if(currentToken.getToken() == TKN.OPN_CURLY) {
            node.addChild(new ASTNode("{", null));
            currentToken = getNextToken();

            //while there are still statements coming from the lexer, keep executing the parseStatement function
            //and adding those statements as children.
            while(currentToken.getToken() == TKN.Identifier || currentToken.getToken() == TKN.RESERVED_KEYWORD) {
                node.addChild(ParseStatement());
                if(currentToken.getToken() != TKN.Identifier && currentToken.getToken() != TKN.RESERVED_KEYWORD) {
                    currentToken = getNextToken();
                }
            }

            if (currentToken.getToken() == TKN.CLOSE_CURLY) {
                node.addChild(new ASTNode("}", null));
            } else
                throw new Exception("Syntax Error! Expected '}', found " +currentToken.getString() +currentToken.getToken());

        }else{
            throw new Exception("'{' expected, found "+currentToken.getString());
        }
        return node;
    }

    //function used to deteremine whether a factor is valid
    static ASTNode factor() throws Exception {
        ASTNode n = new ASTNode();
        n.data = "Factor";

        switch(currentToken.getToken()){

            //if the factor is an Identifier, it could also be a functionCall.
            //this is checked in this case.
            case Identifier:
                String name = currentToken.getString();
                currentToken = getNextToken();
                if(currentToken.getToken() == TKN.OPN_BRACKET){
                    n.addChild(parseFunctionCall(name));
                } else {
                    n.addChild(new ASTNode("Identifier", new ASTNode(name, null)));
                }
                break;
                //if the factor is a BooleanLiteral, add this as a child to the Factor node.
            case BooleanLiteral:
                n.addChild(new ASTNode("BooleanLiteral", new ASTNode(currentToken.getString(), null)));
            break;
            //if the factor is a FloatLiteral, add this as a child to the Factor node.
            case FloatLiteral:
                n.addChild(new ASTNode("FloatLiteral", new ASTNode(currentToken.getString(), null)));
            break;
            //if the factor is a IntegerLiteral, add this as a child to the Factor node.
            case IntegerLiteral:
                n.addChild(new ASTNode("IntegerLiteral", new ASTNode(currentToken.getString(), null)));
            break;

            case OPN_BRACKET:
                n.addChild(parseSubExpression());
                break;
            //if the factor is a Unary, add this as a child to the Factor node.
            case NOT:
                //-|NOT EXPRESSION
                n.addChild(new ASTNode("Unary", parseExpression()));
                //parseExpression();
                break;
            //if the factor is a Digit, add this as a child to the Factor node.
            case Digit:
                n.addChild(new ASTNode("Digit", new ASTNode(currentToken.getString(), null)));
            break;
            //if the factor is a Letter, add this as a child to the Factor node.
            case Letter:
                n.addChild(new ASTNode("Letter", new ASTNode(currentToken.getString(), null)));
            break;

            default:
                throw new Exception("Incorrect syntax! Unexpected symbol: " + currentToken.getString());
        }
        return n;
    }

    //function used to Parse the program, as per the EBNF
    static ASTNode ParseProgram() throws Exception {
        ASTNode node = new ASTNode();
        currentToken = getNextToken();
        //continuously call the parseStatement function for as long as there are statements being fed by the lexer
        while(currentToken.getToken() != TKN.Delimeter && currentToken.getToken() != TKN.CLOSE_CURLY & currentToken.getToken() != TKN.EPSILON) {
            node.addChild(ParseStatement());

            if(currentToken.getToken() == TKN.Delimeter || currentToken.getToken() == TKN.CLOSE_CURLY) {
                currentToken = getNextToken();
            }

        }
        //if no Exceptions are thrown, the syntax is valid.
        System.out.println("Valid parser syntax!");
        return node;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Input your file name: ");
        Scanner scn = new Scanner(System.in);
        String str = scn.nextLine();

        String content = new String(Files.readAllBytes(Paths.get(str)));
        content = content.replace("\n", "").replace("\r", "");

        l = new Lexer(content);

        ASTNode rootnode = new ASTNode();
        rootnode.data = "PROGRAM";
        rootnode = ParseProgram();

        XML.GenerateXML(rootnode);
        SemanticAnalyser.Semantic(rootnode);
        Interpreter.Interpret(rootnode);
    }

}
