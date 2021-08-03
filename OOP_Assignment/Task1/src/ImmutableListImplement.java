public class ImmutableListImplement<T> implements ImmutableList<T> {

    private Node head, tail;


    public boolean equals(ImmutableListImplement<T> list){
        ImmutableListImplement<T> compare = new ImmutableListImplement<>();
        compare.copy(list);
        ImmutableListImplement<T> compare2 = new ImmutableListImplement<>();
        compare2.copy(this);

        if(compare.size() != compare2.size()){
            return false;
        }

        while(compare.head != null && compare2.head != null){
            if(!compare.head.data.equals(compare2.head.data)){
                return false;
            }
            compare.head = compare.head.next;
            compare2.head = compare2.head.next;
        }
        return true;
    }

    public void displayList() {
        if (this.size() == 0) {
            System.out.println("[]");
        } else {
            Node list = this.head;
            System.out.print("[");
            while (list.next != null) {
                System.out.print(" " + list.data + ", ");
                list = list.next;
            }
            System.out.print(list.data);
            System.out.print("]");
        }
    }
    public int size() {
        int size = 0;
        ImmutableListImplement<T> list2 = new ImmutableListImplement<>();
        list2.copy(this);
        while(list2.head != null){
            size++;
            list2.head = list2.head.next;
        }
        return size;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean contains(T element){
        Node n = this.head;
        while(n != null){
            if(element.equals(n.data)){
                return true;
            }
            n = n.next;
        }
        return false;
    }

    public ImmutableListImplement<T> add(T element) {
        ImmutableListImplement<T> copy = new ImmutableListImplement<>();
        copy.copy(this);

        Node n = new Node<>(element);
        n.next = null;

        if (copy.head == null) {
            copy.head = n;
        } else {
            Node last = copy.head;
            while (last.next != null) {
                last = last.next;
            }
            copy.tail = last;
            last.next = n;
        }
        return copy;
    }

    public ImmutableListImplement<T> clear(){
        return new ImmutableListImplement<T>();
    }

    public ImmutableListImplement<T> remove(){
        ImmutableListImplement<T> listcopy = new ImmutableListImplement<>();
        listcopy.copy(this);
        if(listcopy.head == null){
            return null;
        } else {
            listcopy.head.next = removenode(listcopy.head.next);
        }
    return listcopy;
    }

    private Node removenode(Node n) {
        if (n.next == null) {
            n.data = null;
            return null;
        } else {
            n.next = removenode(n.next);
        }
        return n;
    }

    private void addNode(T element) {
        Node<T> tail = this.tail;
        Node<T> newNode = new Node<>(element);

        this.tail = newNode;
        if (tail == null) {
            this.head = newNode;
        } else {
            tail.next = newNode;
        }
    }

    public void copy(ImmutableListImplement<T> list) {
        ImmutableListImplement<T> listcopy = new ImmutableListImplement<>();

        Node<T> x = list.head;
        while(x != null){
            listcopy.addNode(x.data);
            x = x.next;
        }
        this.head = listcopy.head;
    }

}