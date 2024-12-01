package Tests;

import Data.IData;
import Data.TestClass;
import Hash.BlockWithHash;
import Hash.ExtendibleHash;
import Hash.TestClassWithHash;
import Heap.HeapFile;

import java.util.ArrayList;
import java.util.Random;

public class MeasuringWithHash {
    private int pocetOperacii = 200;
    private int pocetData= 250;
    private
    ExtendibleHash<TestClassWithHash> testHeap;
    ArrayList<TestClassWithHash> data;
    Random random;
    private int id;
    public MeasuringWithHash() {
        TestClassWithHash test = new TestClassWithHash("Prvy", "Prvy", 0);
        this.testHeap = new ExtendibleHash<>(400, "DataHash.bin", "RiadiaceHash.bin", test);
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
        TestClassWithHash test = new TestClassWithHash("Prvy", "Prvy", id);
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


    private static class Pair<T extends IData<T>> {
        T data;
        int adresa;

        Pair(T data, int adresa) {
            this.data = data;
            this.adresa = adresa;
        }
    }
}
