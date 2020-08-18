package proxy;

import annotations.ALog;

public class Calculation implements ICalculation{
    public Calculation() {
    }

    @Override
    @ALog
    public void calculation(int a, int b) {
        System.out.println(a + b);
    }

    @Override
    public void calculation(int a, int b, int c) {
        System.out.println(a + b + c);
    }

    @Override
    @ALog
    public void multiplication(int a, int b) {
        System.out.println(a * b);
    }
}
