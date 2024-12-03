package Tests;

import Data.TestClass;
import Data.TestNavstevaClass;
import Hash.*;
import Heap.Block;
import Heap.HeapFile;

import java.util.ArrayList;
import java.util.Random;

public class Tester {
    private int pocetOperacii = 100;
    private int pocetData= 202;
    private HeapFile<TestClass> heapFile;
    private ExtendibleHash<TestClassWithECVHash> ecvHash;
    private ExtendibleHash<TestClassWithIDHash> idHash;
    private String heapFileName = "Data.bin";
    private String heapRiadiace = "Riadiace.bin";
    private String ecvFileName = "EcvHash.bin";
    private String ecvRiadiace = "EcvRiadiace.bin";
    private String idFileName = "IdHash.bin";
    private String idRiadiace = "IdRiadiace.bin";
    ArrayList<TestClassWithHash> data;
    Random random;
    public Tester() {
        heapFile = new HeapFile<>(400, heapFileName, heapRiadiace, new TestClass());
        ecvHash = new ExtendibleHash<>(400, ecvFileName, ecvRiadiace, new TestClassWithECVHash());
        idHash = new ExtendibleHash<>(400, idFileName, idRiadiace, new TestClassWithIDHash());
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
        System.out.println(this.findUsingECV("hehe").toString());
        System.out.println(this.findUsingECV("hihi").toString());
        System.out.println(this.findUsingECV("haha").toString());
        System.out.println(this.findUsingID(1).toString());
        System.out.println(this.findUsingID(2).toString());
        System.out.println(this.findUsingID(3).toString());
        for (BlockWithHash<TestClassWithECVHash> block : this.ecvHash.getVsetkyBloky(new TestClassWithECVHash())) {
            System.out.println(block.toString());
        };
        for (Block<TestClass> block : this.heapFile.getVsetkyBloky(new TestClass())) {
            System.out.println(block.toString());
        };

    }
    public void insert(String meno, String priezvisko, int id, String ecv) {
        if (ecvHash.get(new TestClassWithECVHash(0, ecv, 0)) == null && idHash.get(new TestClassWithIDHash(0, id)) == null) {
            TestClass data = new TestClass(meno, priezvisko, id, ecv, new TestNavstevaClass[]{});
            int cisloBlokuData = heapFile.insert(data);
            TestClassWithECVHash ecvData = new TestClassWithECVHash(cisloBlokuData, ecv, id);
            TestClassWithIDHash idData = new TestClassWithIDHash(cisloBlokuData, id);
            ecvHash.insert(ecvData);
            idHash.insert(idData);
        }
    }
    public TestClass findUsingID(int idcko) {
        TestClassWithIDHash idData = idHash.get(new TestClassWithIDHash(0, idcko));
        TestClass data = new TestClass();
        data.setId(idcko);
        return heapFile.get(idData.getAdresa(), data);
    }

    public TestClass findUsingECV(String ecv) {
        TestClassWithECVHash idData = ecvHash.get(new TestClassWithECVHash(0, ecv, 0));
        TestClass data = new TestClass();
        data.setECV(ecv);
        data.setId(idData.getIdcko());
        return heapFile.get(idData.getAdresa(), data);
    }
}
