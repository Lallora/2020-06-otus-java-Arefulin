import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Runnable r = new KeyReader();
        ThreadHandler threadHandler = new ThreadHandler();
        new Thread(() -> threadHandler.showNextNumber(r,  "Thread-1")).start();
        new Thread(() -> threadHandler.showNextNumber(r,  "Thread-2")).start();
    }
}
