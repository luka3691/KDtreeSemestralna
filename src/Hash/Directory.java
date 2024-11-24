package Hash;
import java.util.HashMap;
import java.util.Map;
import java.util.BitSet;
//The directories store addresses of the buckets in pointers.
// An id is assigned to each directory which may change each time when Directory Expansion takes place.
public class Directory {
    private Map<BitSet, Bucket> directory;
    private int globalDepth;

    public Directory(int initialDepth) {
        this.globalDepth = initialDepth;
        this.directory = new HashMap<>();
        initializeBuckets(initialDepth);
    }
    private String bitSetToKey(BitSet bitSet, int depth) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            key.append(bitSet.get(i) ? '1' : '0');
        }
        return key.toString();
    }
    private void initializeBuckets(int depth) {
        int size = (int) Math.pow(2, depth);
        for (int i = 0; i < size; i++) {
            BitSet prefix = BitSet.valueOf(new long[]{i});
            directory.put(prefix, new Bucket(depth));
        }
    }

    public Bucket getBucket(BitSet hashPrefix) {
        return directory.get(hashPrefix);
    }

    public void doubleDirectory() {
        Map<BitSet, Bucket> newDirectory = new HashMap<>();
        for (Map.Entry<BitSet, Bucket> entry : directory.entrySet()) {
            BitSet prefix = entry.getKey();
            Bucket bucket = entry.getValue();

            BitSet zeroPrefix = (BitSet) prefix.clone();
            BitSet onePrefix = (BitSet) prefix.clone();
            onePrefix.set(globalDepth);  // Add an additional bit for doubling

            newDirectory.put(zeroPrefix, bucket);
            newDirectory.put(onePrefix, bucket);
        }
        globalDepth++;
        directory = newDirectory;
    }
    public void updateBucketMapping(BitSet prefix, Bucket bucket) {
        String key = bitSetToKey(prefix, globalDepth);
        directory.put(prefix, bucket);
    }

    public int getGlobalDepth() {
        return globalDepth;
    }
}