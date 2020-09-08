package ru.otus.serializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

public class MyGson {

    private boolean isPrimitive(Object obj) {
        if (obj instanceof Boolean) return true;
        if (obj instanceof Byte) return true;
        if (obj instanceof Integer) return true;
        if (obj instanceof Short) return true;
        if (obj instanceof Long) return true;
        if (obj instanceof Float) return true;
        if (obj instanceof Double) return true;
        return false;
    }

    private boolean isCharOrString(Object obj) {
        if (obj instanceof Character) return true;
        if (obj instanceof String) return true;
        return false;
    }

    private void serializeArray(Object obj, StringBuilder sbJson) {
        sbJson.append("[");
        for (int i = 0; i < Array.getLength(obj); i++) {
            serialize(Array.get(obj, i), sbJson, null);
            if (i != Array.getLength(obj) - 1) sbJson.append(",");
        }
        sbJson.append("]");
    }

    private void serializeCollection(Object obj, StringBuilder sbJson) {
        sbJson.append("[");
        Iterator iterator = ((Collection) obj).iterator();
        while (iterator.hasNext()) {
            serialize(iterator.next(), sbJson, null);
            if (iterator.hasNext()) sbJson.append(",");
        }
        sbJson.append("]");
    }

    private void serializeOther(Object obj, StringBuilder sbJson) {
        sbJson.append("{");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                field.setAccessible(true);
                if (field.get(obj) != null) serialize(field.get(obj), sbJson, field.getName());
                if (i != fields.length - 1) sbJson.append(",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sbJson.append("}");
    }

    private void writeTitle(String title, StringBuilder sbJson) {
        if (title != null) {
            sbJson.append("\"");
            sbJson.append(title);
            sbJson.append("\":");
        }
    }

    private void prepareJson(Object obj, StringBuilder sbJson) {
        if (isPrimitive(obj)) sbJson.append(obj);
        else if (isCharOrString(obj)) {
            sbJson.append("\"");
            sbJson.append(obj);
            sbJson.append("\"");
        } else if (obj.getClass().isArray()) {
            serializeArray(obj, sbJson);
        } else {
            if (obj instanceof Collection) serializeCollection(obj, sbJson);
            else serializeOther(obj, sbJson);
        }
    }

    private void serialize(Object obj, StringBuilder sbJson, String title) {
        if (obj == null) return;
        writeTitle(title, sbJson);
        prepareJson(obj, sbJson);
    }

    public String toJson(Object obj) {
        if (obj == null) return "null";
        StringBuilder sbJson = new StringBuilder();
        serialize(obj, sbJson, null);
        return sbJson.toString();
    }
}
