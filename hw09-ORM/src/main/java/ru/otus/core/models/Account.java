package ru.otus.core.models;

import ru.otus.annotations.Id;

public class Account {
    @Id
    private long no;
    private String type;
    private long rest;

    public Account() {
    }
    public Account(long no, String type, long rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getRest() { return rest; }

    public void setRest(long rest) { this.rest = rest; }

    @Override
    public String toString() {
        return "Account{no=" + no + ", type='" + type + '\'' + ", rest=" + rest + '}';
    }
}
