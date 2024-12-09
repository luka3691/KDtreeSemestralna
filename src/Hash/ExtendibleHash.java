package Hash;

import Heap.HeapFile;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class ExtendibleHash<T extends IDataWithHash<T>> {
    //private int globalDepth;
    private int blockFactor;
    private int velkostCluster;
    /*
    private int adresaNaPrvyVolnyBlok; // toto neukazuje na koniec suboru
    private int adresaNaPrvyCiastocneVolnyBlok;

     */
    private Directory directory;
    private RandomAccessFile subor;
    private RandomAccessFile riadiaceData;
    //velkostClustra == velkostNajmensejAlokovanejJednotky
    public ExtendibleHash(int velkostClustra, String suborName, String suborRiadiace, T insanciaGenerickej) {
        //this.blockFactor = blockFactor; // vypocitat
        try {
            this.subor = new RandomAccessFile(suborName, "rw"); // uz su
            this.riadiaceData = new RandomAccessFile(suborRiadiace, "rw"); // uz su
            if (this.riadiaceData.length() == 0) {
                //vypocitat blokovaci faktor == pocet zaznamov v bloku
                this.velkostCluster = velkostClustra;
                int velkostVkladanychDat = insanciaGenerickej.getSize();
                blockFactor = Math.floorDiv(velkostClustra - Integer.BYTES*4, velkostVkladanychDat);
                this.directory = new Directory(1);
            } else {
                this.readRiadiaceData();
                //zatial nemam urobene naciatanie riadiacich dat do suboru
            }

        } catch (IOException ex) {

        }
    }

    private void readRiadiaceData() {
        try {
            this.riadiaceData.seek(0);
            byte[] riadiaceData = new byte[4*Integer.BYTES];
            this.riadiaceData.readFully(riadiaceData);
            ByteArrayInputStream hlpByteArrayInputStream= new ByteArrayInputStream(riadiaceData);
            DataInputStream hlpOutStream = new DataInputStream(hlpByteArrayInputStream);
            this.blockFactor = hlpOutStream.readInt();
            this.velkostCluster = hlpOutStream.readInt();
            int globalDepth = hlpOutStream.readInt();
            int velkostAdresaru = hlpOutStream.readInt();
            byte[] directory = new byte[velkostAdresaru*Integer.BYTES];
            this.riadiaceData.readFully(directory);
            ByteArrayInputStream hlpByteArrayInputStreamAdresy = new ByteArrayInputStream(directory);
            DataInputStream hlpOutStreamAdresy = new DataInputStream(hlpByteArrayInputStreamAdresy);
            ArrayList<Integer> directoryData = new ArrayList<>();
            for (int i = 0; i < velkostAdresaru; i++) {
                directoryData.add(hlpOutStreamAdresy.readInt());
            }
            this.directory = new Directory(globalDepth, directoryData);
        } catch (IOException ex) {

        }
    }

    public void insert(T record) {
        try {
            boolean jeVlozene = false;
            while (!jeVlozene) {
                BitSet hash = record.getHash();
                BitSet prefix = hash.get(0, directory.getGlobalDepth());
                int adresa = directory.getBlockAddress(prefix);
                if (adresa == -1) {
                    BlockWithHash<T> readBlock = new BlockWithHash<>( blockFactor, 1, record);
                    readBlock.insertRecord(record);
                    int adresaNaVratenie = getposlednaAdresaBloku();
                    this.subor.seek(getAdresaBloku(adresaNaVratenie));
                    this.subor.write(padding(readBlock.toByteArray()));
                    directory.setBlockAddress(prefix, adresaNaVratenie);
                    jeVlozene = true;
                } else {
                    this.subor.seek(getAdresaBloku(adresa));
                    byte[] blok = new byte[velkostCluster];
                    this.subor.read(blok);
                    BlockWithHash<T> readBlock = new BlockWithHash<>(blok, blockFactor, record);
                    if (readBlock.isFull()) {
                        if (readBlock.getLocalDepth() == directory.getGlobalDepth()) {
                            directory.doubleDirectory();
                        }
                        splitBlock(readBlock, prefix, adresa);
                    } else {
                        readBlock.insertRecord(record);
                        this.subor.seek(getAdresaBloku(adresa));
                        this.subor.write(padding(readBlock.toByteArray()));
                        jeVlozene = true;
                    }
                }

            }

        }catch (IOException ex) {

        }

    }

    private void splitBlock(BlockWithHash<T> block, BitSet prefix, int adresa) {
        try {
            int localDepth = block.getLocalDepth();
            block.incrementDepth();

            BlockWithHash<T> newBlock = new BlockWithHash<T>(blockFactor, localDepth + 1, block.getRecords().getFirst().createClass());
            List<T> recordsToRedistribute = new ArrayList<>(block.getRecords());
            block.clearRecords();
            BitSet originalPrefix = prefix.get(0, localDepth);
            for (T record : recordsToRedistribute) {
                BitSet hash = record.getHash();
                BitSet newPrefix = hash.get(0, localDepth + 1);
                if (originalPrefix.equals(newPrefix)) {
                    block.insertRecord(record);
                } else {
                    newBlock.insertRecord(record);
                }
            }

            this.subor.seek(getAdresaBloku(adresa));
            this.subor.write(padding(block.toByteArray()));
// Update directory pointers
            this.directory.setBlockAddress(originalPrefix, adresa);
            if (!newBlock.isEmpty()) {
                int adresaNaVratenie = this.getposlednaAdresaBloku();
                this.subor.seek(getAdresaBloku(adresaNaVratenie));
                this.subor.write(padding(newBlock.toByteArray()));
                BitSet newPrefix = (BitSet) prefix.clone();
                newPrefix.set(localDepth); // Flip the next significant bit
                this.directory.setBlockAddress(newPrefix, adresaNaVratenie);
            }

        } catch (IOException ex) {

        }
    }
    private int getPocetBlokov() {
        try {
            return (int) Math.floor((double) this.subor.length() / this.velkostCluster);
        } catch (IOException ex) {

        }

        return 0;
    }

    private int getAdresaBloku(int indexBloku) {
        return  (indexBloku) * velkostCluster;
    }

    private int getposlednaAdresaBloku() throws IOException {
        return  ((int)this.subor.length() / velkostCluster) ;
    }
/*
    //toto sa pouziva len ked sa vymaze
    private void vlozDoZretazenia(BlockWithHash<T> readBlock, int adresaBloku, int adresaNaUzZretazenyBlok, T data) throws IOException {
        readBlock.setNextVolnyBlock(adresaNaUzZretazenyBlok); // zapiseme iba dalsi volny blok kedze predosli neexistuje (ja som najnovsi)
        readBlock.setPredchadazajuciBlock(0);
        this.subor.seek(getAdresaBloku(adresaBloku));
        this.subor.write(padding(readBlock.toByteArray()));
        if (adresaNaUzZretazenyBlok != 0) {
            this.subor.seek(getAdresaBloku(adresaNaUzZretazenyBlok));
            byte[] blok = new byte[velkostCluster];
            this.subor.read(blok);
            BlockWithHash<T> dalsiVZretazeni = new BlockWithHash<>(blok, blockFactor, data);
            dalsiVZretazeni.setPredchadazajuciBlock(adresaBloku); // zapiseme adresu na vkladany blok
            this.subor.seek(getAdresaBloku(adresaNaUzZretazenyBlok));
            this.subor.write(padding(dalsiVZretazeni.toByteArray()));
        }
        //this.adresaNaPrvyCiastocneVolnyBlok = adresaBloku;
    }

    //toto sa pouziva iba ked je plny block
    private void odoberZoZretazenia(BlockWithHash<T> readBlock, int adresaBloku, T data) throws IOException {
        if (readBlock.getPredchadazajuciBlock() != 0) {
            this.subor.seek(getAdresaBloku(readBlock.getPredchadazajuciBlock()));
            byte[] blok = new byte[velkostCluster];
            this.subor.read(blok);
            BlockWithHash<T> predchadzajuciVZretazeni = new BlockWithHash<>(blok, blockFactor, data);
            predchadzajuciVZretazeni.setNextVolnyBlock(readBlock.getNextVolnyBlock());
            this.subor.seek(getAdresaBloku(readBlock.getPredchadazajuciBlock()));
            this.subor.write(padding(predchadzajuciVZretazeni.toByteArray()));
        }
        if(readBlock.getNextVolnyBlock() != 0) {
            this.subor.seek(getAdresaBloku(readBlock.getNextVolnyBlock()));
            byte[] blok = new byte[velkostCluster];
            this.subor.read(blok);
            BlockWithHash<T> dalsiVZretazeni = new BlockWithHash<>(blok, blockFactor, data);

            dalsiVZretazeni.setPredchadazajuciBlock(readBlock.getPredchadazajuciBlock());
            this.subor.seek(getAdresaBloku(readBlock.getNextVolnyBlock()));
            this.subor.write(padding(dalsiVZretazeni.toByteArray()));
        }

        readBlock.setNextVolnyBlock(0); // zapiseme iba dalsi volny blok kedze predosli neexistuje (ja som najnovsi)
        readBlock.setPredchadazajuciBlock(0);
        this.subor.seek(getAdresaBloku(adresaBloku));
        this.subor.write(padding(readBlock.toByteArray()));
    }


    private void zkratBlokOdKonca() {
        try {
            this.subor.setLength(this.subor.length() - this.velkostCluster);
        }catch (IOException ex) {

        }

    }
 */
    public T get(T dataSKlucom) {
        //vrati data podla adresy
        try {
            BitSet hash = dataSKlucom.getHash();
            int adresa = this.directory.getBlockAddress(hash);
            this.subor.seek(getAdresaBloku(adresa));
            byte[] blok = new byte[velkostCluster];
            this.subor.read(blok);
            BlockWithHash<T> readBlock = new BlockWithHash<>(blok, blockFactor, dataSKlucom);
            for (T data : readBlock.getRecords()) {
                if (dataSKlucom.ownEquals(data)) {
                    return data;
                }
            }
        }catch (IOException ex) {

        }
        return null;
    }
    public void edit(T dataSKlucom, T dataNove) {
        //vrati data podla adresy
        try {
            BitSet hash = dataSKlucom.getHash();
            BitSet noveHash = dataNove.getHash();
            int adresa = this.directory.getBlockAddress(hash);
            int novaAdresa  = this.directory.getBlockAddress(noveHash);
            if (adresa != novaAdresa) {
                return; //kontrola ci sa nezmenil hash
            }
            this.subor.seek(getAdresaBloku(adresa));
            byte[] blok = new byte[velkostCluster];
            this.subor.read(blok);
            T foundData = null;
            int recordNumber = -1;
            BlockWithHash<T> readBlock = new BlockWithHash<>(blok, blockFactor, dataSKlucom);
            ArrayList<T> list = readBlock.getRecords();
            for (int i = 0; i < list.size(); i++) {
                T data = readBlock.getRecords().get(i);
                if (dataSKlucom.ownEquals(data)) {
                    foundData = data;
                    recordNumber = i;
                }
            }
            if (foundData != null) {
                readBlock.removeRecord(dataSKlucom, recordNumber);
                readBlock.insertRecord(dataNove);
                this.subor.seek(getAdresaBloku(adresa));
                this.subor.write(padding(readBlock.toByteArray()));
            }
        }catch (IOException ex) {

        }
    }
    public void close() {
        try {
            this.subor.close();
            this.riadiaceData.seek(0);
            ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
            DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
            hlpOutStream.writeInt(blockFactor);
            hlpOutStream.writeInt(velkostCluster);
            hlpOutStream.write(this.directory.toByteArray());
            this.riadiaceData.seek(0);
            this.riadiaceData.write(hlpByteArrayOutputStream.toByteArray());
            this.riadiaceData.close();
        } catch (IOException ex) {

        }
    }

    public  byte[] padding(byte[] block) {
        byte [] paddedBloc = new byte[velkostCluster];
        System.arraycopy(block, 0, paddedBloc, 0, block.length);
        return paddedBloc;
    }

    public ArrayList<BlockWithHash<T>> getVsetkyBloky(T data) {
        ArrayList<BlockWithHash<T>> list = new ArrayList<>();
        try {
            int maxAdresa = this.getposlednaAdresaBloku();
            for (int i = 0; i < maxAdresa; i++) {
                this.subor.seek(getAdresaBloku(i));
                byte[] blok = new byte[velkostCluster];
                this.subor.read(blok);
                BlockWithHash<T> readBlock = new BlockWithHash<>(blok, blockFactor, data);
                list.add(readBlock);
            }
        }catch (IOException ex) {

        }

        return list;
    }
}
/*V semestrálnej práci je potrebné
použiť rozšíriteľné hešovanie nevyužívajúce preplňujúci súbor a pod. (používate priame
hešovanie). Implementujte efektívny manažment prázdnych blokov v súboroch založený na
ich zreťazení.*/