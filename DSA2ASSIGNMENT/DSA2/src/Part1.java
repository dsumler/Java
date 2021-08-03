import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Character.toLowerCase;

public class Part1 {
    //Rows of states
    static Part1States[] Rows = {Part1States.S0, Part1States.S1, Part1States.S2, Part1States.S3, Part1States.S4, Part1States.S5, Part1States.SE};
    //Columns of tokens
    static Part1Tokens[] Columns = {Part1Tokens.OPEN_BRACKETS, Part1Tokens.CLOSE_BRACKETS, Part1Tokens.LITERAL, Part1Tokens.COMMA, Part1Tokens.EXCLMARK, Part1Tokens.INVALID};
    //Transition table
    static Part1States[][] symbolTable = //OPEN_BRACKETS     CLOSE_BRACKETS   LITERAL        COMMA          EXCLMARK         INVALID
                                  /*S0*/ { {Part1States.S1, Part1States.SE, Part1States.SE, Part1States.SE, Part1States.SE, Part1States.SE},
                                  /*S1*/   {Part1States.SE, Part1States.S2, Part1States.S3, Part1States.SE, Part1States.S5, Part1States.SE},
                                  /*S2*/   {Part1States.S1, Part1States.SE, Part1States.SE, Part1States.SE, Part1States.SE, Part1States.SE},
                                  /*S3*/   {Part1States.SE, Part1States.S2, Part1States.SE, Part1States.S4, Part1States.SE, Part1States.SE},
                                  /*S4*/   {Part1States.SE, Part1States.SE, Part1States.S3, Part1States.SE, Part1States.S5, Part1States.SE},
                                  /*S5*/   {Part1States.SE, Part1States.SE, Part1States.S3, Part1States.SE, Part1States.SE, Part1States.SE},
                                  /*SE*/   {Part1States.SE, Part1States.SE, Part1States.SE, Part1States.SE, Part1States.SE, Part1States.SE}
    };

    static Part1States symbolTables(Part1States state, Part1Tokens token){
        int rowNumber, columnNumber;
        //Finding the correct row index for input into the symbol table
        for(rowNumber = 0; rowNumber < Rows.length; rowNumber++){
            if(Rows[rowNumber] == state){
                break;
            }
        }
        //Finding the correct column index for input into the symbol table
        for(columnNumber = 0; columnNumber < Columns.length; columnNumber++){
            if(Columns[columnNumber] == token){
                break;
            }
        }
        return symbolTable[rowNumber][columnNumber];

    }
    //only the close_brackets state is considered a final state in this case
    static boolean isFinalState(Part1States state){
        return state == Part1States.S2;
    }


