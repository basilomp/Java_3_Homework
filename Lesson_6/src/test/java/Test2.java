import Task2.CheckApp;
import org.junit.Before;
import org.junit.Test;

public class Test2 {
    CheckApp checkApp;

    @Before
    public void prepareApp() {
        checkApp = new CheckApp();
    }

    @Test
    public void Test1() {
        System.out.println("Test_1: ");
        checkApp.isValid(new int[]{ 1, 1, 1, 4, 4, 1, 4, 4 });
        System.out.println("______");
    }
    @Test
    public void Test2() {
        System.out.println("Test_2: ");
        checkApp.isValid(new int[]{ 1, 1, 4, 1, 3, 1});
        System.out.println("______");
    }
    @Test
    public void Test3() {
        System.out.println("Test_3: ");
        checkApp.isValid(new int[]{4, 4, 4, 4});
        System.out.println("______");
    }

    @Test
    public void Test4() {
        System.out.println("Test_4: ");
        checkApp.isValid(new int[]{1, 4, 4, 1, 1, 4, 3 });
        System.out.println("______");
    }

}
