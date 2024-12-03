import Hash.ExtendibleHash;
import Hash.TestClassWithHash;
import Tests.Measuring;
import Tests.MeasuringWithHash;
import Tests.Tester;
import Tests.TesterHeap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        MeasuringWithHash measuring = new MeasuringWithHash();
        //testovanie heap file
        //TesterHeap tester = new TesterHeap();
        //tester.insert();

        //Measuring measuring = new Measuring();

        measuring.insert();
        measuring.test();




        /*
        Tester tester = new Tester();
        tester.insert();

    }
}