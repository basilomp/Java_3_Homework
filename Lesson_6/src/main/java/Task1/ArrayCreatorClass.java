package Task1;

import java.util.Arrays;

public class ArrayCreatorClass implements ArrayCreator{
    @Override
    public int[] createdArray(int[] initialArray) {

        int[] result;
        for (int i = initialArray.length - 1; i >= 0; i--) {
            if (initialArray[initialArray.length - 1] == 4) {
                throw new RuntimeException("Nothing to display. 4 is the last number in the array");
            } else if (initialArray[i] == 4) {
                int length = initialArray.length - i - 1;
                result = new int[length];
                for (int j = i + 1, k = 0; j < initialArray.length; j++, k++) {
                    result[k] = initialArray[j];
                }
                System.out.println(Arrays.toString(result));
                return result;

            }
        }
        throw new RuntimeException("Invalid array.");
    }
}


