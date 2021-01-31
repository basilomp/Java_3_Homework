package Task2;

import java.util.Arrays;

public class CheckApp implements CheckArray {
    public static void main(String[] args) {

    }

    @Override
    public boolean isValid(int[] array) {
        boolean flag = false;
        if (Arrays.stream(array).anyMatch(x -> x == 1) && Arrays.stream(array).anyMatch(x -> x == 4)) {
            for (int i = 0; i < array.length; i++) {
                if ((array[i] == 1 || array[i] == 4)) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        }

        System.out.println(flag);
        return flag;





    }
}