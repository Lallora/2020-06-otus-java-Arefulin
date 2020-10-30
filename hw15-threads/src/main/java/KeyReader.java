import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyReader implements Runnable {
    public void run() {
        String answer = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Stop it? (Y/N)");
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (answer.equals("Y") || answer.equals("y")) {
            System.out.println("Выполнение остановлено пользователем");
            System.exit(0);
        }
    }
}
