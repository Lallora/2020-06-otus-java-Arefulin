package hw17.server.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ReciveMessageTest {
    private final static String TEST_JSON =  "{\"from\":\"front\",\"to\":\"db\",\"data\":\"content\"}";
    @Test
    public void formJsonForEmptyStringTest() {
        assertTrue( InterprocessMessage.fromJson("").isEmpty());
    }

    @Test
    public void formJson() {
        assertTrue( InterprocessMessage.fromJson(TEST_JSON).isPresent());
    }

    @Test
    public void toJsonTest(){
        final InterprocessMessage message = new InterprocessMessage("front", "db", "content");
        assertEquals(TEST_JSON, message.toJson());
    }
}
