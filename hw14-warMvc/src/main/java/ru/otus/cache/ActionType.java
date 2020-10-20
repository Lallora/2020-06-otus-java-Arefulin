package ru.otus.cache;

public enum ActionType {
    GET("get"), PUT("put"), REMOVE("remove");

    private final String type;

    ActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
