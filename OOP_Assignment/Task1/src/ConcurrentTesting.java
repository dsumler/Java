class MyThread extends Thread{
    @Override
    public void run(){
        ImmutableListImplement<Integer> List = new ImmutableListImplement<>();

        ImmutableListImplement<Integer> List2 = List.add(30);
        ImmutableListImplement<Integer> List3 = List2.add(20);
        ImmutableListImplement<Integer> List4 = List3.remove();
        ImmutableListImplement<Integer> List5 = List4.add(23);
        List5.displayList();
    }
}


public class ConcurrentTesting extends Thread{
    public static void main(String[] args){
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        t1.start();
        t2.start();
        yield();
    }
}
