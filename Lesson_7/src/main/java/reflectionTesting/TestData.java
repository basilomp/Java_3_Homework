package reflectionTesting;

public class TestData {

    @AfterSuite
    public void mPost() {
        System.out.println("Finishing testing. The AfterSuite annotation works");
    }

    @Test(prio = 3)
    public void test1() {
        System.out.println("Priority 3 test");
    }
    @Test(prio = 2)
    public void test2() {
        System.out.println("Priority 2 test");
    }

    @Test(prio = 9)
    public void test3() {
        System.out.println("Priority 9 test");
    }

    @Test(prio = 4)
    public void test4() {
        System.out.println("Priority 4 test");
    }

    @Test(prio = 8)
    public void test5() {
        System.out.println("Priority 8 test");
    }

    @Test(prio = 5)
    public void test6() {
        System.out.println("Priority 5 test");
    }

    @Test(prio = 7)
    public void test7() {
        System.out.println("Priority 7 test");
    }

    @Test(prio = 6)
    public void test8() {
        System.out.println("Priority 6 test");
    }

    @Test
    public void test9() {
        System.out.println("Priority 1 test");
    }



    @BeforeSuite
    public void mPre() {
        System.out.println("Initiating testing. The Before annotation works");
    }
}
