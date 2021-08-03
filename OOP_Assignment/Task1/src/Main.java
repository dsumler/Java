
public class Main {
    public static void main(String[] args) {

        ImmutableListImplement<Integer> List= new ImmutableListImplement<>();


        ImmutableListImplement<Integer> List2 = List.add(30);
        System.out.println("Testing the add(T element) method. Adding '30' to List2.\nDisplaying List2");
        List2.displayList();
        System.out.println("\nList.isEmpty() (expected output : true) : " +List.isEmpty());
        List.displayList();
        System.out.println("List.equals(List2) (expected output : false) : " +List.equals(List2)+"\nList :");
        List.displayList();
        System.out.println("List2 : ");
        List2.displayList();
        ImmutableListImplement<Integer> List3 = List2.add(10);
        System.out.println("\nList3.contains(10) (expected output : true) : " +List3.contains(10)+"\nList 3 :");
        List3.displayList();
        ImmutableListImplement<Integer> List4 = List3.remove();
        System.out.println("\nList4 = List3.remove() . List3 : ");
        List3.displayList();
        System.out.println("\nList4 :");
        List4.displayList();
        System.out.println("\nList4.equals(List2) (expected output : true) : " +List4.equals(List2)+"\nList 4:");
        List4.displayList();
        System.out.println("\nList2 :");
        List2.displayList();
        ImmutableListImplement<Integer> List5 = List4.clear();
        System.out.println("\nList5 = List4.clear() (expected output of List5.isEmpty() : true) : "+List5.isEmpty()+"\nList4 :");
        List4.displayList();
        System.out.println("\nList5 :");
        List5.displayList();
    }
}