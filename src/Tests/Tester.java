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
    private int idGenerator;
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

    private static final char[] CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int BASE = CHARACTERS.length;
    private static int counter = 0;
    public Tester() {
        heapFile = new HeapFile<>(5000, heapFileName, heapRiadiace, new TestClass());
        ecvHash = new ExtendibleHash<>(100, ecvFileName, ecvRiadiace, new TestClassWithECVHash());
        idHash = new ExtendibleHash<>(400, idFileName, idRiadiace, new TestClassWithIDHash());
        this.data = new ArrayList<>();
        random = new Random();
        idGenerator = 1;

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
        if (ecvHash.get(new TestClassWithECVHash(0, ecv, 0)) == null) {
            TestClass data = new TestClass(meno, priezvisko, id, ecv, new TestNavstevaClass[]{});
            int cisloBlokuData = heapFile.insert(data);
            TestClassWithECVHash ecvData = new TestClassWithECVHash(cisloBlokuData, ecv, id);
            ecvHash.insert(ecvData);
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
    public void insertData(int pocetData) {
        for (int i = 0; i< pocetData; i++) {
            String ecv = getNextUniqueString();
            if (ecv.equals("AASY") || ecv.equals("AAAQ")) { //AABV, AACY, AAEV
                System.out.println("tu");
            }
            this.insert("", "", idGenerator, ecv);
            this.idGenerator++;
        }
        this.ecvHash.getVsetkyBloky(new TestClassWithECVHash());
        this.ecvHash.get(new TestClassWithECVHash(0, "AAAQ", 0));
        this.ecvHash.get(new TestClassWithECVHash(0, "AASY", 0));
    }

    public ArrayList<Block<TestClass>> getHeapBlocks() {
        return this.heapFile.getVsetkyBloky(new TestClass());
    }

    public ArrayList<BlockWithHash<TestClassWithIDHash>> getIDBlocks() {
        return this.idHash.getVsetkyBloky(new TestClassWithIDHash());
    }

    public ArrayList<BlockWithHash<TestClassWithECVHash>> getECVBlocks() {
        return this.ecvHash.getVsetkyBloky(new TestClassWithECVHash());
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
