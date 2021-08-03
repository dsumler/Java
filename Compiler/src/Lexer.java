import java.util.Scanner;

enum LexerStates{
    S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, SKEYWORD, SPunc, SNOT, SE, SInv;
}

public class Lexer {
    //the char pointer, used to keep track of what char is to be examined next
    int charLocation = 0;
    //String to store the input program.
    private String inputString;
    //custom token class which contains a string and a token
    static TOKEN tok = new TOKEN();
    //custom LexerStates class to keep track of the current state of the program
    private static LexerStates State;
    //lexeme to store the processed characters, ready to be returned
    private static String lexeme;
    //An array of States, used to make up the Rows of the transition table
    private static LexerStates[] Rows;
    //An array of Tokens, used to make up the Columns of the transition table
    private static TKN[]  Columns;
    //An array of States, used to make up the Transitions of the transition table
    private static LexerStates[][] DeltaTable;

    public Lexer(String input){
        this.inputString = input;
    }


    static void PrepareTables(){
        //function to declare the transition table
        Rows = new LexerStates[] {LexerStates.S0, LexerStates.S1, LexerStates.S2, LexerStates.S3, LexerStates.S4, LexerStates.S5, LexerStates.S6, LexerStates.S7, LexerStates.S8, LexerStates.S9, LexerStates.S10, LexerStates.S11,LexerStates.SPunc, LexerStates.SNOT, LexerStates.SKEYWORD, LexerStates.SE, LexerStates.SInv};
        Columns = new TKN[] {TKN.Letter, TKN.Digit, TKN.Type, TKN.Identifier, TKN.Auto, TKN.BooleanLiteral, TKN.IntegerLiteral, TKN.FloatLiteral, TKN.MultiplicativeOp, TKN.AdditiveOp, TKN.RelationalOp, TKN.Punctuation, TKN.NOT, TKN.RESERVED_KEYWORD, TKN.Other, TKN.Invalid};
        DeltaTable = new LexerStates[][] {
                //                          Letter          Digit           Type            Identifier      Auto            BooleanLiteral  IntegerLiteral  FloatLiteral    MultiplicativeOp    AdditiveOp  RelationalOp          Punctuation          NOT          KEYWORD         SE              Invalid
                /*S0 - Start*/             {LexerStates.S1, LexerStates.S2, LexerStates.S3, LexerStates.S4, LexerStates.S5, LexerStates.S6, LexerStates.S7, LexerStates.S8, LexerStates.S9, LexerStates.S10, LexerStates.S11, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S1 - Letter*/            {LexerStates.S4, LexerStates.S4, LexerStates.S3, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S2 - Digit*/             {LexerStates.S4, LexerStates.S7, LexerStates.S7, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.S8, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S3 - Type*/              {LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S4 - Identifier*/        {LexerStates.S4, LexerStates.S4, LexerStates.S3, LexerStates.S4, LexerStates.S5, LexerStates.S6, LexerStates.S7, LexerStates.S8, LexerStates.S9, LexerStates.S10, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S5 - Auto*/              {LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.S5, LexerStates.S6, LexerStates.S7, LexerStates.S8, LexerStates.S9, LexerStates.S10, LexerStates.S11, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S6 - BooleanLiteral*/    {LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.S5, LexerStates.S6, LexerStates.S7, LexerStates.S8, LexerStates.S9, LexerStates.S10, LexerStates.S11, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S7 - IntegerLiteral*/    {LexerStates.S4, LexerStates.S7, LexerStates.SE, LexerStates.SE, LexerStates.S5, LexerStates.S7, LexerStates.S7, LexerStates.S8, LexerStates.S9, LexerStates.S10, LexerStates.S11, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S8 - FloatLiteral*/      {LexerStates.SE, LexerStates.S8, LexerStates.SE, LexerStates.SE, LexerStates.S5, LexerStates.S7, LexerStates.S7, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S9 - MultiplicativeOp*/  {LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.S5, LexerStates.S7, LexerStates.S7, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S10 - AdditiveOp*/       {LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.S5, LexerStates.S7, LexerStates.S7, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*S11 - RelationalOp*/     {LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.S11, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*SPunc - Punctuation*/    {LexerStates.S1, LexerStates.S2, LexerStates.S3, LexerStates.S4, LexerStates.S5, LexerStates.S6, LexerStates.S7, LexerStates.S8, LexerStates.S9, LexerStates.S10, LexerStates.S11, LexerStates.SPunc, LexerStates.SNOT, LexerStates.SKEYWORD, LexerStates.SE, LexerStates.SInv},
                /*SNOT - NOT*/             {LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv},
                /*SKEYWORD - KEYWORD*/     {LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.S4, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SPunc, LexerStates.SE, LexerStates.S4, LexerStates.SE, LexerStates.SInv},
                /*SE*/                     {LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SE, LexerStates.SInv}
        };
    }

    static boolean returnChar(char ch){
        //if true is returned, it means that a special character was encountered that must be returned immediately
        if(ch == '(' || ch == ')' || ch == ',' || ch == '{' || ch == '}' || ch == '+' ||
        ch == '-' || ch == '*' || ch == '/' || ch == ' ' || ch == ':' || ch == ';'){
            return true;
        } else if((State == LexerStates.S11 || State == LexerStates.S0) && (ch == '<' || ch == '>' || ch == '=')){
            return false;
        } else if((State == LexerStates.S11) && (ch != '<' && ch != '>' && ch != '=')){
            return true;
        } else if(ch == '=' || ch == '>' || ch == '<'){
            return true;
        } else{
            return false;
        }
    }

    //provide translation from the input into a token
    static TKN CharCat(char c){
        if(Character.isDigit(c)){
            return TKN.Digit;
        } else if(Character.isLetter(c)){
            return TKN.Letter;
        } else if(c == '_'){
            return TKN.Identifier;
        } else if(c == '.'){
            return TKN.FloatLiteral;
        } else if(c == '*' || c == '/'){
            return TKN.MultiplicativeOp;
        } else if(c == '+' || c == '-'){
            return TKN.AdditiveOp;
        } else if(c == '<' || c == '>' || c == '='){
            return TKN.RelationalOp;
        } else if(c == '('){
            return TKN.OPN_BRACKET;
        } else if(c == ')'){
            return TKN.CLOSE_BRACKET;
        } else if(c == ','){
            return TKN.COMMA;
        } else if(c == '{'){
            return TKN.OPN_CURLY;
        } else if(c == '}'){
            return TKN.CLOSE_CURLY;
        } else if(c == ';'){
            return TKN.Delimeter;
        }
        return TKN.Other;
    }

    //look through the delta table for the required transition
    static LexerStates TX(TKN token, LexerStates currentState){
        int row, column;
        for(row = 0; row < Rows.length; row ++){
            if(currentState == Rows[row]){
                break;
            }
        }
        for(column = 0; column < Columns.length; column ++){
            if(token == Columns[column]){
                break;
            }
        }
        return DeltaTable[row][column];

    }

    static TKN SymbolTable(String lexeme, LexerStates State){
        //function to determine the current token given the current processed string and
        //the current state
        if(State == LexerStates.S4) {
            switch (lexeme) {
                case "or":
                    return TKN.AdditiveOp;
                case "and":
                    return TKN.MultiplicativeOp;
                case "true":
                case "false":
                    return TKN.BooleanLiteral;
                case "auto":
                    return TKN.Auto;
                case "float":
                case "bool":
                case "int":
                    return TKN.Type;
                case "let":
                case "print":
                case "return":
                case "if":
                case "for":
                case "while":
                case "ff":
                    return TKN.RESERVED_KEYWORD;
            }
            return TKN.Identifier;
        } else if(State == LexerStates.S11){
            if(lexeme.equals("<") || lexeme.equals(">") || lexeme.equals("==") || lexeme.equals("<>") || lexeme.equals("<=") || lexeme.equals(">=") || lexeme.equals("=")){
                return TKN.RelationalOp;
            } else{
                return TKN.Invalid;
            }
        }
        return TKN.Other;

    }

    //function to translate the current state to a token, and return said token
    static TKN StateToToken(LexerStates state) {
        if (state == LexerStates.SInv) {
            return TKN.Invalid;
        } else if (state == LexerStates.S1) {
            return TKN.Letter;
        } else if (state == LexerStates.S2) {
            return TKN.Digit;
        } else if (state == LexerStates.S3) {
            return TKN.Type;
        } else if (state == LexerStates.S4) {
            return TKN.Identifier;
        } else if (state == LexerStates.S5) {
            return TKN.Auto;
        } else if (state == LexerStates.S6) {
            return TKN.BooleanLiteral;
        } else if (state == LexerStates.S7) {
            return TKN.IntegerLiteral;
        } else if (state == LexerStates.S8) {
            return TKN.FloatLiteral;
        } else if (state == LexerStates.S9) {
            return TKN.MultiplicativeOp;
        } else if (state == LexerStates.S10) {
            return TKN.AdditiveOp;
        } else if (state == LexerStates.S11) {
            return TKN.RelationalOp;
        } else if(state == LexerStates.SPunc){
            return TKN.Punctuation;
        }else{
            return TKN.Invalid;
        }

    }

    public TOKEN NextWord() {
        if (inputString.length() == 0) {
            tok.setToken(TKN.EPSILON);
            tok.setString("");
            return tok;
        }

        PrepareTables();

        State = LexerStates.S0;
        lexeme = "";

        //scanning loop - if the end of the input string is reached, return epsilon
        if (this.charLocation >= inputString.length()) {
            tok.setToken(TKN.EPSILON);
            tok.setString("");
            return tok;
        }
        //loop for processing the inputted characters
        while (this.charLocation < inputString.length()) {
            //take each character out seperately and return the current token if
            //the character is a whitespace
            char Char = inputString.charAt(charLocation);
            while (Char == ' ' && charLocation < inputString.length()) {
                charLocation++;
                if (inputString.length() <= charLocation) {
                    tok.setToken(TKN.EPSILON);
                    tok.setString("");
                    return tok;
                }
                //if the token is empty, return the token
                Char = inputString.charAt(charLocation);
                if (tok.isEmpty()) {
                    return tok;
                }
            }

            //added the new character to the processed string
            lexeme = lexeme + Char;
            //place the processed string in the token, ready to be returned
            tok.setString(lexeme);

            //check whether the current character represents a special token,
            //eg ;, _, etc.. and add that as the current token
            tok.setToken(CharCat(Char));

            //if the current character is its own token, meaning for example, a
            // ; or , symbol, it should be returned immediately as its own
            //separate token.
            if (returnChar(this.inputString.charAt(charLocation))) {
                charLocation++;
                return tok;
            }

            //set the current state to a new one based off the previous state
            //and the current token using the transition table
            State = TX(tok.getToken(), State);
            tok.setToken(StateToToken(State));

            //if the current state is that of an identifier or a relationalOp,
            //it could be part of a keyword. Check these and change the current token
            //and state if necessary.
            if (State == LexerStates.S4 || State == LexerStates.S11) {
                tok.setToken(SymbolTable(lexeme, State));
                State = TX(tok.getToken(), State);
            }
            //increment the char pointer
            int c = charLocation + 1;

            //if the next char in the string is a special token that returns immediately, return
            //the current token and increment the pointer.
            if (charLocation < inputString.length() - 1 && returnChar(this.inputString.charAt(c))) {
                charLocation++;
                return tok;
            }
            //increment the character pointer
            this.charLocation++;
        }

        return tok;

    }

    public static void main(String[] args){
        System.out.println("Syntax Checker:");
        Scanner scn = new Scanner(System.in);
        String str = scn.nextLine();
        Lexer l = new Lexer(str);
        TOKEN token;
        token = l.NextWord();
        while(token.getToken() != TKN.EPSILON){
            System.out.println("Token = " +token.getToken()+ " | String = "+token.getString());
            token = l.NextWord();
        }

    }
}
