package ru.otus.test;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class ForTest {
    @Before
    public void beforeMethod() {
        System.out.println("running beforeMethod ...");
    }

    @Test
    public void testMethod1() {
        System.out.println("running testMethod1 ...");
    }

    @Test
    private void testMethod2() throws RuntimeException {
        throw new RuntimeException("*** Crash test! ***");
    }

    // Намеренно оставлен без аннотации
    @Test
    private String testMethod3() {
        System.out.println("running testMethod3 ...");
        return "running testMethod3 ...";
    }

    @Test
    public void testMethod4() throws RuntimeException {
        if (2 == 3) {
            throw new RuntimeException("*** Attention! We've got an unique case! 2=3! ***");
        }
        System.out.println("running testMethod4 ...");
    }

    @After
    public void afterMethod() {
        System.out.println("running afterMethod ...");
    }

}
