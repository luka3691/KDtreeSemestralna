package Hash;

import Data.TestNavstevaClass;

import java.io.*;
import java.util.BitSet;

public class TestClassWithIDHash implements IDataWithHash<TestClassWithIDHash> {
    private int adresa;
    private int id;
    public TestClassWithIDHash(int adresa, int id) {
        this.adresa = adresa;
        this.id = id;
    }
    public TestClassWithIDHash() {
        this.adresa = 0;
        this.id = 0;
    }


    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        try {
            hlpOutStream.writeInt(this.id);
            hlpOutStream.writeInt(this.adresa);
            return hlpByteArrayOutputStream.toByteArray();
        } catch (IOException ex) {

        }
        return new byte[0];
    }

    @Override
    public void fromByteArray(byte[] array) {
        ByteArrayInputStream hlpByteArrayInputStream= new ByteArrayInputStream(array);
        DataInputStream hlpOutStream = new DataInputStream(hlpByteArrayInputStream);
        try {
            this.id = hlpOutStream.readInt();
            this.adresa = hlpOutStream.readInt();
        } catch (IOException ex) {

        }
    }

    @Override
    public int getSize() {
        return Integer.BYTES * 2;
    }

    @Override
    public BitSet getHash() {
        return BitSet.valueOf(new long[]{this.id});
    }

    @Override
    public boolean ownEquals(TestClassWithIDHash data) {
        return data.getId() == this.id;
    }

    @Override
    public TestClassWithIDHash createClass() {
        return new TestClassWithIDHash();
    }

    public int getAdresa() {
        return adresa;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TestClassWithIDHash{" +
                "adresa=" + adresa +
                ", id=" + id +
                '}';
    }
}
