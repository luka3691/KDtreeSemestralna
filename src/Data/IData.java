package Data;

public interface IData<T> extends IRecord<T> {
    boolean ownEquals(T data);
    public T createClass(); // kopirovaci konstruktor ktory vracia sameho seba
}
