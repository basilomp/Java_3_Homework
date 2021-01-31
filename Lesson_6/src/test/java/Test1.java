import Task1.ArrayCreatorClass;
import org.junit.Before;

public class Test1 {
    ArrayCreatorClass arr;

    @Before
    public void prepareClass(){
        arr = new ArrayCreatorClass();
    }

    @org.junit.Test
    public void ArrayTest1(){
        arr.createdArray(new int[]{1, 2, 3, 4, 56});

    }

    @org.junit.Test
    public void ArrayTest2(){
        arr.createdArray(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7});

    }

    @org.junit.Test
    public void ArrayTest3(){
        arr.createdArray(new int[]{4, 6, 16, 50, 68});

    }
    @org.junit.Test
    public void ArrayTest4(){
        arr.createdArray(new int[]{12, 15, 53, 84, 4});

    }   @org.junit.Test
    public void ArrayTest5(){
        arr.createdArray(new int[]{12, 15, 53, 84, 10000});


    }
}