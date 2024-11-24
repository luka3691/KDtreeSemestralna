import java.io.*;
import java.util.Random;

public class HeapFile<T extends IData<T>> {
    private int blockFactor;
    private int velkostCluster;
    private int adresaNaPrvyVolnyBlok; // toto neukazuje na koniec suboru
    private int adresaNaPrvyCiastocneVolnyBlok;
    private RandomAccessFile subor;
    private RandomAccessFile riadiaceData;
    //velkostClustra == velkostNajmensejAlokovanejJednotky
    public HeapFile(int velkostClustra, String suborName, String suborRiadiace, T insanciaGenerickej) {
        //this.blockFactor = blockFactor; // vypocitat
        try {
            this.subor = new RandomAccessFile(suborName, "rw"); // uz su
            this.riadiaceData = new RandomAccessFile(suborRiadiace, "rw"); // uz su
            if (this.riadiaceData.length() == 0) {
                //vypocitat blokovaci faktor == pocet zaznamov v bloku
                this.velkostCluster = velkostClustra;
                int velkostVkladanychDat = insanciaGenerickej.getSize();
                blockFactor = Math.floorDiv(velkostClustra - Integer.BYTES*3, velkostVkladanychDat);
                Block<T> prvyBlok = new Block<>(blockFactor, insanciaGenerickej);
                this.adresaNaPrvyVolnyBlok = 0;
                this.adresaNaPrvyCiastocneVolnyBlok = 0;
            } else {
                this.readRiadiaceData();
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
            this.adresaNaPrvyVolnyBlok = hlpOutStream.readInt();
            this.adresaNaPrvyCiastocneVolnyBlok = hlpOutStream.readInt();
        } catch (IOException ex) {

        }
    }

    public int insert(T data) {
        //vrati mi adresu bloku kde sa data nachadzaju
        try {
            //davam na koniec subor
            int adresaNaVratenie;
            if (adresaNaPrvyCiastocneVolnyBlok != 0) {
                this.subor.seek(getAdresaBloku(adresaNaPrvyCiastocneVolnyBlok));
                byte[] blok = new byte[velkostCluster];
                this.subor.read(blok);
                Block<T> readBlock = new Block<>(blok, blockFactor, data);
                boolean jePlny = readBlock.insertRecord(data);
                adresaNaVratenie = this.adresaNaPrvyCiastocneVolnyBlok;
                if (jePlny) {
                    this.adresaNaPrvyCiastocneVolnyBlok = readBlock.getNextVolnyBlock();
                }
                this.subor.seek(getAdresaBloku(adresaNaPrvyCiastocneVolnyBlok));
                this.subor.write(padding(readBlock.toByteArray()));
                return adresaNaVratenie;
            } else if (adresaNaPrvyVolnyBlok != 0) {
                this.subor.seek(getAdresaBloku(adresaNaPrvyVolnyBlok));
                byte[] blok = new byte[velkostCluster];
                System.out.println(blockFactor);
                Block<T> readBlock = new Block<>(blockFactor, data);
                boolean jePlny = readBlock.insertRecord(data);
                this.subor.seek(getAdresaBloku(adresaNaPrvyVolnyBlok));
                adresaNaVratenie = this.adresaNaPrvyVolnyBlok;
                this.adresaNaPrvyVolnyBlok = readBlock.getNextVolnyBlock();
                if (!jePlny) {
                    this.adresaNaPrvyCiastocneVolnyBlok = adresaNaVratenie;
                }
                this.subor.write(padding(readBlock.toByteArray()));
                this.adresaNaPrvyVolnyBlok++;
                return adresaNaVratenie;
            } else {
                int adresaNaPredchadzajuci;
                if (this.subor.length() == 0) {
                    adresaNaPredchadzajuci = 0;
                } else {
                    adresaNaPredchadzajuci = this.getposlednaAdresaBloku();
                }
                this.subor.seek(this.subor.length());
                byte[] blok = new byte[velkostCluster];
                System.out.println(blockFactor);
                Block<T> readBlock = new Block<>(blockFactor, data);
                boolean jePlny = readBlock.insertRecord(data);
                readBlock.setPredchadazajuciBlock(adresaNaPredchadzajuci);
                readBlock.setNextVolnyBlock(this.adresaNaPrvyCiastocneVolnyBlok);
                this.subor.seek(getAdresaBloku(getposlednaAdresaBloku()+1));
                this.subor.write(padding(readBlock.toByteArray()));
                adresaNaVratenie = getposlednaAdresaBloku();
                if (jePlny) {
                    this.adresaNaPrvyCiastocneVolnyBlok = readBlock.getNextVolnyBlock();
                } else {
                    this.adresaNaPrvyCiastocneVolnyBlok = adresaNaVratenie;
                }
                return adresaNaVratenie;
            }
            /*
            this.subor.seek(this.subor.length());
            this.subor.write(data.toByteArray());*/
        }catch (IOException ex) {

        }
    return -1;
    }
    private int getPocetBlokov() {
        try {
            return (int) Math.floor((double) this.subor.length() / this.velkostCluster);
        } catch (IOException ex) {

        }

        return 0;
    }

    private int getAdresaBloku(int indexBloku) {
        return  (indexBloku - 1) * velkostCluster;
    }

    private int getposlednaAdresaBloku() throws IOException {
        return  ((int)this.subor.length() / velkostCluster);
    }

    public boolean delete(int adresaBloku, T data) {
        try {
            this.subor.seek(getAdresaBloku(adresaBloku));
            byte[] blok = new byte[velkostCluster];
            this.subor.read(blok);
            Block<T> readBlock = new Block<>(blok, blockFactor, data);
            int cisloRecordu = readBlock.findRecord(data);
            if (cisloRecordu == -1) {
                System.out.println("Error");
                return false;
            } else {
                boolean jePrazdny = readBlock.removeRecord(data, cisloRecordu);
                if (jePrazdny) {
                    boolean mazem = true;
                    int aktualnaAdresa = adresaBloku;
                    if (adresaBloku == getposlednaAdresaBloku()) {
                        while (mazem) {
                            //problematicke pretypovanie adresy0&
                            this.zkratBlokOdKonca();
                            aktualnaAdresa = readBlock.getPredchadazajuciBlock();
                            if (readBlock.getPocetValidnych() != 0) {
                                this.adresaNaPrvyCiastocneVolnyBlok = aktualnaAdresa;
                                mazem = false;
                            } else {
                                this.adresaNaPrvyVolnyBlok = readBlock.getNextVolnyBlock();
                            }
                            this.subor.seek(getAdresaBloku(aktualnaAdresa));
                            blok = new byte[velkostCluster];
                            this.subor.read(blok);
                            readBlock = new Block<>(blok, blockFactor, data);

                        }
                    } else {
                        this.adresaNaPrvyCiastocneVolnyBlok = readBlock.getNextVolnyBlock();
                        readBlock.setNextVolnyBlock(this.adresaNaPrvyVolnyBlok);
                        this.adresaNaPrvyVolnyBlok = adresaBloku;
                    }
                    this.subor.seek(getAdresaBloku(aktualnaAdresa));
                    this.subor.write(padding(readBlock.toByteArray()));
                } else {
                   if (readBlock.getPocetValidnych() == readBlock.getRecords().size() - 1) { //bola plna kapacita a teraz nie je
                       readBlock.setNextVolnyBlock(this.adresaNaPrvyCiastocneVolnyBlok);
                       this.adresaNaPrvyCiastocneVolnyBlok = adresaBloku;
                   }
                    this.subor.seek(getAdresaBloku(adresaBloku));
                    this.subor.write(padding(readBlock.toByteArray()));
                }
            }
        } catch (IOException ex) {

        }
return false;
        //ked sa mi vyprazdni na konci bloku tak orezem subor
        //ked blokoval bloky ine pozriem si
        //urobim si cyklus a pozeram valid count a orezavam, musim menit referencie
        //ked mazem a je uplne volny tak zobiem blok, ulozim do neho adresu aktualne volneho prveho bloku a prepisem v heapfile adresu na prvy volny na aktualny
    }


    private void zkratBlokOdKonca() {
        try {
            this.subor.setLength(this.subor.length() - this.velkostCluster);
        }catch (IOException ex) {

        }

    }
    public T get(int adresaBloku, T dataSKlucom) {
        //vrati data podla adresy
        try {
            this.subor.seek(getAdresaBloku(adresaBloku));
            byte[] blok = new byte[velkostCluster];
            this.subor.read(blok);
            Block<T> readBlock = new Block<>(blok, blockFactor, dataSKlucom);
            for (T data : readBlock.getRecords()) {
                if (dataSKlucom.ownEquals(data)) {
                    return data;
                }
            }
        }catch (IOException ex) {

        }
        return null;
    }

    public void close() {
        try {
            this.subor.close();
            this.riadiaceData.seek(0);
            ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
            DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
            hlpOutStream.writeInt(blockFactor);
            hlpOutStream.writeInt(velkostCluster);
            hlpOutStream.writeInt(adresaNaPrvyVolnyBlok);
            hlpOutStream.writeInt(adresaNaPrvyCiastocneVolnyBlok);
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
}
