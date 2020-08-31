package ru.otus.processor;

import ru.otus.Message;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ProcessorThrowExeption implements Processor {

    @Override
    public Message process(Message message) {

        final int delay = 1000;  // delay for 1 sec.
        final int period = 1000; // repeat every 2 sec.
        final int duration = 10; // stop after 10 times
        final int[] counter = {0};

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    Date date = new Date();
                    if (date.getSeconds() % 2 == 0) {
                        throw new RuntimeException("\n" + date.toString() + " Exception number: " + ++counter[0]);
                    }
                } catch (RuntimeException re) {
                    System.out.print(re.getMessage());
                } finally {
                    if (counter[0] == duration) timer.cancel();   // stop the displaying
                }
            }
        }, delay, period);
        return message;
    }
}