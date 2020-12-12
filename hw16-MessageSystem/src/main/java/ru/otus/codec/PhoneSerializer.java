package ru.otus.codec;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.otus.model.Phone;

import java.io.IOException;

public class PhoneSerializer extends StdSerializer<Phone> {

    protected PhoneSerializer() {
        super(Phone.class);
    }

    @Override
    public void serialize(Phone value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getNumber());
    }
}
