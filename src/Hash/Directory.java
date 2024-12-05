package Hash;

import java.util.ArrayList;
import java.util.BitSet;
//The directories store addresses of the buckets in pointers.
// An id is assigned to each directory which may change each time when Directory Expansion takes place.
import java.util.ArrayList;
import java.util.BitSet;

public class Directory {
    private ArrayList<Integer> directory; // adresar blokov
    private int globalDepth;

    public Directory(int initialDepth) {
        this.globalDepth = initialDepth;
        this.directory = new ArrayList<>();
        initializeDirectory(initialDepth);
    }

    private void initializeDirectory(int depth) {
        int size = (int) Math.pow(2, depth);
        for (int i = 0; i < size; i++) {
            directory.add(-1); // incializacia s invalidnou adresou
        }
    }

    private int bitSetToIndex(BitSet bitSet) {
        int index = 0;
        for (int i = 0; i < globalDepth; i++) {
            if (bitSet.get(i)) {
                index |= (1 << i); // nastavenie bitu na indexe
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
        if (globalDepth >= 26) { // Maximalne 26 bits (2^26)
            throw new IllegalStateException("Maximalna hlbka dosiahnuta!");
        }
        for (int i = 0; i < oldSize; i++) {
            directory.add(directory.get(i)); // Duplikovanie blokovych adries
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