package Data;

public interface IRecord<T> {
    byte[] toByteArray();
    void fromByteArray(byte[] array);
    int getSize();
}
