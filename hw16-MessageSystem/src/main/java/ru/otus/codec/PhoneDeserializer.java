package ru.otus.codec;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.otus.model.Phone;

import java.io.IOException;

public class PhoneDeserializer extends StdDeserializer<Phone> {

    protected PhoneDeserializer() {
        super(Phone.class);
    }

    @Override
    public Phone deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new Phone(p.getValueAsString());
    }
}
