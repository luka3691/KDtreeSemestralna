package Hash;

import Data.IRecord;

public interface IDataWithHash<T> extends IRecordWithHash<T> {
    boolean ownEquals(T data);
    public T createClass(); // kopirovaci konstruktor ktory vracia sameho seba
}
