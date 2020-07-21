package ru.otus;

public class Main {

    public static void main(String[] args) {
        String strHW1_1 = "Hello";
        String strHW1_2 = "OTUS!";
        System.out.println(Borders.hwStart());
        HelloOtus helloOtus1 = new HelloOtus();
        System.out.println(String.format("Предложение 1: %s", helloOtus1.startHelloOtus()));
        HelloOtus helloOtus2 = new HelloOtus(strHW1_1, strHW1_2);
        System.out.println(String.format("Предложение 2: %s", helloOtus2.startHelloOtus()));
        HelloOtus helloOtus3 = new HelloOtus(strHW1_1);
        System.out.println(String.format("Предложение 3: %s", helloOtus3.startHelloOtus()));
        HelloOtus helloOtus4 = new HelloOtus(null, strHW1_2);
        System.out.println(String.format("Предложение 4: %s", helloOtus4.startHelloOtus()));
        HelloOtus helloOtus5 = new HelloOtus(null, null);
        System.out.println(String.format("Предложение 5: %s", helloOtus5.startHelloOtus()));
        System.out.println(Borders.hwEnd());

    }
}
