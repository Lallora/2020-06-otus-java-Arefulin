package ru.otus;

public class TextColor {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static String black(String txt) {
        return ANSI_BLACK + txt + ANSI_RESET;
    }

    public static String red(String txt) {
        return ANSI_RED + txt + ANSI_RESET;
    }

    public static String green(String txt) {
        return ANSI_GREEN + txt + ANSI_RESET;
    }

    public static String yellow(String txt) {
        return ANSI_YELLOW + txt + ANSI_RESET;
    }

    public static String blue(String txt) {
        return ANSI_BLUE + txt + ANSI_RESET;
    }

    public static String purple(String txt) {
        return ANSI_PURPLE + txt + ANSI_RESET;
    }

    public static String cyan(String txt) {
        return ANSI_CYAN + txt + ANSI_RESET;
    }

    public static String white(String txt) {
        return ANSI_WHITE + txt + ANSI_RESET;
    }
}
