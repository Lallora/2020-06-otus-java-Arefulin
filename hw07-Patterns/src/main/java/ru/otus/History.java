package ru.otus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class History {

    public static boolean writeToHistoryFile(String msgInput) {
        boolean result = false;
        String filePath = "logs\\history.txt";
        try {
            FileWriter writer = new FileWriter(filePath, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write("\n" + msgInput);
            bufferWriter.close();
            result = true;
        }
        catch (IOException e) {
            result = false;
            System.out.println(e);
        } finally {
            return result;
        }
    }
}
