package ru.otus;

import ru.otus.arrays.DIYArrayList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        System.out.println(TextColor.blue(Borders.hwStart()));
        try {
            // Создаём набор исходных DIYArrayList
            DIYArrayList<Integer> firstDIYArrayList = new DIYArrayList<>(0);
            DIYArrayList<Integer> secondDIYArrayList = new DIYArrayList<>(19);
            DIYArrayList<String> thirdDIYArrayList = new DIYArrayList<>();

            // Создаём набор исходных ArrayList (для сравнения поведения коллекций)
            ArrayList<Integer> firstArrayList = new ArrayList<>();
            ArrayList<Integer> secondArrayList = new ArrayList<>(24);
            ArrayList<String> thirdArrayList = new ArrayList<>(0);

            // Насыщаем идентичными данными первые коллекции
            for (int i = 1; i <= 20; i++) {
                firstDIYArrayList.add(i);
                firstArrayList.add(i);
            }

            // Насыщаем идентичными данными вторые коллекции
            for (int j = 25; j > 0; j--) {
                secondDIYArrayList.add(j);
                secondArrayList.add(j);
            }

            // Параллельное насыщение идентичными данными третьих коллекций
            Collections.addAll(thirdDIYArrayList, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
            Collections.addAll(thirdArrayList, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

            // Получение capacity secondArrayList путём рефлексии (ради интереса)
            // Для сравнения надо, чтобы исходные capacity DIYArrayList и ArrayList совпадали
            Field fieldElementDataAL = secondArrayList.getClass().getDeclaredField("elementData");
            fieldElementDataAL.setAccessible(true);
            Object[] secondArray = (Object[]) fieldElementDataAL.get(secondArrayList);

            // Выводим все коллекции в исходных состояниях
            System.out.println(TextColor.blue("Initial state:"));
            System.out.println("First:               " + firstDIYArrayList);
            System.out.println("FirstAL:             " + firstArrayList);
            System.out.println("Second:              " + secondDIYArrayList + ", size:" + secondDIYArrayList.size() + ", capacity:" + secondDIYArrayList.capacity());
            System.out.println("SecondAL:            " + secondArrayList + ", size:" + secondArrayList.size() + ", capacity:" + secondArray.length);
            System.out.println("Third:               " + thirdDIYArrayList);
            System.out.println("ThirdAL:             " + thirdArrayList);

            // Параллельное применение метода копирования
            Collections.copy(secondDIYArrayList, firstDIYArrayList);
            Collections.copy(secondArrayList, firstArrayList);

            // Параллельное применение метода сортировки
            Collections.sort(thirdDIYArrayList, Collections.reverseOrder());
            Collections.sort(thirdArrayList, Collections.reverseOrder());

            // Для повторного получения capacity secondArrayList
            secondArray = (Object[]) fieldElementDataAL.get(secondArrayList);

            // Выводим все коллекции в финальных состояниях
            System.out.println(TextColor.blue("\nFinal state:"));
            System.out.println(TextColor.cyan("The iterator values of list are:"));
            System.out.println("First:    " + TextColor.yellow(firstDIYArrayList.toString()));
            System.out.println("FirstAL:  " + TextColor.yellow(firstArrayList.toString()));
            System.out.println(TextColor.cyan("\nCopied from First to Second (The iterator values of list are):"));
            System.out.println("Second:   " + secondDIYArrayList + ", size:" + secondDIYArrayList.size() + ", capacity:" + secondDIYArrayList.capacity());
            System.out.println("SecondAL: " + secondArrayList + ", size:" + secondArrayList.size() + ", capacity:" + secondArray.length);
            System.out.println(TextColor.cyan("\nSorted Third in reverse order (The iterator values of list are):"));
            System.out.println("Third:    " + thirdDIYArrayList);
            System.out.println("ThirdAL:  " + thirdArrayList);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            System.out.println(TextColor.blue(Borders.hwEnd()));
        }
    }
}