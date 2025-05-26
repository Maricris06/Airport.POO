package core.models.storage;

import core.utils.events.DataType;
import core.utils.events.StorageListener;
import java.util.*;

public class StorageNotifier {
    private final Map<DataType, List<StorageListener>> listeners = new EnumMap<>(DataType.class);
    
    public StorageNotifier() {
        Arrays.stream(DataType.values()).forEach(type -> listeners.put(type, new ArrayList<>()));
    }
    
    public void subscribe(DataType type, StorageListener listener) {
        listeners.get(type).add(listener);
    }
    
    public void unsubscribe(DataType type, StorageListener listener) {
        listeners.get(type).remove(listener);
    }
    
    public void notify(DataType type) {
        listeners.getOrDefault(type, Collections.emptyList())
                .forEach(listener -> listener.update(type));
    }
}