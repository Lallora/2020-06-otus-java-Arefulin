import proxy.ClProxy;
import proxy.ICalculation;

public class Main {
    public static void main(String[] args) {
        ICalculation operation = ClProxy.create();
        operation.calculation(1, 2);
        operation.calculation(1, 2, 3);
        operation.multiplication(4, 5);
    }
}
