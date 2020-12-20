package ru.otus.codec;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.otus.model.Address;

import java.io.IOException;

public class AddressSerializer extends StdSerializer<Address> {
    protected AddressSerializer() {
        super(Address.class);
    }

    @Override
    public void serialize(Address value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getStreet());
    }
}
