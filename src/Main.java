import Tests.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //MeasuringWithHashString measuring = new MeasuringWithHashString();

        Measuring measuring = new Measuring();
        //MeasuringWithHash measuring = new MeasuringWithHash();
        measuring.insert();
        measuring.test();




        /*
        Tester tester = new Tester();
        tester.insert();
*/
    }
}