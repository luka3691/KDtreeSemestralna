import java.io.*;
import java.util.ArrayList;

public class Block <T extends IData<T>> implements IRecord<T> {
    private int blockFactor;
    private int pocetValidnych;
    private int nextVolnyBlock;
    private int predchadazajuciBlock;
    private ArrayList<T> records;
//vytvorim si blok s jednym zaznamom a ostanymi prazndimy
    public Block(int blockFactor, T instanciaTriedy) {
        this.blockFactor = blockFactor;

        this.records = new ArrayList<T>(blockFactor);
        this.records.add(instanciaTriedy.createClass());
        for (int i = 1; i < blockFactor; i++) {
            this.records.add(instanciaTriedy.createClass());
        }
        this.nextVolnyBlock = 0;
        this.predchadazajuciBlock = 0;
    }
    public Block(byte[] blockData, int blockFactor, T data) {
        this.blockFactor = blockFactor;
        this.records = new ArrayList<T>(blockFactor);
        this.records.add(data.createClass());
        for (int i = 1; i < blockFactor; i++) {
            this.records.add(data.createClass());
        }
        this.fromByteArray(blockData);
    }
    public boolean insertRecord(T newData) {
        //TODO implementovat insert
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
        //TODO implementovat insert
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
           for (T record : this.records)  {
               hlpOutStream.write(record.toByteArray());
           }
            hlpOutStream.writeInt(pocetValidnych);
            hlpOutStream.writeInt(nextVolnyBlock);
            hlpOutStream.writeInt(predchadazajuciBlock);

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
            for (int i = 0; i < blockFactor; i++) {
                byte[] zaznamy = new byte[this.records.getFirst().getSize()];
                for (int j = 0; j < records.getFirst().getSize(); j++) {
                    zaznamy[j] = hlpInStream.readByte();
                }
                T newData = this.records.getFirst().createClass();
                newData.fromByteArray(zaznamy);
                this.records.set(i, newData);
            }
            this.pocetValidnych = hlpInStream.readInt();
            this.nextVolnyBlock = hlpInStream.readInt();
            this.predchadazajuciBlock = hlpInStream.readInt();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    public int getPocetValidnych() {
        return this.pocetValidnych;
    }


    @Override
    public int getSize() {
    //vracia to aky je velky blok
        return blockFactor * this.records.getFirst().getSize() + 3 * Integer.BYTES;
    }


    public ArrayList<T> getRecords() {
        return this.records;
    }

    public int getNextVolnyBlock() {
        return nextVolnyBlock;
    }

    public void setNextVolnyBlock(int nextVolnyBlock) {
        this.nextVolnyBlock = nextVolnyBlock;
    }

    public int getPredchadazajuciBlock() {
        return predchadazajuciBlock;
    }

    public void setPredchadazajuciBlock(int predchadazajuciBlock) {
        this.predchadazajuciBlock = predchadazajuciBlock;
    }


}
