//interface for the immutablelist
public interface ImmutableList<T> {
    int size();
    void displayList();
    boolean isEmpty();
    boolean contains(T element);
    ImmutableListImplement<T> remove();
    //ImmutableList <T> prepend(T element);
    void copy(ImmutableListImplement<T> list);
    ImmutableListImplement <T> clear();
    ImmutableListImplement<T> add(T element);
    boolean equals(ImmutableListImplement<T> list);
}