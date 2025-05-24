package core.interfaces.repository;

import java.util.List;
import java.util.Comparator;

public interface Repository<T, ID> {
    boolean add(T item);
    boolean remove(ID id);
    T get(ID id);
    List<T> getAll();
    List<T> getAllSorted(Comparator<T> comparator);
}