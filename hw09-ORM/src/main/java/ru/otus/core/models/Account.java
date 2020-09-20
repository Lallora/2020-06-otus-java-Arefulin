package ru.otus.core.models;

import ru.otus.annotations.Id;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;

    public Account() {
    }

    public Account(long no, String type, int rest) {
        this.no = no;
        this.type = type;
        this.rest = BigDecimal.valueOf(rest);
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

    public BigDecimal getRest() {
        return rest;
    }

    public void setRest(BigDecimal rest) {
        this.rest = rest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return rest.equals(account.rest) &&
                no == account.no &&
                Objects.equals(type, account.type);
    }

    @Override
    public String toString() {
        return "Account{no=" + no + ", type='" + type + '\'' + ", rest=" + rest + '}';
    }
}
