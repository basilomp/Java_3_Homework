package reflectionTesting;

public class MainClass {
    public static void main(String[] args) {
        TestData testData = new TestData();
        Tester.start(testData.getClass());
    }

}
