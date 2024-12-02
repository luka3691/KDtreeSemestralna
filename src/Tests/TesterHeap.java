package Tests;

import Data.TestClass;
import Data.TestNavstevaClass;
import Hash.*;
import Heap.Block;
import Heap.HeapFile;

import java.util.ArrayList;
import java.util.Random;

public class TesterHeap {
    private int pocetOperacii = 100;
    private int pocetData= 202;
    private HeapFile<TestClass> heapFile;
    private String heapFileName = "Data.bin";
    private String heapRiadiace = "Riadiace.bin";
    ArrayList<TestClassWithHash> data;
    Random random;
    public TesterHeap() {
        heapFile = new HeapFile<>(5000, heapFileName, heapRiadiace, new TestClass());
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
        this.insert("Luka", "Filadelfi", 1, "haha");
        this.insert("Luka", "Filadelfi", 2, "hehe");
        this.insert("Luka", "Filadelfi", 3, "hihi");
        this.insert("Luka", "Filadelfi", 4, "huhu");
        this.insert("Luka", "Filadelfi", 5, "hmhm");
        for (Block<TestClass> block : this.heapFile.getVsetkyBloky(new TestClass())) {
            System.out.println(block.toString());
        };

    }
    public void insert(String meno, String priezvisko, int id, String ecv) {
        TestClass data = new TestClass(meno, priezvisko, id, ecv, new TestNavstevaClass[]{new TestNavstevaClass("2024-11-10", 12.70, new String[]{"hahajasomt", "hihi","hahajasomt", "hihi","hahajasomt", "hihi","hahajasomt", "hihi","hahajasomt", "hihi"})});
        int cisloBlokuData = heapFile.insert(data);

    }

}
