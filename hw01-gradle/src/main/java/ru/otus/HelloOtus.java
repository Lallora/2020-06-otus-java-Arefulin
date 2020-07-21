package ru.otus;

import java.util.Scanner;

import static com.google.common.base.MoreObjects.firstNonNull;

public class HelloOtus {


    private String firstInput;
    private String secondInput;

    {
        secondInput = null;
    }

    public HelloOtus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(TextColor.blue("Constructor without parameters called"));
        System.out.println("Enter the your (first) word, please:");
        this.firstInput = scanner.next();
        System.out.println(TextColor.yellow("Attention! Second word intentionally was missed"));
    }

    public HelloOtus(String firstInput) {
        System.out.println(TextColor.blue("\nConstructor with one parameter called (param1 = " + firstInput + ")"));
        this.firstInput = firstInput;
    }

    public HelloOtus(String firstInput, String secondInput) {
        System.out.println(TextColor.blue("\nConstructor with two parameters called (param1 = " + firstInput + ", param2 = " + secondInput + ")"));
//        this(firstInput);
        this.firstInput = firstInput;
        this.secondInput = secondInput;
    }

    public String startHelloOtus() {
        String msgFull = "Regular situation";
        String msgFirstNull = "First word is null. Using firstNonNull () method from guava";
        String msgSecondNull = "Second word is null. Using firstNonNull () method from guava";
        String msgEmergencySituation = "Attention! We've got an emergency situation. Both values are null.";
        String msgEmptyResult = "It's empty. Nothing to show.";
        String msgRes = "";
        String result = "";
        try {
            if (firstInput == null && secondInput != null) {
                msgRes = msgFirstNull;
            } else if (secondInput == null && firstInput != null) {
                msgRes = msgSecondNull;
            } else if (secondInput != null && firstInput != null) {
                msgRes = msgFull;
            } else {
                throw new Exception(msgEmergencySituation);
            }
            System.out.println(TextColor.cyan(msgRes));
            result = firstNonNull(firstInput, secondInput) + " " + firstNonNull(secondInput, firstInput);
        } catch (Exception myException) {
            msgRes = msgEmergencySituation;
            System.out.println(TextColor.red(msgRes));
            result = TextColor.red(msgEmptyResult);
        } finally {
            return result;
        }
    }
}
