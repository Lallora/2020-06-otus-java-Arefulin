package ru.otus;

import ru.otus.arrays.DIYArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        try {
            // Создаём набор исходных DIYArrayList
            DIYArrayList<Integer> firstDIYArrayList = new DIYArrayList<>();
            DIYArrayList<Integer> secondDIYArrayList = new DIYArrayList<>();
            DIYArrayList<String> thirdDIYArrayList = new DIYArrayList<>();

            for (int i = 1; i <= 21; i++) {
                firstDIYArrayList.add(i);
            }
            for (int j = 21; j > 0; j--) {
                secondDIYArrayList.add(j);
            }
            Collections.addAll(thirdDIYArrayList, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

            // Выводим все коллекции в исходных состояниях
            System.out.println("Initial state:");
            System.out.println("First:  " + firstDIYArrayList);
            System.out.println("Second: " + secondDIYArrayList + ", size:" + secondDIYArrayList.size() + ", capacity:" + secondDIYArrayList.capacity());
            System.out.println("Third:  " + thirdDIYArrayList);

            Collections.copy(secondDIYArrayList, firstDIYArrayList);
            Collections.sort(thirdDIYArrayList, Collections.reverseOrder());

            // Выводим все коллекции в финальных состояниях
            System.out.println("Final state:");
            System.out.println("The iterator values of list are:");
            System.out.println("First:  " + firstDIYArrayList);
            System.out.println("Copied from First to Second (The iterator values of list are):");
            System.out.println("Second: " + secondDIYArrayList + ", size:" + secondDIYArrayList.size() + ", capacity:" + secondDIYArrayList.capacity());
            System.out.println("Sorted Third in reverse order (The iterator values of list are):");
            System.out.println("Third:  " + thirdDIYArrayList);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}