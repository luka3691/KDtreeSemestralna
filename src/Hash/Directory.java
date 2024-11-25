package Hash;

import java.util.ArrayList;
import java.util.BitSet;
//The directories store addresses of the buckets in pointers.
// An id is assigned to each directory which may change each time when Directory Expansion takes place.
import java.util.ArrayList;
import java.util.BitSet;

public class Directory {
    private ArrayList<Integer> directory; // Stores block addresses
    private int globalDepth;

    public Directory(int initialDepth) {
        this.globalDepth = initialDepth;
        this.directory = new ArrayList<>();
        initializeDirectory(initialDepth);
    }

    private void initializeDirectory(int depth) {
        int size = (int) Math.pow(2, depth);
        for (int i = 0; i < size; i++) {
            directory.add(0); // Initialize with invalid address (e.g., -1)
        }
    }

    private int bitSetToIndex(BitSet bitSet) {
        int index = 0;
        for (int i = 0; i < globalDepth; i++) {
            if (bitSet.get(i)) {
                index |= (1 << i); // Set the corresponding bit in the index
            }
        }
        return index;
    }

    public int getBlockAddress(BitSet hashPrefix) {
        int index = bitSetToIndex(hashPrefix);
        return directory.get(index);
    }

    public void setBlockAddress(BitSet hashPrefix, int address) {
        int index = bitSetToIndex(hashPrefix);
        directory.set(index, address);
    }

    public void doubleDirectory() {
        int oldSize = directory.size();
        for (int i = 0; i < oldSize; i++) {
            directory.add(directory.get(i)); // Duplicate the block addresses
        }
        globalDepth++;
    }

    public int getGlobalDepth() {
        return globalDepth;
    }

    public int size() {
        return directory.size();
    }
}