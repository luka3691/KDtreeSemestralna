package Hash;

import java.util.ArrayList;
import java.util.List;
//The buckets are used to hash the actual data.
public class Bucket<T extends IDataWithHash<T>> {
    private List<T> records;
    private int localDepth;

    public Bucket(int localDepth) {
        this.records = new ArrayList<>();
        this.localDepth = localDepth;
    }

    public boolean isFull(int maxSize) {
        return records.size() >= maxSize;
    }

    public int getLocalDepth() {
        return localDepth;
    }

    public void incrementDepth() {
        localDepth++;
    }

    public List<T> getRecords() {
        return records;
    }

    public void addRecord(T record) {
        records.add(record);
    }

    public void clearRecords() {
        records.clear();
    }
}