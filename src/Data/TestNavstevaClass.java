package Data;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;

public class TestNavstevaClass {
    private LocalDate datum;
    private double cena;
    private static int POPIS_MAX = 20;
    private String[] popisy;
    private int[] validChar;
    public TestNavstevaClass(String datum, double cena, String[] popisy) {
        this.datum = LocalDate.parse(datum);
        this.cena = cena;
        this.validChar = new int[]{0,0,0,0,0,0,0,0,0,0};
        if (popisy.length < 10) {
            String[] temp = new String[10];
            for (int i = 0; i < popisy.length; i++) {
                temp[i] = popisy[i]; // prekopirovanie
                validChar[i] = popisy[i].length();
            }
            for (int i = popisy.length; i < 10; i++) {
                temp[i] = " "; // vyplnenie prazdnim

            }
            this.popisy = temp;
        } else {
            this.popisy = popisy;
            for (int i = 0; i < popisy.length; i++) {
                validChar[i] = popisy[i].length();
            }
        }

    }
    public TestNavstevaClass() {
        this.datum = LocalDate.parse("1970-12-03");
        this.cena = 0.0;
        this.popisy = new String[] {" ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    }
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        try {
            hlpOutStream.writeInt(datum.getYear());
            hlpOutStream.writeInt(datum.getMonthValue());
            hlpOutStream.writeInt(datum.getDayOfMonth());

            for (int i = 0; i < this.popisy.length; i++) {
                hlpOutStream.writeChars(normalizeString(this.popisy[i], POPIS_MAX,' '));
            }
            hlpOutStream.writeDouble(cena);
            return hlpByteArrayOutputStream.toByteArray();
        } catch (IOException ex) {

        }
        return new byte[0];
    }

    public void insertZaznamy(String[] popisy) {
        this.popisy = popisy;
        for (int i = 0; i < popisy.length; i++) {
            validChar[i] = popisy[i].length();
        }
    }


    public int getSize() {
        return Integer.BYTES * 3 + 10 * Character.BYTES * POPIS_MAX + Double.BYTES; // + navstevy.getSize()
    }

    public static String normalizeString(String input, int fixedLength, char paddingChar) {
        if (input == null) {
            input = "";
        }
        // orezanie
        if (input.length() > fixedLength) {
            return input.substring(0, fixedLength);
        }
        // dopnenie
        StringBuilder normalized = new StringBuilder(input);
        while (normalized.length() < fixedLength) {
            normalized.append(paddingChar);
        }
        return normalized.toString();
    }

    public LocalDate getDatum() {
        return datum;
    }

    public double getCena() {
        return cena;
    }

    public String[] getPopisy() {
        return popisy;
    }
    public boolean isDummy() {
        return this.datum.toString().equals("1970-12-03");
    }

    @Override
    public String toString() {
        if (this.isDummy()) {
            return "";
        }
        return "(" +
                "datum:" + datum +
                "| cena:" + cena +
                "| popisy:" + Arrays.toString(popisy) +
                ")";
    }
}
