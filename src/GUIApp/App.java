package GUIApp;

import Data.TestClass;
import Data.TestNavstevaClass;
import Hash.BlockWithHash;
import Hash.HashData.TestClassWithECVHash;
import Hash.HashData.TestClassWithIDHash;
import Heap.Block;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class App {

    private JPanel hlavny;
    private JPanel editZakaznik;
    private JPanel addZakaznik;
    private JPanel deleteParcela;
    private JPanel showVsetko;
    private JPanel menu;
    private JButton vyhladavanieButton;
    private JButton option3;
    private JButton option4;
    private JButton backVsetko;
    private JList listBlockHeap;
    private JButton submit2;
    private JButton option2;
    private JButton backCreate;
    private JButton submitCreate;
    private JTextField idCreateField;
    private JTextField ECVCreateField;
    private JTextField menoCreateField;
    private JTextField priezviskoCreateField;
    private JButton vytvorenieZakaznikaButton;
    private JButton option6;
    private JButton option7;
    private JButton option8;
    private JButton option9;
    private JButton hladatPodlaECV;
    private JButton backEdit;
    private JButton hladatPodlaId;
    private JTextField ecvField;
    private JTextField idField;

    private JList listDeleteParcela;
    private JButton back9;
    private JButton option12;
    private JButton generateButton;
    private JButton option10;
    private JButton option11;
    private JPanel generateData;
    private JButton backGenerate;
    private JTextField numberOfPeople;
    private JButton submitGenerate;
    private JButton buttonVsetko;
    private JTextField menoField;
    private JTextField priezviskoField;
    private JTextField denField;
    private JTextField mesiacField;
    private JTextField rokField;
    private JTextField cenaField;
    private JTextField praca4;
    private JTextField praca5;
    private JTextField praca3;
    private JTextField praca8;
    private JTextField praca7;
    private JTextField praca6;
    private JTextField praca9;
    private JTextField praca10;
    private JTextField praca2;
    private JTextField praca1;
    private JList listOfZaznam;
    private JButton submitZmeny;
    private JButton submitNavstevaZmeny;
    private JTextField poradoveCisloField;
    private JButton aktualizovatNavstevuButton;
    private JButton vymazatNavstevuButton;
    private JList listBlockHashID;
    private JList listBlockHashECV;
    private CardLayout cl;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });



    }

    public App() {
        JFrame frame = new JFrame("Databázová aplikácia.");
        frame.setContentPane(this.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        cl = (CardLayout)hlavny.getLayout();

        Manager manager = new Manager();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
                                    public void windowClosing(java.awt.event.WindowEvent e) {
                                        manager.endApp();
                                    }
                                }
        );

        backVsetko.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "menu");
            }
        });
        backCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "menu");
            }
        });
        backEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "menu");
            }
        });
        back9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { cl.show(hlavny, "menu");
            }
        });
        backGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "menu");
            }
        });

        vyhladavanieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "editZakaznik");
            }
        });
        vytvorenieZakaznikaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "addZakaznik" );
            }
        });
        buttonVsetko.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "showVsetko");
                ArrayList<Block<TestClass>> heapBlocks = manager.getHeapBlocks();
                ArrayList<BlockWithHash<TestClassWithIDHash>> idBlocks = manager.getIDBlocks();
                ArrayList<BlockWithHash<TestClassWithECVHash>> ecvBlocks = manager.getECVBlocks();
                DefaultListModel<Block<TestClass>> listModel = new DefaultListModel<>();
                for (Block<TestClass> object : heapBlocks) {
                    listModel.addElement(object);
                }
                listBlockHeap.setModel(listModel);
                DefaultListModel<BlockWithHash<TestClassWithIDHash>> listModelHashID = new DefaultListModel<>();
                for (BlockWithHash<TestClassWithIDHash> object : idBlocks) {
                    listModelHashID.addElement(object);
                }
                listBlockHashID.setModel(listModelHashID);
                DefaultListModel<BlockWithHash<TestClassWithECVHash>> listModelHashECV = new DefaultListModel<>();
                for (BlockWithHash<TestClassWithECVHash> object : ecvBlocks) {
                    listModelHashECV.addElement(object);
                }
                listBlockHashECV.setModel(listModelHashECV);
            }
        });

        option12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.deleteData();
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(hlavny, "generateData");
            }
        });

        submitCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.insert(menoCreateField.getText(), priezviskoCreateField.getText(), Integer.parseInt(idCreateField.getText()), ECVCreateField.getText());
                cl.show(hlavny, "menu");
            }
        });
        hladatPodlaECV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestClass auto = manager.findUsingECV(ecvField.getText());
                DefaultListModel<TestNavstevaClass> listModel = new DefaultListModel<>();
                for (TestNavstevaClass object : auto.getNavvstevy()) {
                    listModel.addElement(object);
                }
                listOfZaznam.setModel(listModel);
                menoField.setText(auto.getMeno());
                priezviskoField.setText(auto.getPriezvisko());
                idField.setText(String.valueOf(auto.getId()));
            }
        });

        listOfZaznam.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!listOfZaznam.isSelectionEmpty()) {
                    TestNavstevaClass data = (TestNavstevaClass)listOfZaznam.getSelectedValue();
                    LocalDate date = data.getDatum();
                    denField.setText(String.valueOf(date.getDayOfMonth()));
                    mesiacField.setText(String.valueOf(date.getMonthValue()));
                    rokField.setText(String.valueOf(date.getYear()));
                    cenaField.setText(String.valueOf(data.getCena()));
                    praca1.setText(data.getPopisy()[0]);
                    praca2.setText(data.getPopisy()[1]);
                    praca3.setText(data.getPopisy()[2]);
                    praca4.setText(data.getPopisy()[3]);
                    praca5.setText(data.getPopisy()[4]);
                    praca6.setText(data.getPopisy()[5]);
                    praca7.setText(data.getPopisy()[6]);
                    praca8.setText(data.getPopisy()[7]);
                    praca9.setText(data.getPopisy()[8]);
                    praca10.setText(data.getPopisy()[9]);
                    poradoveCisloField.setText(String.valueOf(listOfZaznam.getSelectedIndex()));
                }

            }
        });

        hladatPodlaId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestClass auto = manager.findUsingID(Integer.parseInt(idField.getText()));
                DefaultListModel<TestNavstevaClass> listModel = new DefaultListModel<>();
                for (TestNavstevaClass object : auto.getNavvstevy()) {
                    listModel.addElement(object);
                }
                listOfZaznam.setModel(listModel);
                menoField.setText(auto.getMeno());
                priezviskoField.setText(auto.getPriezvisko());
                ecvField.setText(auto.getECV());
            }
        });


        submitGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                manager.insertData(Integer.parseInt(numberOfPeople.getText()));
                cl.show(hlavny, "menu");
            }
        });

        submitZmeny.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.edit(Integer.parseInt(idField.getText()), menoField.getText(), priezviskoField.getText());

            }
        });
        submitNavstevaZmeny.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Vytvorenie navsetvy");
                manager.vlozNavtevu(Integer.parseInt(idField.getText().toString()),
                        denField.getText(),
                        mesiacField.getText(),
                        rokField.getText(),
                        Double.parseDouble(cenaField.getText()),
                        praca1.getText(),
                        praca2.getText(),
                        praca3.getText(),
                        praca4.getText(),
                        praca5.getText(),
                        praca6.getText(),
                        praca7.getText(),
                        praca8.getText(),
                        praca9.getText(),
                        praca10.getText()
                );
            }
        });
        vymazatNavstevuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Vymazanie stlacene");
                manager.vymazNavtevu(Integer.parseInt(poradoveCisloField.getText()), Integer.parseInt(idField.getText().toString()));
            }
        });
        aktualizovatNavstevuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Editovanie navstevy");
                manager.editNavtevu(Integer.parseInt(poradoveCisloField.getText()), Integer.parseInt(idField.getText().toString()),
                        denField.getText(),
                        mesiacField.getText(),
                        rokField.getText(),
                        Double.parseDouble(cenaField.getText()),
                        praca1.getText(),
                        praca2.getText(),
                        praca3.getText(),
                        praca4.getText(),
                        praca5.getText(),
                        praca6.getText(),
                        praca7.getText(),
                        praca8.getText(),
                        praca9.getText(),
                        praca10.getText()
                );
            }
        });
    }

    public JPanel getPanel(){
        return hlavny;
    }


}
