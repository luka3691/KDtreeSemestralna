package Hash;

import Data.IRecord;

import java.util.BitSet;

public interface IRecordWithHash<T> {
    byte[] toByteArray();
    void fromByteArray(byte[] array);
    int getSize();
    BitSet  getHash();
}
