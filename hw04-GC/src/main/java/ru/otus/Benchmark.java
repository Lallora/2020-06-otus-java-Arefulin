package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {
    private final int loopCounter;
    private volatile int size = 0;
    private List<Integer> listLoading = new ArrayList();

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size:" + size);
        this.size = size;
    }

    public void run() throws InterruptedException {
        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;
            for (int i = 0; i < local; i++) {
                listLoading.add(1000);
            }

            // Normal using
            listLoading.clear();
            Thread.sleep(10);

            // Memory leak
            //listLoading.remove(1);
            //Thread.sleep(3000);
        }
    }
}
