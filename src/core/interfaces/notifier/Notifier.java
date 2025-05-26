package core.interfaces.notifier;

import core.utils.events.DataType;
import core.utils.events.StorageListener;

public interface Notifier {
    void subscribe(DataType type, StorageListener listener);
    void unsubscribe(DataType type, StorageListener listener);
    void notify(DataType type);
}