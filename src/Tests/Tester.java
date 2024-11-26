package Tests;

import Hash.ExtendibleHash;
import Hash.TestClassWithHash;

import java.util.ArrayList;
import java.util.Random;

public class Tester {
    private int pocetOperacii = 100;
    private int pocetData= 202;
    private
    ExtendibleHash<TestClassWithHash> testHeap;
    ArrayList<TestClassWithHash> data;
    Random random;
    public Tester() {
        TestClassWithHash test = new TestClassWithHash("Prvy", "Prvy", 0);
        this.testHeap = new ExtendibleHash<>(400, "Data.bin", "Riadiace.bin", test);
        this.data = new ArrayList<>();
        random = new Random();

    }

    public void insert() {
        /*
        16- 10000
4- 00100
6- 00110
22- 10110
24- 11000
10- 01010
31- 11111
7- 00111
9- 01001
20- 10100
26- 11010
         */
        this.insert(16);
        this.insert(4);
        this.insert(22);
        this.insert(24);
        this.insert(10);
        this.insert(31);
        this.insert(7);
        this.insert(9);
        this.insert(20);
        this.insert(26);

        for (int i = 50; i < 200; i++) {
            this.insert(i);
        }
    }

    public void insert(int id) {
        TestClassWithHash test1 = new TestClassWithHash("test", "test", id);
        this.testHeap.insert(test1);
        if (this.testHeap.get(test1) == null) {
            System.out.println("Error" + id);
        }
    }
}
