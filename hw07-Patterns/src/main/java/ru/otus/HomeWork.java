package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerHistoric;
import ru.otus.listener.ListenerPrinter;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessReplace11and13;
import ru.otus.processor.ProcessorThrowExeption;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (ready)
       2. Сделать процессор, который поменяет местами значения field11 и field13 (ready)
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (ready)
       4. Сделать Listener для ведения истории: старое сообщение - новое (вот это не понял, что именно надо)
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элементов "to do" создать new ComplexProcessor и обработать сообщение
         */

        var processors = List.of(
                new LoggerProcessor(new ProcessReplace11and13()), new ProcessorThrowExeption());

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});
        var listenerPrinter = new ListenerPrinter();
        var listenerHistoric = new ListenerHistoric();

        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(listenerHistoric);

        var message = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13("field13")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("\nresult:" + result);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(listenerHistoric);
    }
}
