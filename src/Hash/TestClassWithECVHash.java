package Hash;

import Data.IData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

public class TestClassWithECVHash implements IDataWithHash<TestClassWithECVHash> {
    private int adresa;
    private String ECV;
    private int ECVvalidCharacters;
    private int idcko;
    private static int ECV_MAX = 10;
    public TestClassWithECVHash(int adresa, String ECV, int idcko) {
        this.adresa = adresa;
        this.ECV = ECV;
        this.ECVvalidCharacters = ECV.length();
        this.idcko = idcko;
    }
    public TestClassWithECVHash() {
        this.adresa = 0;
        this.ECV = "";
        this.ECVvalidCharacters = 0;
        this.idcko = 0;
    }


    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        try {
            hlpOutStream.writeInt(adresa);
            hlpOutStream.writeChars(normalizeString(ECV, ECV_MAX,'0'));
            hlpOutStream.writeInt(ECVvalidCharacters);
            hlpOutStream.writeInt(idcko);
            return hlpByteArrayOutputStream.toByteArray();
        } catch (IOException ex) {

        }
        return new byte[0];
    }

    @Override
    public void fromByteArray(byte[] array) {
        ByteArrayInputStream hlpByteArrayInputStream= new ByteArrayInputStream(array);
        DataInputStream hlpOutStream = new DataInputStream(hlpByteArrayInputStream);
        try {
            this.adresa = hlpOutStream.readInt();
            for (int i = 0; i < ECV_MAX; i++) {
                this.ECV += hlpOutStream.readChar();
            }
            this.ECVvalidCharacters = hlpOutStream.readInt();
            this.ECV = this.ECV.substring(0, this.ECVvalidCharacters);
            this.idcko = hlpOutStream.readInt();
        } catch (IOException ex) {

        }
    }
    @Override
    public int getSize() {
        return Character.BYTES * (ECV_MAX) + Integer.BYTES * 3 ; // + navstevy.getSize()
    }


    @Override
    public boolean ownEquals(TestClassWithECVHash data) {
        return data.getECV().equals(this.ECV);
    }

    @Override
    public TestClassWithECVHash createClass() {
        return new TestClassWithECVHash();
    }

    @Override
    public BitSet getHash() {
        String trimmedInput = ECV.length() > 4 ? ECV.substring(0, 4) : ECV;

        // algoritmus na rovnomernejsie rozlozenie stringov
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(trimmedInput.getBytes(StandardCharsets.UTF_8));
            if (ECV.equals("AASY") || ECV.equals("AAAQ")) {
                System.out.println("Hash bytes for '" + trimmedInput + "': " + Arrays.toString(Arrays.copyOf(hashBytes, 4)));
            }
            return BitSet.valueOf(Arrays.copyOf(hashBytes, 4)); // prve 4 znaky

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public static String normalizeString(String input, int fixedLength, char paddingChar) {
        if (input == null) {
            input = "";
        }
        // Truncate if longer
        if (input.length() > fixedLength) {
            return input.substring(0, fixedLength);
        }
        // Pad if shorter
        StringBuilder normalized = new StringBuilder(input);
        while (normalized.length() < fixedLength) {
            normalized.append(paddingChar);
        }
        return normalized.toString();
    }

    public int getAdresa() {
        return adresa;
    }

    public String getECV() {
        return ECV;
    }

    public int getIdcko() {
        return idcko;
    }

    @Override
    public String toString() {
        return "[" +
                "adresa:" + adresa +
                "| ECV:'" + ECV + '\'' +
                "| PocetValidECVZnak:" + ECVvalidCharacters +
                "| ID:" + idcko +
                "]";
    }
}
