package ru.otus.formatter;

import org.springframework.format.Formatter;
import ru.otus.model.Phone;

import java.text.ParseException;
import java.util.Locale;

public class PhoneFormatter implements Formatter<Phone> {
    @Override
    public Phone parse(String text, Locale locale) throws ParseException {
        return new Phone(text);
    }

    @Override
    public String print(Phone object, Locale locale) {
        return object.getNumber();
    }
}
