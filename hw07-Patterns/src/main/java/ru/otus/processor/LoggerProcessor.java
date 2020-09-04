package ru.otus.processor;

import ru.otus.Message;

public class LoggerProcessor implements Processor {
    //todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду

    private final Processor processor;

    public LoggerProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) {
        System.out.println("\nlog processing message:" + message);
        return processor.process(message);
    }
}
