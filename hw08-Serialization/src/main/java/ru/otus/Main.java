package ru.otus;

import com.google.gson.Gson;
import ru.otus.testobjects.TestObject;
import ru.otus.serializer.MyGson;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        MyGson myGson = new MyGson();

        List<String> songs = new ArrayList<>();
        songs.add("Johnny B. Goode");
        songs.add("Rock and roll music");
        songs.add("Run Rudolph run");

        Object[] performances = new Object[]{"Chuck Berry is on top",
                "One Dozen Berrys", "The London Chuck Berry sessions"};

        TestObject testStartObject = new TestObject("Chuck", "Berry", 90,
                true, songs, performances);

        String strGson = gson.toJson(testStartObject);
        String strMyGson = myGson.toJson(testStartObject);

        System.out.println("Gson   : " + strGson);
        System.out.println("MyJson : " + strMyGson);

        TestObject testResObject = gson.fromJson(strMyGson, TestObject.class);

        System.out.println("Are objects equal (testStartObject == testResObject)? " + testStartObject.equals(testResObject));
    }
}
