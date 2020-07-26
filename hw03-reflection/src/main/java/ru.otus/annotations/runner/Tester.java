package ru.otus.annotations.runner;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Tester {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public void start(String className) throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            ClassNotFoundException {

        int passedTests = 0;
        int faultTests = 0;
        String strCatch = "";
        Object tempObject;

        Class clazz = Class.forName(className);

        List<Method> beforeMethodsList = new ArrayList<>();
        List<Method> testMethodsList = new ArrayList<>();
        List<Method> afterMethodsList = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethodsList.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethodsList.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethodsList.add(method);
            }
        }

        int allTests = testMethodsList.size();

        Method[] beforeMethods = new Method[beforeMethodsList.size()];
        Method[] afterMethods = new Method[afterMethodsList.size()];

        beforeMethods = beforeMethodsList.toArray(beforeMethods);
        afterMethods = afterMethodsList.toArray(afterMethods);

        for (Method method : testMethodsList) {
            tempObject = clazz.getDeclaredConstructor().newInstance();
            try {
                System.out.println("\nMethod: " + method);
                invokeMethods(tempObject, beforeMethods);
                invokeMethods(tempObject, method);
                passedTests++;
            } catch (IllegalAccessException | InvocationTargetException e) {
                strCatch = ANSI_RED + "Attention!" + ANSI_RESET + " Method " + method.getName() + " thrown " + e.getCause();
                System.out.println(strCatch);
                System.err.println(strCatch);
                faultTests++;
            } finally {
                try {
                    invokeMethods(tempObject, afterMethods);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    strCatch = ANSI_RED + "Attention!" + ANSI_RESET + " Method " + method.getName() + " thrown " + e.getCause();
                    System.out.println(strCatch);
                    System.err.println(strCatch);
                }
            }
        }
        printResult(allTests, passedTests, faultTests);
    }

    private static void invokeMethods(Object object, Method... methods) throws
            InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.setAccessible(true);
            method.invoke(object);
        }
    }

    private void printResult(int allTests, int passedTests, int faultTests) {
        String strBorder = "\n===================";
        System.out.println(String.format(strBorder +
                "\nAll tests:\t\t%d\nPassed tests:\t%d\nFault tests:\t%d" +
                strBorder, allTests, passedTests, faultTests));
    }
}

