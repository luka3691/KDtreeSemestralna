package GUIApp;

import Data.TestClass;
import Data.TestNavstevaClass;

import Hash.*;
import Heap.Block;
import Heap.HeapFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;



public class Manager {
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
    private static final char[] CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int BASE = CHAR_POOL.length;
    private static final char[] CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static int counter = 0;
    public Manager() {
        this.intialize();
    }

    private void intialize() {
        heapFile = new HeapFile<>(5000, heapFileName, heapRiadiace, new TestClass());
        ecvHash = new ExtendibleHash<>(400, ecvFileName, ecvRiadiace, new TestClassWithECVHash());
        idHash = new ExtendibleHash<>(400, idFileName, idRiadiace, new TestClassWithIDHash());
        idGenerator = 1;
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

    public void vlozNavtevu(int id, String den, String mesiac, String rok, double cena, String praca1, String praca2, String praca3, String praca4, String praca5, String praca6, String praca7, String praca8, String praca9, String praca10) {
        String[] popisy = new String[]{praca1,
                praca2,
                praca3,
                praca4,
                praca5,
                praca6,
                praca7,
                praca8,
                praca9,
                praca10};
        String tempMesiac = mesiac;
        if (Integer.parseInt(tempMesiac)<10) {
            tempMesiac = "0" + tempMesiac;
        }
        String tempDen = den;
        if (Integer.parseInt(tempDen)<10) {
            tempDen = "0" + tempDen;
        }
        TestNavstevaClass navsteva = new TestNavstevaClass(rok + "-" + tempMesiac + "-" + tempDen, cena, popisy);
        TestClass existing = this.findUsingID(id);
        existing.vlozNavstevu(navsteva);
        TestClassWithIDHash idData = idHash.get(new TestClassWithIDHash(0, id));
        heapFile.edit(idData.getAdresa(), existing, existing);
    }

    public void editNavtevu(int poradoveCislo, int id, String den, String mesiac, String rok, double cena, String praca1, String praca2, String praca3, String praca4, String praca5, String praca6, String praca7, String praca8, String praca9, String praca10) {
        String[] popisy = new String[]{praca1,
                praca2,
                praca3,
                praca4,
                praca5,
                praca6,
                praca7,
                praca8,
                praca9,
                praca10};
        String tempMesiac = mesiac;
        if (Integer.parseInt(tempMesiac)<10) {
            tempMesiac = "0" + tempMesiac;
        }
        String tempDen = den;
        if (Integer.parseInt(tempDen)<10) {
            tempDen = "0" + tempDen;
        }
        TestNavstevaClass navsteva = new TestNavstevaClass(rok + "-" + tempMesiac + "-" + tempDen, cena, popisy);
        TestClass existing = this.findUsingID(id);
        existing.getNavvstevy()[poradoveCislo] = navsteva;
        TestClassWithIDHash idData = idHash.get(new TestClassWithIDHash(0, id));
        heapFile.edit(idData.getAdresa(), existing, existing);
    }

    public void vymazNavtevu(int poradoveCislo, int id) {
        TestClassWithIDHash idData = idHash.get(new TestClassWithIDHash(0, id));
        TestClass data = new TestClass();
        data.setId(id);
        TestClass existing = heapFile.get(idData.getAdresa(), data);
        existing.getNavvstevy()[poradoveCislo] = new TestNavstevaClass();
        heapFile.edit(idData.getAdresa(), existing, existing);
    }

    public void edit(int id, String meno, String priezvisko) {
        TestClassWithIDHash idData = idHash.get(new TestClassWithIDHash(0, id));
        TestClass data = new TestClass();
        data.setId(id);
        TestClass existing = heapFile.get(idData.getAdresa(), data);
        existing.setMeno(meno);
        existing.setPriezvisko(priezvisko);
        heapFile.edit(idData.getAdresa(), existing, existing);
    }
    private static String getNextString(String current) {
        if (current.isEmpty()) {
            return String.valueOf(CHAR_POOL[0]);
        }

        StringBuilder result = new StringBuilder(current);
        int index = result.length() - 1;

        while (index >= 0) {
            int charIndex = findCharIndex(result.charAt(index));

            if (charIndex < BASE - 1) {
                result.setCharAt(index, CHAR_POOL[charIndex + 1]);
                return result.toString();
            } else {
                result.setCharAt(index, CHAR_POOL[0]);
                index--;
            }
        }

        result.insert(0, CHAR_POOL[0]);
        return result.toString();
    }

    private static int findCharIndex(char c) {
        for (int i = 0; i < BASE; i++) {
            if (CHAR_POOL[i] == c) {
                return i;
            }
        }
        throw new IllegalArgumentException("Nansiel sa znak v poole: " + c);
    }

    public void insertData(int pocetData) {
        for (int i = 0; i< pocetData; i++) {
            String ecv = getNextUniqueString();
            this.insert("", "", idGenerator, ecv);
            this.idGenerator++;
        }
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

    public void endApp() {
        this.heapFile.close();
        this.ecvHash.close();
        this.idHash.close();
    }

    public void deleteData() {
        this.endApp();
        File file1 = new File(heapFileName);
        if (file1.delete()) {
            System.out.println("Deleted the file: " + file1.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        File file2 = new File(heapRiadiace);
        if (file2.delete()) {
            System.out.println("Deleted the file: " + file2.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        File file3 = new File(ecvFileName);
        if (file3.delete()) {
            System.out.println("Deleted the file: " + file3.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        File file4 = new File(ecvRiadiace);
        if (file4.delete()) {
            System.out.println("Deleted the file: " + file4.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        File file5 = new File(idFileName);
        if (file5.delete()) {
            System.out.println("Deleted the file: " + file5.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        File file6 = new File(idRiadiace);
        if (file6.delete()) {
            System.out.println("Deleted the file: " + file6.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        this.intialize();
    }
}
