package ru.otus;

public interface BenchmarkMBean {
    abstract int getSize();
    void setSize(int size);
}
