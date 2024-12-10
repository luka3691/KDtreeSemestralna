package Tests;

import Hash.BlockWithHash;
import Hash.ExtendibleHash;
import Hash.HashData.TestClassWithECVHash;
import Hash.HashData.TestClassWithHash;

import java.util.ArrayList;
import java.util.Random;

public class MeasuringWithHash {
    private int pocetOperacii = 400;
    private int pocetData= 1000;
    private
    ExtendibleHash<TestClassWithHash> testHeap;
    ExtendibleHash<TestClassWithECVHash> ecvTest;

    ArrayList<TestClassWithHash> data;
    Random random;
    private int id;
    private static final char[] CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int BASE = CHARACTERS.length;
    private static int counter = 0;
    public MeasuringWithHash() {
        TestClassWithHash test = new TestClassWithHash();
        TestClassWithECVHash test2 = new TestClassWithECVHash();
        this.testHeap = new ExtendibleHash<>(400, "DataHash.bin", "RiadiaceHash.bin", test);
        this.ecvTest = new ExtendibleHash<>(400, "DataECV.bin", "RiadiaceECV.bin", test2);
        this.data = new ArrayList<>();
        random = new Random();
        id = 1;
    }

    public void insert() {
        for (int i = 1; i < pocetData; i++) {
            this.createData();
        }
        //System.out.println("Pocet vlozenych dat:" + pocetOperacii+ ". Počet dát v strome:" + this.tree.numberOfData());
    }
    private void createData() {
        /*

         */
        TestClassWithHash test = new TestClassWithHash("Prvy", "prvyy", id, "nenene"); //dopnit ked testovat
        this.testHeap.insert(test);
        this.data.add(test);
        if (this.testHeap.get(test) == null) {
            System.out.println("Error");
        }
        this.testHeap.getVsetkyBloky(test);
        id++;
    }

    public void test() {
        int pocetPokus = pocetOperacii;
        int pocetDelete = 0;
        int pocetInsert = 0;
        ArrayList<BlockWithHash<TestClassWithHash>> list = this.testHeap.getVsetkyBloky(this.data.getFirst());
        for (int i = 0; i < pocetPokus; i++) {
            switch (random.nextInt(1, 3)) {
                case 1:
                    System.out.println("Inserting");
                    this.createData();
                    pocetInsert++;
                    break;
                case 2:
                    TestClassWithHash data = this.data.get(random.nextInt(0, this.data.size()-1));
                    if (this.testHeap.get(data) == null) {
                        System.out.println("Error");
                    } else {
                        System.out.println("Found");
                    };
                    break;
                    /*
                case 3:
                    Pair<TestClass> dataNaDelete = this.data.get(random.nextInt(0, this.data.size()-1));
                    pocetDelete++;
                    boolean ret = this.testHeap.delete(dataNaDelete.adresa, dataNaDelete.data);

                    if (! ret ) {
                        System.out.println("Error deleting");
                    }
                    this.data.remove(dataNaDelete);

                    if ( this.testHeap.get(dataNaDelete.adresa, dataNaDelete.data) != null) {
                        System.out.println("Error: deleted data found");
                    };
                    break;


                     */
            }
        }
        testHeap.close();
    }


}
