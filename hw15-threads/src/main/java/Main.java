import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        ThreadHandler threadHandler = new ThreadHandler();
        new Thread(() -> threadHandler.showNextNumber("Thread-1")).start();
        new Thread(() -> threadHandler.showNextNumber("Thread-2")).start();
    }
}
