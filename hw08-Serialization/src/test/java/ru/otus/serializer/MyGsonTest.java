package ru.otus.serializer;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyGsonTest {
    Gson gson;
    MyGson myGson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
        myGson = new MyGson();
    }

    @Test
    void checkNull() {
        assertEquals(myGson.toJson((null)), gson.toJson((null)));
    }

    @Test
    void checkEqualsByteType() {
        assertEquals(myGson.toJson((byte) 1), gson.toJson((byte) 1));
    }

    @Test
    void checkEqualsShortType() {
        assertEquals(myGson.toJson((short) 1f), gson.toJson((short) 1f));
    }

    @Test
    void checkEqualsIntType() {
        assertEquals(myGson.toJson(1), gson.toJson(1));
    }

    @Test
    void checkEqualsLongType() {
        assertEquals(myGson.toJson(1L), gson.toJson(1L));
    }

    @Test
    void checkEqualsFloatType() {
        assertEquals(myGson.toJson(1f), gson.toJson(1f));
    }

    @Test
    void checkEqualsDoubleType() {
        assertEquals(myGson.toJson(1d), gson.toJson(1d));
    }

    @Test
    void checkEqualsStringType() {
        assertEquals(myGson.toJson("aaa"), gson.toJson("aaa"));
    }

    @Test
    void checkEqualsCharType() {
        assertEquals(myGson.toJson('a'), gson.toJson("a"));
    }

    @Test
    void checkEqualsPrimitiveArrType() {
        assertEquals(myGson.toJson(new int[]{1, 2, 3}), gson.toJson(new int[]{1, 2, 3}));
    }

    @Test
    void checkEqualsListType() {
        assertEquals(myGson.toJson(List.of(1, 2, 3)), gson.toJson(List.of(1, 2, 3)));
    }

    @Test
    void checkEqualsCollectionsType() {
        assertEquals(myGson.toJson(Collections.singletonList(1)), gson.toJson(Collections.singletonList(1)));
    }
}
