import java.util.ArrayList;

public class ASTNode {
    String data;
    //arraylist to store children of this node
    ArrayList<ASTNode> children = new ArrayList<>();

    public ASTNode(String data, ASTNode child){
        this.data = data;
        this.children.add(child);
    }

    public void addChild(ASTNode child){
        this.children.add(child);
    }

    public ASTNode(){
        this.data = null;
    }

    public String getData(){
        return this.data;
    }

    public ASTNode getChild(int index){
        return this.children.get(index);
    }

    public int childrenNumber(){
        return this.children.size();
    }

    public String getChildData(int index){
        //only practical when the parent node has one child
        return this.children.get(index).getData();
    }
    public void setData(String data){
        this.data = data;
    }

}
