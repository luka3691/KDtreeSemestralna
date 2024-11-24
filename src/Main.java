//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //zapisujem pole dat
        TestClass test = new TestClass("Luka", "Filadelfi", 4);
        TestClass test2 = new TestClass("Koko", "Loko", 5);
        HeapFile<TestClass> testHeap = new HeapFile<>(400, "Data.bin", "Riadiace.bin", test);
        System.out.println(testHeap.insert(test));
        System.out.println(testHeap.insert(test2));
        //System.out.println(testHeap.get(1, test2));
        testHeap.close();
    }
}