    public static void main(String[] args) {

        Scanner scn = new Scanner(System.in);
        String booleanExp;
        Part1Tokens token;
        Part1States state = Part1States.S0;

        System.out.println("Input your boolean expression: ");
        booleanExp = scn.nextLine();
        //remove whitespaces
        booleanExp = booleanExp.replaceAll("\\s+", "");
        //determine the current token
        for (int i = 0; i < booleanExp.length(); i++) {
            char character = booleanExp.charAt(i);
            if (character != ' ') {
                if (character == '(')
                    token = Part1Tokens.OPEN_BRACKETS;
                else if (character == ')')
                    token = Part1Tokens.CLOSE_BRACKETS;
                else if (toLowerCase(character) == 'w' || toLowerCase(character) == 'x' || toLowerCase(character) == 'y' || toLowerCase(character) == 'z')
                    token = Part1Tokens.LITERAL;
                else if (character == '!')
                    token = Part1Tokens.EXCLMARK;
                else if (character == ',')
                    token = Part1Tokens.COMMA;
                else
                    token = Part1Tokens.INVALID;
                //determine the current state
                state = symbolTables(state, token);
            }
        }
        //accept the expression if it is in a final state
        if (isFinalState(state)) {
            System.out.println("Expression accepted!");
            //create a new ArrayList to store the clauses
            ArrayList<String> Clauses = new ArrayList<>(Arrays.asList(booleanExp.split("\\)\\(")));
            //remove the brackets from the first and last clause to store in the ArrayList
            Clauses.set(0, parseClauses(Clauses.get(0)));
            Clauses.set(Clauses.size() - 1, parseClauses(Clauses.get(Clauses.size() - 1)));

            //if the DPLL algorithm returns true, the statement is SAT, else UNSAT
            if (DPLL(Clauses)) {
                System.out.println("Statement is SAT!");
            } else {
                System.out.println("Statement is UNSAT!");
            }

        } else {
            //if the expression is incorrect, exit the program softly
            System.out.println("Incorrect Expression!");
        }
    }
    //function to parse the strings in order to remove brackets
    private static String parseClauses(String str){
        char ch[] = str.toCharArray();
        char ch2[] = new char[str.length() - 1];
        if(ch[0] == '(') {
            for (int i = 0; i < ch.length - 1; i++) {
                ch2[i] = ch[i + 1];
            }
        }else if(ch[str.length()-1] == ')'){
            for (int i = 0; i < ch.length - 1; i++) {
                ch2[i] = ch[i];
            }
        }
        return new String(ch2);
    }
    //checking all of the clauses to find and remove any negative appearances of the given literal, letter
    static String CheckForNegation(String Clause, char letter, boolean negative){
        //if the given literal is positive, remove every negative occurrence of it
        if(!negative) {
            if (Clause.matches(".*,!" + letter + ".*")) {
                Clause = Clause.replace(",!" + letter, "");
            } else if (Clause.matches(".*!" + letter + ",.*")) {
                Clause = Clause.replace("!" + letter + ",", "");
            } else if (Clause.matches(".*!" + letter + ".*")) {
                Clause = Clause.replace("!" + letter + "", "");
            }
        //if the given literal is negative, remove every positive occurrence of it
        }else{
            if (Clause.matches(".*," + letter + ".*")) {
                Clause = Clause.replace("," + letter, "");
            } else if (Clause.matches(letter + ",.*")) {
                Clause = Clause.replace(letter + ",", "");
            } else if (Clause.matches( letter+"")) {
                Clause = Clause.replace(letter + "", "");
            }
        }
        return Clause;
    }
    //function to apply the 1 literal rule
    static ArrayList<String> Apply1LiteralRule(int i, ArrayList<String> Clauses, boolean negative){
        char letter;
        //if the given clause is negative, increment in order to select the literal instead of the !
        if(negative){
            letter = Clauses.get(i).charAt(1);
        } else{
            //if the given clause is positive, store the literal as 'letter'
            letter = Clauses.get(i).charAt(0);
        }

        //call the checkForNegation function to remove any and all occurrences of negative versions of the given literal
        for(int i2 = 0; i2 < Clauses.size(); i2++){

            Clauses.set(i2, CheckForNegation(Clauses.get(i2), letter, negative));

        }
        //delete all clauses which feature the given literal
        for(int i2 = 0; i2 < Clauses.size(); i2++){
            if(negative && Clauses.get(i2).matches(".*!"+letter+".*")){
                Clauses.remove(i2);
                i2 = -1;
            } else if(!negative && Clauses.get(i2).matches(".*"+letter+".*")){
                Clauses.remove(i2);
                i2 = -1;
            }
        }

        printClauses(Clauses);
        return Clauses;
    }
    //return true if the list of clauses is empty
    static boolean CheckEmptylist(ArrayList<String> Clauses){
        return Clauses.size() == 0;
    }
    //return true if the list does not contain an empty clause
    static boolean CheckValidity(ArrayList<String> Clauses){
        return !Clauses.contains("");
    }
    //function to apply the pure literal rule
    static ArrayList<String> ApplyPureLiteralRule(int i, ArrayList<String> Clauses){
        String letter;
        boolean contains = false;
        //each literal will be checked using this method
        if(i == 0){
            letter = "w";
        } else if(i == 1){
            letter = "x";
        } else if(i == 2){
            letter = "y";
        } else{
            letter = "z";
        }
        //if a negative occurence of the literal is found, abort the method
        for(int i2 = 0; i2 < Clauses.size(); i2++){
            if(Clauses.get(i2).matches(".*!"+letter+".*")){
                return Clauses;
            } else if(Clauses.get(i2).matches(".*"+letter+".*")){
                contains = true;
            }
        }
        //else if the clause only contains positive versions of the literal, delete all clauses containing the literal
        if(contains) {
            System.out.println("Applying the pure literal rule on " + letter);
            for (int i2 = 0; i2 < Clauses.size(); i2++) {
                if (Clauses.get(i2).matches(".*" + letter + ".*")) {
                    Clauses.remove(i2);
                    i2 = -1;
                    System.out.println("Removed a clause containing " + letter);
                    printClauses(Clauses);
                }
            }
        }
        return Clauses;
    }

