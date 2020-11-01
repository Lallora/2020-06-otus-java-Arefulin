import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

public class ThreadHandler {

    private boolean isIncrement = true;
    private boolean isNeedShow = true;
    private int number = 0;
    private String lastThreadName = "Thread-2";


    final private static Logger logger = LoggerFactory.getLogger(ThreadHandler.class);

    public synchronized void showNextNumber(Runnable r, String threadName) {
        try {
            for (int i = 0; i < 19; i++) {
                while (!Thread.currentThread().isInterrupted() && lastThreadName.equals(threadName)) {
                    sleep(1000);
                    wait();
                }

                if (isNeedShow) {
                    int maxNumber = 10;
                    if (number < maxNumber && isIncrement) {
                        if (number == 3) r.run();
                        if (number == 6) r.run();
                        ++number;
                    } else {
                        isIncrement = false;
                        --number;
                    }
                }
                showNumber(threadName);
                lastThreadName = threadName;
                isNeedShow = !isNeedShow;
                notifyAll();
            }
            Thread.currentThread().interrupt();
        }catch (Exception e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void showNumber (String threadName) {
        logger.info(threadName+"\t"+this.number);
    }
}
