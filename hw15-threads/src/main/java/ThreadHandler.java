import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadHandler {

    private boolean isIncrement = true;
    private boolean isNeedShow = true;
    private int number = 0;
    private String lastThreadName = "Thread-2";

    final private static Logger logger = LoggerFactory.getLogger(ThreadHandler.class);

    public synchronized void showNextNumber(String threadName) {
        for (int i = 0; i < 19; i++) {
            while (lastThreadName.equals(threadName)) {
                try {
                    wait();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            if (isNeedShow) {
                int maxNumber = 10;
                if (number < maxNumber && isIncrement) {
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
    }

    private void showNumber (String threadName) {
        logger.info(threadName+"\t"+this.number);
    }
}
