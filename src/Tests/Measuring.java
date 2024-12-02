package Tests;

import Data.IData;
import Data.TestClass;
import Heap.HeapFile;

import java.util.ArrayList;
import java.util.Random;

public class Measuring {
    private int pocetOperacii = 200;
    private int pocetData= 202;
    private
    HeapFile<TestClass> testHeap;
    ArrayList<Pair<TestClass>> data;
    Random random;
    private int id;
    public Measuring() {
        TestClass test = new TestClass();
        this.testHeap = new HeapFile<>(400, "Data.bin", "Riadiace.bin", test);
        this.data = new ArrayList<>();
        random = new Random();
        id = 0;
    }

    public void insert() {
        for (int i = 1; i < pocetData; i++) {
            this.createData();
        }
        //System.out.println("Pocet vlozenych dat:" + pocetOperacii+ ". Počet dát v strome:" + this.tree.numberOfData());
    }
    private void createData() {
        TestClass test = new TestClass(); //dopnit ked testovat
        int returnAdresa = this.testHeap.insert(test);
        if (returnAdresa < 1) {
            System.out.println("Error pri inserte");
        }
        this.data.add(new Pair<>(test, returnAdresa));
        id++;
    }

    public void test() {
        int pocetPokus = pocetOperacii;
        int pocetDelete = 0;
        int pocetInsert = 0;
        for (int i = 0; i < pocetPokus; i++) {
            switch (random.nextInt(1, 4)) {
                case 1:
                    System.out.println("Inserting");
                    this.createData();
                    pocetInsert++;
                    break;
                case 2:
                    Pair<TestClass> data = this.data.get(random.nextInt(0, this.data.size()-1));
                    if (this.testHeap.get(data.adresa, data.data) == null) {
                        System.out.println("Error");
                    } else {
                        System.out.println("Found");
                    };
                    break;
                case 3:
                    System.out.println("Deleting.");
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
