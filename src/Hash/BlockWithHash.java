package Hash;

import Hash.HashData.IDataWithHash;
import Hash.HashData.IRecordWithHash;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;

public class BlockWithHash<T extends IDataWithHash<T>> implements IRecordWithHash<T> {
    private int blockFactor;
    private int localDepth;
    private int pocetValidnych;
    private ArrayList<T> records;
//vytvorim si blok s jednym zaznamom a ostanymi prazndimy

    //ostranit block factor
    public BlockWithHash(int blockFactor,int localDepth, T instanciaTriedy) {
        this.blockFactor = blockFactor;

        this.records = new ArrayList<T>(blockFactor);
        this.records.add(instanciaTriedy.createClass());
        for (int i = 1; i < blockFactor; i++) {
            this.records.add(instanciaTriedy.createClass());
        }
        this.localDepth = localDepth;
    }
    public BlockWithHash(byte[] blockData, int blockFactor, T data) {
        this.blockFactor = blockFactor;
        this.records = new ArrayList<T>(blockFactor);
        this.records.add(data.createClass());
        for (int i = 1; i < blockFactor; i++) {
            this.records.add(data.createClass());
        }
        this.fromByteArray(blockData);
    }
    public boolean insertRecord(T newData) {
        this.records.set(pocetValidnych,newData);
        pocetValidnych++;
        return pocetValidnych == blockFactor;
    }
    public int findRecord(T dataToFind) {
        for (int i = 0; i < this.records.size(); i++) {
            if (this.records.get(i).ownEquals(dataToFind)) {
                return i;
            };
        }
        return -1;
    }
    public boolean removeRecord(T newData, int recordNumber) {
        this.records.remove(recordNumber);
        this.records.add(newData.createClass());
        pocetValidnych--;
        return pocetValidnych == 0;
    }
    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try{
            hlpOutStream.writeInt(pocetValidnych);
            hlpOutStream.writeInt(localDepth);
           for (T record : this.records)  {
               hlpOutStream.write(record.toByteArray());
           }


            return hlpByteArrayOutputStream.toByteArray();


        }catch (IOException e){
            throw new IllegalStateException("Error during conversion to byte array.");

        }

    }

    @Override
    public void fromByteArray(byte[] array) {

        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(array);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);

        try {
            this.pocetValidnych = hlpInStream.readInt();
            this.localDepth = hlpInStream.readInt();
            for (int i = 0; i < blockFactor; i++) {
                byte[] zaznamy = new byte[this.records.getFirst().getSize()];
                for (int j = 0; j < records.getFirst().getSize(); j++) {
                    zaznamy[j] = hlpInStream.readByte();
                }
                T newData = this.records.getFirst().createClass();
                newData.fromByteArray(zaznamy);
                this.records.set(i, newData);
            }


        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    public int getPocetValidnych() {
        return this.pocetValidnych;
    }
    public boolean isFull() {
        return this.pocetValidnych == this.blockFactor;
    }
    public boolean isEmpty() {
        return this.pocetValidnych == 0;
    }

    @Override
    public int getSize() {
    //vracia to aky je velky blok
        return blockFactor * this.records.getFirst().getSize() + 2 * Integer.BYTES;
    }

    @Override
    public BitSet getHash() {
        return null;
    }


    public ArrayList<T> getRecords() {
        return this.records;
    }

    public int getLocalDepth() {
        return localDepth;
    }

    public void incrementDepth() {
        localDepth++;
    }


    public void clearRecords() {
        T getFirst = this.records.getFirst();
        this.records.clear();
        for (int i = 0; i < blockFactor; i++) {
            this.records.add(getFirst.createClass());
        }
        this.pocetValidnych = 0;
    }

    @Override
    public String toString() {
        return "{" +
                "blockFactor=" + blockFactor +
                "| localDepth=" + localDepth +
                "| pocetValidnych:" + pocetValidnych +
                "| records=" + records.toString() +
                '}';
    }
}
