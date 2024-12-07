package Tests;

import Hash.BlockWithHash;
import Hash.ExtendibleHash;
import Hash.TestClassWithECVHash;
import Hash.TestClassWithHash;

import java.util.ArrayList;
import java.util.Random;

public class MeasuringWithHashString {
    private int pocetOperacii = 5;
    private int pocetData= 20;
    private ExtendibleHash<TestClassWithECVHash> ecvTest;

    ArrayList<TestClassWithECVHash> data;
    Random random;
    private int id;
    private static final char[] CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int BASE = CHARACTERS.length;
    private static int counter = 0;
    public MeasuringWithHashString() {
        //TestClassWithHash test = new TestClassWithHash();
        TestClassWithECVHash test2 = new TestClassWithECVHash();
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
        TestClassWithECVHash test = new TestClassWithECVHash(id, getNextUniqueString(), id); //dopnit ked testovat
        TestClassWithECVHash test2 = new TestClassWithECVHash(id, "AADE", id);

        this.ecvTest.insert(test);
        this.data.add(test);
        if (this.ecvTest.get(test) == null) {
            System.out.println("Error");
        }
        /*
        if (this.ecvTest.get(test2) == null && id > 113) {
            System.out.println("TI");
        }
         */
        id++;
    }

    public void test() {
        int pocetPokus = pocetOperacii;
        int pocetDelete = 0;
        int pocetInsert = 0;
        //ArrayList<BlockWithHash<TestClassWithHash>> list = this.testHeap.getVsetkyBloky(this.data.getFirst());
        for (int i = 0; i < pocetPokus; i++) {
            switch (random.nextInt(1, 3)) {
                case 1:
                    System.out.println("Inserting");
                    this.createData();
                    pocetInsert++;
                    break;
                case 2:
                    TestClassWithECVHash data = this.data.get(random.nextInt(0, this.data.size()-1));

                    if (this.ecvTest.get(data) == null) {
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
        this.ecvTest.close();
    }

    public static synchronized String getNextUniqueString() {
        if (counter >= Math.pow(BASE, 4)) {
            throw new IllegalStateException("Uz neexistuje viac.");
        }
        return toBase62(counter++);
    }

    private static String toBase62(int number) {
        StringBuilder result = new StringBuilder();
        do {
            result.insert(0, CHARACTERS[number % BASE]);
            number /= BASE;
        } while (number > 0);

        while (result.length() < 4) {
            result.insert(0, CHARACTERS[0]); //vyplnennie
        }

        return result.toString();
    }
}