    //function to print the clauses in a user-friendly manner
    static void printClauses(ArrayList<String> Clauses){
        for(int i = 0; i < Clauses.size(); i++) {
            System.out.print("(" + Clauses.get(i) + ")");
        }
        System.out.println();
    }
    //function to choose the literal to use in the splitting method, based on the amount of times it appears in the clauses
    static char ChoosingLiteral(ArrayList<String> Clauses){

        HashMap<Character, Integer> charCounter = new HashMap<>();

        //using the hashmap to count the amount of occurrences per literal
        for (String str : Clauses) {
            for (int i2 = 0; i2 < str.length(); i2++) {
                while(str.charAt(i2) == '!' || str.charAt(i2) == ',' || str.charAt(i2) == ' '){
                    i2++;
                }
                if (i2 < str.length() && charCounter.containsKey(str.charAt(i2))) {
                    charCounter.put(str.charAt(i2), charCounter.get(str.charAt(i2)) + 1);
                } else if(i2<str.length()){
                    charCounter.put(str.charAt(i2), 1);
                }

            }
        }
        //each literal's occurrences will be compared with eachother, and the one with the highest number will be selected
        char c = 'x';
        int highest = 0;
        for (HashMap.Entry<Character, Integer> entry : charCounter.entrySet()) {
            if(entry.getValue() > highest){
                highest = entry.getValue();
                c = entry.getKey();
            }
        }
    return c;
    }
    //the DPLL function
    static boolean DPLL(ArrayList<String> Clauses) {
        //return unsat if there are any trivially unsatisfiable clauses (e.g. (p)(!p))
        if((Clauses.contains("w") && Clauses.contains("!w"))
        || (Clauses.contains("x") && Clauses.contains("!x"))
        || (Clauses.contains("y") && Clauses.contains("!y"))
        || Clauses.contains("z") && Clauses.contains("!z")){
            return false;
        }
        //remove any trivially satisfiable clauses
        for (int i = 0; i < Clauses.size(); i++) {
            if((Clauses.get(i).matches("x,.*|.*,x.*") && Clauses.get(i).matches(".*!x.*")) ||
                    (Clauses.get(i).matches("w,.*|.*,w.*") && Clauses.get(i).matches(".*!w.*")) ||
                    (Clauses.get(i).matches("y,.*|.*,y.*") && Clauses.get(i).matches(".*!y.*")) ||
                    (Clauses.get(i).matches("z,.*|.*,z.*") && Clauses.get(i).matches(".*!z.*"))){
                System.out.println("Removed trivially satisfiable clause.");
                Clauses.remove(i);
                printClauses(Clauses);
                i = -1;
            }
        }
        //IS STATEMENT EMPTY?
        for (int i = 0; i < Clauses.size(); i++) {
            if (Clauses.get(i).length() == 0) {
                System.out.println("Empty Clause Found!");
                return false;
            }
        }

        if (Clauses.size() == 0) {
            return true;
        }
        //applying 1 Literal Rule
        for (int i = 0; i < Clauses.size(); i++) {
            if (Clauses.get(i).length() <= 2 && Clauses.get(i).matches(".*[xwyz].*")) {
                System.out.println("Applied 1 Literal Rule on " + Clauses.get(i));
                if(Clauses.get(i).charAt(0) == '!') {
                    Clauses = Apply1LiteralRule(i, Clauses, true);
                } else{
                    Clauses = Apply1LiteralRule(i, Clauses, false);
                }
                //reset the counter in case there is a new single literal clause
                i = -1;
            }
        }
        //Check if the list is empty
        if (CheckEmptylist(Clauses)) {
            System.out.println("Proven true by 1 Literal Rule");
            return true;
        }
        //check if there is an empty clause
        if(!CheckValidity(Clauses)){
            System.out.println("Empty clause found!");
            return false;
        }

        //Apply Pure Literal Rule
        for(int i = 0; i < 4; i++){
            Clauses = ApplyPureLiteralRule(i, Clauses);
            //check if there are any empty clauses/list in order to save on processing time
            if (CheckEmptylist(Clauses)) {
                System.out.println("Proven true by Pure Literal Rule");
                return true;
            } if(!CheckValidity(Clauses)){
                System.out.print("Empty clause found!");
                return false;
            }
        }

        //choosing the literal to be used in the splitting rule
        char c = ChoosingLiteral(Clauses);

        ArrayList<String> Clauses1 = new ArrayList<>();
        Clauses1.addAll(Clauses);
        Clauses1.add(""+c);
        ArrayList<String> Clauses2 = new ArrayList<>();
        Clauses2.addAll(Clauses);
        Clauses2.add("!"+c);
        //re-doing the DPLL algorithm, as per the splitting rule
        //PLEASE NOTE: This line may cause an infinite loop due to the way
        //the splitting literal is chosen
        //if an infinite loop occurs, please test one of the following lines instead:
        //return DPLL(Clauses1)
        //return DPLL(Clauses2)
        System.out.println("Putting back in new clause : " + Clauses1);
        if(DPLL(Clauses1)){
            return true;
        }
        System.out.println("Putting back in new clause : " + Clauses2);
        if(DPLL(Clauses2)){
            return true;
        }
        return false;
        //return (DPLL(Clauses1) || DPLL(Clauses2));

    }


}
