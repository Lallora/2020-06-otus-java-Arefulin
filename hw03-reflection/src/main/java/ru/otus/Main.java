package ru.otus;

import ru.otus.annotations.runner.Tester;

public class Main {
    public static void main(String[] args) throws Exception {

        Tester tester = new Tester();
        tester.start("ru.otus.test.ForTest");
    }
}
