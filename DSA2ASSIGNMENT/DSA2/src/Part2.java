import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Part2 {
    private static ArrayList<String> strs = new ArrayList<>();
    //Comparator used to compare node frequencies and order them into descending order
    private static Comparator<Node> NodeFrequency = (n1, n2) -> {
        return n1.getFrequency() - n2.getFrequency();
    };
    //Comparator used to order integers and return them in descending order
    static Comparator<Integer> Order = (n1, n2) -> {
        return n1 - n2;
    };


    //function to read text file using filename entered by the user
    private static void ReadFile(String filename) throws FileNotFoundException {
        File file = new File("./"+filename);
        //if the file is not found, exit the program softly and display a suitable error message
        if(!file.exists()){
            System.out.println("The file was not found. Please check the file exists and try again.");
            System.exit(1);
        }
        //read the file line by line, and store each line in a different index in the ArrayList
        Scanner scn = new Scanner(file);
        String str;
        while (scn.hasNextLine()) {
            str = scn.nextLine();
            //if whitespaces need to be used, please uncomment line 32.
            //str = str.replaceAll("\\s+", "");
            //check whether each character is either a letter or a number
            if(str.matches("[a-zA-Z0-9]*")) {
                strs.add(str);
            } else{
                //if a different character is found, exit the program softly and display an error message
                System.out.println("Illegal Character found! Characters can only be numbers and letters!");
                System.exit(1);
            }
        }
        //else, display a positive message to the user
        System.out.println("File "+filename+" was accepted!");
    }

        //function used to generate the actual Huffman Code by traversing the Huffman Tree recursively
    private static void GenerateCode(Node n, String code, HashMap<Character, String> HuffmanTree) {

        if (n != null) {
            //if node has no children (a leaf node), put the current code and character into the HashMap
            if (n.getLeftChild() == null && n.getRightChild() == null) {
                HuffmanTree.put(n.getChar(), code);
            } else {
                //else, recursively call the GenerateCode function using both children of the current node
                GenerateCode(n.getLeftChild(), code + "0", HuffmanTree);
                GenerateCode(n.getRightChild(), code + "1", HuffmanTree);
            }
        }
    }
    //function to organise the list of Huffman Codes based on their ASCII values
    private static void OrganiseList(HashMap<Character, String> HuffmanTree){
        //extract all the keys and values from the HuffmanTree hashmap
        Object[] keys = HuffmanTree.keySet().toArray();
        Object[] values = HuffmanTree.values().toArray();

        //create a new array and store the decimal values of each Huffman code
        int[] value = new int[values.length];
        for(int i = 0; i < values.length; i++){
            value[i] = Integer.parseInt((String)values[i], 2);
        }
        //sort the values in ascending order
        Arrays.sort(value);

        //declare a linked hashmap in order to record the order in which elements are pushed into the hashmap
        LinkedHashMap<String, Integer> sort = new LinkedHashMap<>();

        //fill the LinkedHashMap with the ordered Huffman codes and its corresponding ASCII value
        for(int i2 =0; i2<value.length; i2++) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null && Integer.parseInt((String) values[i], 2) == value[i2]) {
                    sort.put((String) values[i], value[i2]);
                    values[i] = null;
                    break;
                }
            }
        }

        //extract the SORTED keyset from the LinkedHashMap
        Object[] keyset = sort.keySet().toArray();
        //match and display each character and its correspondng Huffman code in ascending order to the user
        for(int i = 0; i < keyset.length; i++){
            for(int i2 = 0; i2 < keys.length; i2++){
                if(keyset[i].equals(HuffmanTree.get(keys[i2]))){
                    System.out.println(keys[i2] +" = "+ keyset[i]);
                    break;
                }
            }
        }
    }

    //the function used in order to create the Huffman Tree
    private static void Huffman() {
        String str;
        //declaring a new PriorityQueue to store Nodes and a HashMa to store each character and their frequency of appearances
        PriorityQueue<Node> NodeQueue = new PriorityQueue<>(NodeFrequency);
        HashMap<Character, Integer> charFrequency = new HashMap<>();

        //calculate the frequency for each character and place them in the hashmap
        for(int i2 = 0; i2 < strs.size(); i2 ++) {
            str = strs.get(i2);
            for (int i = 0; i < str.length(); i++) {
                if (charFrequency.containsKey(str.charAt(i))) {
                    charFrequency.put(str.charAt(i), charFrequency.get(str.charAt(i)) + 1);
                } else {
                    charFrequency.put(str.charAt(i), 0);
                }
            }
        }

        //Use the characters and values in the hashmap in order to create a node for every character,
        //with their character and frequency as data
        //these are then added to the PriorityQueue
        List<Character> chars = new ArrayList<>(charFrequency.keySet());
        for(int i = 0; i < chars.size(); i++){
            Node n = new Node(chars.get(i), charFrequency.get(chars.get(i)));
            NodeQueue.add(n);
        }
        //poll the PriorityQueue in order to combine 2 nodes into a frequency node (as per the Huffman Coding method)
        while (NodeQueue.size() != 1) {
            Node l = NodeQueue.poll();
            Node r = NodeQueue.poll();

            Node n;
            //creating a new node containing the frequencies of its children
            if(r != null) {
                n = new Node(l.getFrequency() + r.getFrequency(), l, r);
            } else{
                n = new Node(l.getFrequency(), l, null);
            }
            //adding the new node to the PriorityQueue
            NodeQueue.add(n);
        }
        //creating a new HashMap in order to calculate the Huffman Code for each character in the tree
        HashMap<Character, String> HuffmanTree = new HashMap<>();
        //declaring the root node
        Node root = NodeQueue.peek();
        //calling the GenerateCode function to generate the Huffman Code
        GenerateCode(root, "", HuffmanTree);
        //organise the HashMap based on ASCII values and display to the user
        OrganiseList(HuffmanTree);

    }

    public static void main(String args[]) throws IOException {
        //ask the user for the filename
        System.out.println("Enter filename :");
        Scanner scn = new Scanner(System.in);
        String s = scn.nextLine();
        //read the file and run the Huffman function
        ReadFile(s);


        Huffman();
    }
}
