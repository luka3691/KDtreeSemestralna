package Data;

import java.io.*;

public class TestClass implements IData<TestClass> {
    private String meno;
    private String priezvisko;
    private int menoValidCharacters;
    private int priezviskoValidCharacters;
    private int id;
    private static int MENO_MAX = 20;
    private static int PRIEZVISKO_MAX = 30;
    private TestNavstevaClass[] navvstevy;

    public TestClass() {
        this.meno = "";
        this.priezvisko = "";
        this.id = 0;
        menoValidCharacters = 0;
        priezviskoValidCharacters = 0;
        //this.navvstevy = new Data.TestNavstevaClass[5];
    }
    public TestClass(String meno, String priezvisko, int id) {
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.id = id;
        menoValidCharacters = meno.length();
        priezviskoValidCharacters = priezvisko.length();
        //this.navvstevy = new Data.TestNavstevaClass[5];
    }
    @Override
    public boolean ownEquals(TestClass data) {
        return data.getId() == this.id;
    }

    @Override
    public TestClass createClass() {
        return new TestClass();
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        try {
            hlpOutStream.writeChars(normalizeString(meno, MENO_MAX,'0'));
            hlpOutStream.writeInt(menoValidCharacters);
            hlpOutStream.writeChars(normalizeString(priezvisko, PRIEZVISKO_MAX,'0'));
            hlpOutStream.writeInt(priezviskoValidCharacters);
            hlpOutStream.writeInt(id);
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
            this.meno = "";
            for (int i = 0; i < MENO_MAX; i++) {
                this.meno += hlpOutStream.readChar();
            }
            this.menoValidCharacters = hlpOutStream.readInt();
            this.meno = this.meno.substring(0, this.menoValidCharacters);
            this.priezvisko = "";
            for (int i = 0; i < PRIEZVISKO_MAX; i++) {
                this.priezvisko += hlpOutStream.readChar();
            }
            this.priezviskoValidCharacters = hlpOutStream.readInt();
            this.priezvisko = this.priezvisko.substring(0, this.priezviskoValidCharacters);
            this.id = hlpOutStream.readInt();
        } catch (IOException ex) {

        }
    }

    @Override
    public int getSize() {
        return Character.BYTES * (MENO_MAX + PRIEZVISKO_MAX) + Integer.BYTES * 3 ; // + navstevy.getSize()
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

    public int getId() {
        return id;
    }
}
