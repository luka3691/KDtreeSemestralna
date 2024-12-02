package GUIApp;

import Data.TestClass;
import Data.TestNavstevaClass;

import Hash.ExtendibleHash;
import Hash.TestClassWithECVHash;
import Hash.TestClassWithHash;
import Hash.TestClassWithIDHash;
import Heap.HeapFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;



public class Manager {
    private int idGenerator;
    private String ecvGenerator;
    private HeapFile<TestClass> heapFile;
    private ExtendibleHash<TestClassWithECVHash> ecvHash;
    private ExtendibleHash<TestClassWithIDHash> idHash;
    private String heapFileName = "Data.bin";
    private String heapRiadiace = "Riadiace.bin";
    private String ecvFileName = "EcvHash.bin";
    private String ecvRiadiace = "EcvRiadiace.bin";
    private String idFileName = "IdHash.bin";
    private String idRiadiace = "IdRiadiace.bin";
    public Manager() {
        heapFile = new HeapFile<>(400, heapFileName, heapRiadiace, new TestClass());
        ecvHash = new ExtendibleHash<>(400, ecvFileName, ecvRiadiace, new TestClassWithECVHash());
        idHash = new ExtendibleHash<>(400, idFileName, idRiadiace, new TestClassWithIDHash());
        idGenerator = 1;
    }

/*

    public ArrayList<Land> find(char pismenko1, double x1, char pismenko2, double y1, char pismenko3, double x2, char pismenko4, double y2, char typeOfSearch) {
        double x1plus = x1;
        double x2plus = x2;
        double y1plus = y1;
        double y2plus = y2;
        if (pismenko1 == 'W') {
            x1plus = -x1plus;
        }
        if (pismenko2 == 'S') {
            y1plus = -y1plus;
        }
        if (pismenko3 == 'W') {
            x2plus = -x2plus;
        }
        if (pismenko4 == 'S') {
            y2plus = -y2plus;
        }

        if (typeOfSearch == 'E') {
            ArrayList<Data> everything = new ArrayList<>();
            everything.addAll(vseobecnyTree.findAllExact(new GPSsuradnice(new Point(x1plus,y1plus))));
            everything.addAll(vseobecnyTree.findAllExact(new GPSsuradnice(new Point(x2plus,y2plus))));
            ArrayList<Land> listOfLand = new ArrayList<>();
            for (Data i : everything) {
                if (i.getLand() instanceof Nehnutelnost) {
                    if (!listOfLand.contains(new Nehnutelnost(i.getLand()))) {
                        listOfLand.add(new Nehnutelnost(i.getLand()));
                    }
                } else {
                    if (!listOfLand.contains(new Parcela(i.getLand()))) {
                        listOfLand.add(new Parcela(i.getLand()));
                    }
                }
            }
            return listOfLand;
        } else if (typeOfSearch == 'N') {
            ArrayList<Data> everything = new ArrayList<>();
            everything.addAll(nehnutelnostiTree.findAllExact(new GPSsuradnice(new Point(x1plus,y1plus))));
            everything.addAll(nehnutelnostiTree.findAllExact(new GPSsuradnice(new Point(x2plus,y2plus))));
            ArrayList<Land> listOfLand = new ArrayList<>();
            for (Data i : everything) {
                if (!listOfLand.contains(new Nehnutelnost(i.getLand()))) {
                    listOfLand.add(new Nehnutelnost(i.getLand()));
                }
            }
            return listOfLand;
        } else {
            ArrayList<Data> everything = new ArrayList<>();
            everything.addAll(parcelyTree.findAllExact(new GPSsuradnice(new Point(x1plus,y1plus))));
            everything.addAll(parcelyTree.findAllExact(new GPSsuradnice(new Point(x2plus,y2plus))));
            ArrayList<Land> listOfLand = new ArrayList<>();
            for (Data i : everything) {
                if (!listOfLand.contains(new Parcela(i.getLand()))) {
                    listOfLand.add(new Parcela(i.getLand()));
                }
            }
            return listOfLand;
        }

    }
    public ArrayList<Land> find(char pismenko1, double x1, char pismenko2, double y1, char typeOfSearch) {
        double x1plus = x1;
        double y1plus = y1;
        if (pismenko1 == 'W') {
            x1plus = -x1plus;
        }
        if (pismenko2 == 'S') {
            y1plus = -y1plus;
        }


        if (typeOfSearch == 'E') {
            ArrayList<Data> everything = new ArrayList<>();
            everything.addAll(vseobecnyTree.findAllExact(new GPSsuradnice(new Point(x1plus,y1plus))));
            ArrayList<Land> listOfLand = new ArrayList<>();
            for (Data i : everything) {
                if (i.getLand() instanceof Nehnutelnost) {
                    if (!listOfLand.contains(new Nehnutelnost(i.getLand()))) {
                        listOfLand.add(new Nehnutelnost(i.getLand()));
                    }
                } else {
                    if (!listOfLand.contains(new Parcela(i.getLand()))) {
                        listOfLand.add(new Parcela(i.getLand()));
                    }
                }
            }
            return listOfLand;
        } else if (typeOfSearch == 'N') {
            ArrayList<Data> everything = new ArrayList<>();
            everything.addAll(nehnutelnostiTree.findAllExact(new GPSsuradnice(new Point(x1plus,y1plus))));
            ArrayList<Land> listOfLand = new ArrayList<>();
            for (Data i : everything) {
                if (!listOfLand.contains(new Nehnutelnost(i.getLand()))) {
                    listOfLand.add(new Nehnutelnost(i.getLand()));
                }
            }
            return listOfLand;
        } else {
            ArrayList<Data> everything = new ArrayList<>();
            everything.addAll(parcelyTree.findAllExact(new GPSsuradnice(new Point(x1plus,y1plus))));
            ArrayList<Land> listOfLand = new ArrayList<>();
            for (Data i : everything) {
                if (!listOfLand.contains(new Parcela(i.getLand()))) {
                    listOfLand.add(new Parcela(i.getLand()));
                }
            }
            return listOfLand;
        }

    }

    public void edit(char pismenko1, double x1, char pismenko2, double y1, char pismenko3, double x2, char pismenko4, double y2, int parseInt, String text, Land data, char type) {
        Data originalDataBottom;
        Data originalDataTop;
        if (type == 'N') {
            originalDataBottom = nehnutelnostiTree.findOne(new Data(data, new GPSsuradnice(data.getSuradnice().getBottomLeft())));
            originalDataTop = nehnutelnostiTree.findOne(new Data(data, new GPSsuradnice(data.getSuradnice().getTopRight())));
        } else {
            originalDataBottom = parcelyTree.findOne(new Data(data, new GPSsuradnice(data.getSuradnice().getBottomLeft())));
            originalDataTop = parcelyTree.findOne(new Data(data, new GPSsuradnice(data.getSuradnice().getTopRight())));
        }

        double x1plus = x1;
        double x2plus = x2;
        double y1plus = y1;
        double y2plus = y2;
        if (pismenko1 == 'W') {
            x1plus = -x1plus;
        }
        if (pismenko2 == 'S') {
            y1plus = -y1plus;
        }
        if (pismenko3 == 'W') {
            x2plus = -x2plus;
        }
        if (pismenko4 == 'S') {
            y2plus = -y2plus;
        }

        if (!originalDataBottom.getLand().getKey().equals(new GPSsuradnice(new Point(x1plus, y1plus), new Point(x2plus, y2plus)))) {
            Land land = originalDataBottom.getLand();
            if (originalDataBottom.getLand() instanceof Nehnutelnost) {
                this.delete(land, 'N');
                this.insert('F', x1plus,
                        'F', y1plus,
                        'F', x2plus,
                        'F', y2plus,
                        'N',parseInt, text);
            } else {
                this.delete(land, 'P');
                this.insert('F', x1plus,
                        'F', y1plus,
                        'F', x2plus,
                        'F', y2plus,
                        'P',parseInt, text);
            }
        } else {
            originalDataBottom.getLand().setPopis(text);
            originalDataBottom.getLand().setCisloSupis(parseInt);
        }


    }

    public void delete(Land data, char type) {
        if (type == 'N') {
            this.vseobecnyTree.deletePublic(new Data(new Nehnutelnost(data), new GPSsuradnice(data.getSuradnice().getTopRight())));
            this.vseobecnyTree.deletePublic(new Data(new Nehnutelnost(data), new GPSsuradnice(data.getSuradnice().getBottomLeft())));
            this.nehnutelnostiTree.deletePublic(new Data(new Nehnutelnost(data), new GPSsuradnice(data.getSuradnice().getBottomLeft())));
            this.nehnutelnostiTree.deletePublic(new Data(new Nehnutelnost(data), new GPSsuradnice(data.getSuradnice().getTopRight())));
        } else {
            this.vseobecnyTree.deletePublic(new Data(new Parcela(data), new GPSsuradnice(data.getSuradnice().getTopRight())));
            this.vseobecnyTree.deletePublic(new Data(new Parcela(data), new GPSsuradnice(data.getSuradnice().getBottomLeft())));
            this.parcelyTree.deletePublic(new Data(new Parcela(data), new GPSsuradnice(data.getSuradnice().getBottomLeft())));
            this.parcelyTree.deletePublic(new Data(new Parcela(data), new GPSsuradnice(data.getSuradnice().getTopRight())));
        }
    }



    public void insertData(int pocetParciel, int pocetNehnutelnosti, int podiel) {
        this.vseobecnyTree.clear();
        this.parcelyTree.clear();
        this.nehnutelnostiTree.clear();
        double minX = -180.0;
        double maxX = 180.0;
        double minY = -90.0;
        double maxY = 90.0;
        char pismenko1 = 'F';
        char pismenko2= 'F';
        char pismenko3= 'F';
        char pismenko4= 'F';
        int pocetObjektov = pocetNehnutelnosti + pocetParciel;
        int nehnutelnostCounter = 0;
        int parcelaCounter = 0;
        while (pocetObjektov > 0) {
            if (ThreadLocalRandom.current().nextDouble(1) < ((double)podiel / 100)) {
                double x1 = (ThreadLocalRandom.current().nextDouble() * (maxX - minX)) + minX;
                double x2 = (ThreadLocalRandom.current().nextDouble() * (maxX - x1)) + x1;
                double y1 = (ThreadLocalRandom.current().nextDouble() * (maxY - minY)) + minY;
                double y2 = (ThreadLocalRandom.current().nextDouble() * (maxY - y1))+ y1;
                this.insert(pismenko1, x1, pismenko2,  y1, pismenko3, x2, pismenko4, y2,'P',  parcelaCounter, "");
                this.insert(pismenko1, x1, pismenko2,  y1, pismenko3, x2, pismenko4, y2,'N',  nehnutelnostCounter, "");

            } else {
                if (parcelaCounter < pocetNehnutelnosti) {
                    double x1 = (ThreadLocalRandom.current().nextDouble() * (maxX - minX)) + minX;
                    double x2 = (ThreadLocalRandom.current().nextDouble() * (maxX - x1)) + x1;
                    double y1 = (ThreadLocalRandom.current().nextDouble() * (maxY - minY)) + minY;
                    double y2 = (ThreadLocalRandom.current().nextDouble() * (maxY - y1))+ y1;
                    this.insert(pismenko1, x1, pismenko2,  y1, pismenko3, x2, pismenko4, y2,'P',  parcelaCounter, "");
                }
               if (nehnutelnostCounter < pocetNehnutelnosti) {
                    double x11 = (ThreadLocalRandom.current().nextDouble() * (maxX - minX)) + minX;
                    double x22 = (ThreadLocalRandom.current().nextDouble() * (maxX - x11))+ x11;
                    double y11 = (ThreadLocalRandom.current().nextDouble() * (maxY - minY)) + minY;
                    double y22 = (ThreadLocalRandom.current().nextDouble() * (maxY - y11))+ y11;
                    this.insert(pismenko1, x11, pismenko2,  y11, pismenko3, x22, pismenko4, y22,'N',  nehnutelnostCounter, "");
                }

            }
            pocetObjektov = pocetObjektov-2;
            parcelaCounter++;
            nehnutelnostCounter++;
        }
    }

    public void loadData() {
        try {
            // Reading from a CSV file
            this.vseobecnyTree = null;
            this.parcelyTree = null;
            this.nehnutelnostiTree = null;
            KDtree[] stromy = CVSFileHandler.readCSVFile(csvFilePath, 'F');
            this.vseobecnyTree = stromy[0];
            this.parcelyTree = stromy[2];
            this.nehnutelnostiTree = stromy[1];
            /*
            this.vseobecnyTree = CVSFileHandler.readCSVFile(csvFilePath, 'F');
            this.parcelyTree = CVSFileHandler.readCSVFile(csvFilePathParcely,  'P');
            this.nehnutelnostiTree = CVSFileHandler.readCSVFile(csvFilePathNehnutelnosti, 'N');



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void saveData() {
        try {
            // Reading from a CSV file
            CVSFileHandler.writeCSVFile(csvFilePath, this.vseobecnyTree);
            CVSFileHandler.writeCSVFile(csvFilePathNehnutelnosti, this.nehnutelnostiTree);
            CVSFileHandler.writeCSVFile(csvFilePathParcely, this.parcelyTree);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<Land> findAll() {
        ArrayList<Data> everything = new ArrayList<>();
        everything.addAll(vseobecnyTree.allData());
        ArrayList<Land> listOfLand = new ArrayList<>();
        for (Data i : everything) {
            if (i.getLand() instanceof Nehnutelnost) {
                if (!listOfLand.contains(new Nehnutelnost(i.getLand()))) {
                listOfLand.add(new Nehnutelnost(i.getLand()));
            }
            } else if (i.getLand() instanceof Parcela) {
                if (!listOfLand.contains(new Parcela(i.getLand()))) {
                    listOfLand.add(new Parcela(i.getLand()));
                }
            }
        }
        return listOfLand;

    }
    private double treeRound(double value) {
        double HODNOTA = 100000.0;
        //BigDecimal result = new BigDecimal(value * HODNOTA);
        double result =    Math.round(value * HODNOTA);
        return result / HODNOTA;
    }

    public void clear() {
        this.vseobecnyTree.clear();
        this.nehnutelnostiTree.clear();
        this.parcelyTree.clear();
    }*/

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

    public void vlozNavtevu( int id, String den, String mesiac, String rok, double cena, String praca1, String praca2, String praca3, String praca4, String praca5, String praca6, String praca7, String praca8, String praca9, String praca10) {
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

    public void editNavtevu(int poradoveCislo, int id, int den, int mesiac, int rok, double cena, String praca1, String praca2, String praca3, String praca4, String praca5, String praca6, String praca7, String praca8, String praca9, String praca10) {
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
        TestNavstevaClass navsteva = new TestNavstevaClass(den + "-" + mesiac + "-" + rok, cena, popisy);
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
}
