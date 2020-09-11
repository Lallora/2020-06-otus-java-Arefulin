package ru.otus.testobjects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestObject {
    private String name;
    private String surname;
    private int age;
    private boolean sex; // true - male, false - female
    private List<String> listOfSongs;
    private Object[] arrPerformancesObject;

    public TestObject(String name, String surname, int age, boolean sex, List<String> listOfSongs, Object[] arrPerformancesObject) {
        if (name == null) this.name = "";
        else this.name = name;
        if (surname == null) this.surname = "";
        else this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.listOfSongs = listOfSongs;
        this.arrPerformancesObject = arrPerformancesObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject testObject = (TestObject) o;

        return name.equals(testObject.name)
                && surname.equals(testObject.surname)
                && age == testObject.age
                && sex == testObject.sex
                && Objects.equals(listOfSongs, testObject.listOfSongs)
                && Arrays.equals(arrPerformancesObject, testObject.arrPerformancesObject);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, surname, age, sex, listOfSongs, arrPerformancesObject);
        result = 19 * result + Arrays.hashCode(arrPerformancesObject);
        return result;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "name='" + name + '\'' +
                ", surname=" + surname + '\'' +
                ", age=" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", listOfSongs='" + listOfSongs.toString() + '\'' +
                ", arrPerformancesObject=" + Arrays.toString(arrPerformancesObject) +
                '}';
    }
}
