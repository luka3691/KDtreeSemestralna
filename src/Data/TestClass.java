package Data;

import Hash.IDataWithHash;

import java.io.*;
import java.util.Arrays;
import java.util.BitSet;

public class TestClass implements IData<TestClass> {
    private String meno;
    private String priezvisko;
    private int menoValidCharacters;
    private int priezviskoValidCharacters;
    private String ECV;
    private int id;
    private static int MENO_MAX = 20;
    private static int PRIEZVISKO_MAX = 30;
    private static int POPIS_MAX = 20;
    private int ECVvalidCharacters;
    private static int ECV_MAX = 10;
    private TestNavstevaClass[] navvstevy;

    public TestClass() {
        this.meno = "";
        this.priezvisko = "";
        this.ECV = "";
        this.id = 0;
        menoValidCharacters = 0;
        priezviskoValidCharacters = 0;
        ECVvalidCharacters = 0;
        this.navvstevy = new TestNavstevaClass[]{new TestNavstevaClass(), new TestNavstevaClass(), new TestNavstevaClass(), new TestNavstevaClass(), new TestNavstevaClass()};
    }
    public TestClass(String meno, String priezvisko, int id, String ECV, TestNavstevaClass[] navstevy) {
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.id = id;
        this.ECV = ECV;
        menoValidCharacters = meno.length();
        priezviskoValidCharacters = priezvisko.length();
        ECVvalidCharacters = ECV.length();
        if (navstevy.length < 5) {
            TestNavstevaClass[] temp = new TestNavstevaClass[5];
            for (int i = 0; i < navstevy.length; i++) {
                temp[i] = navstevy[i]; // Copy existing elements
            }
            for (int i = navstevy.length; i < 5; i++) {
                temp[i] = new TestNavstevaClass(); // Fill remaining slots with empty strings
            }
            this.navvstevy = temp;
        } else {
            this.navvstevy = navstevy;
        }
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
            hlpOutStream.writeChars(normalizeString(ECV, ECV_MAX,'0'));
            hlpOutStream.writeInt(ECVvalidCharacters);


            for (int i = 0; i< navvstevy.length; i++) {
                hlpByteArrayOutputStream.write(navvstevy[i].toByteArray());
            }



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
            for (int i = 0; i < ECV_MAX; i++) {
                this.ECV += hlpOutStream.readChar();
            }
            this.ECVvalidCharacters = hlpOutStream.readInt();
            this.ECV = this.ECV.substring(0, this.ECVvalidCharacters);

            for (int i = 0; i < this.navvstevy.length; i++) {
                int year = hlpOutStream.readInt();
                int month = hlpOutStream.readInt();
                int day = hlpOutStream.readInt();
                String date = String.valueOf(year);
                if (month < 10) {
                    date = date + "-0" + String.valueOf(month);
                } else {
                    date = date + "-" + String.valueOf(month);
                }
                if (day < 10) {
                    date = date + "-0" + String.valueOf(day);
                } else {
                    date = date + "-" + String.valueOf(day);
                }


                String[] popisy = new String[10];
                for (int m = 0; m < 10; m++) {
                    String popis = "";
                    for (int n = 0; n < POPIS_MAX; n++) {
                        popis += hlpOutStream.readChar();
                    }
                    popisy[m] = popis;
                }
                double cena = hlpOutStream.readDouble();
                this.navvstevy[i] = new TestNavstevaClass(date, cena, popisy);
            }



        } catch (IOException ex) {

        }
    }

    @Override
    public int getSize() {
        return Character.BYTES * (MENO_MAX + PRIEZVISKO_MAX + ECV_MAX) + Integer.BYTES * 4 + this.navvstevy[0].getSize() * 5;
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

    public String getMeno() {
        return meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public String getECV() {
        return ECV;
    }

    public TestNavstevaClass[] getNavvstevy() {
        return navvstevy;
    }

    public void setECV(String ECV) {
        this.ECV = ECV;
        ECVvalidCharacters = ECV.length();
    }

    public void setMeno(String meno) {
        this.meno = meno;
        menoValidCharacters = meno.length();
    }

    public void setPriezvisko(String priezvisko) {

        this.priezvisko = priezvisko;
        priezviskoValidCharacters = priezvisko.length();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNavvstevy(TestNavstevaClass[] navvstevy) {
        this.navvstevy = navvstevy;
    }

    public void vlozNavstevu(TestNavstevaClass navsteva) {
        for (int i = 0; i < navvstevy.length; i++) {
            if (this.navvstevy[i].isDummy()) {
                this.navvstevy[i] = navsteva;
            }
        }
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "meno='" + meno + '\'' +
                ", priezvisko='" + priezvisko + '\'' +
                ", menoValidCharacters=" + menoValidCharacters +
                ", priezviskoValidCharacters=" + priezviskoValidCharacters +
                ", ECV='" + ECV + '\'' +
                ", id=" + id +
                ", ECVvalidCharacters=" + ECVvalidCharacters +
                ", navvstevy=" + Arrays.toString(navvstevy) +
                '}';
    }
}
