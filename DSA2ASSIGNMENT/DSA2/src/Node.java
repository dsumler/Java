//Node class used to make the huffman tree in Part2.java
class Node {
    private char data = '$';
    private int frequency;
    private Node leftchild = null;
    private Node rightchild = null;

    Node(char c, int i){
        this.data = c;
        this.frequency = i;
    }
    Node(int f, Node l, Node r){
        this.frequency = f;
        this.leftchild = l;
        this.rightchild = r;
    }
    //getters for the Node class
    int getFrequency(){
        return this.frequency;
    }
    char getChar(){
        return this.data;
    }

    Node getRightChild(){
        return this.rightchild;
    }

    Node getLeftChild(){
        return this.leftchild;
    }

}
