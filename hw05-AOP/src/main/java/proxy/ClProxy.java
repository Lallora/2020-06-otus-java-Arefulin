package proxy;

import annotations.ALog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClProxy {

    private ClProxy() {
    }

    public static ICalculation create() {
        InvocationHandler handler = new OwnInvocationHandler(new Calculation());
        return (ICalculation) Proxy.newProxyInstance(ClProxy.class.getClassLoader(),
                new Class<?>[]{ICalculation.class}, handler);
    }

    static class OwnInvocationHandler implements InvocationHandler {
        private final ICalculation calculation;
        private final List<String> loggingMethods;

        OwnInvocationHandler(ICalculation calculation) {
            this.calculation = calculation;
            loggingMethods = methodForAutoLogging(calculation.getClass().getMethods());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLoggingMethod(method)) {
                System.out.println("Executed method: " + method.getName() + ": " + Arrays.toString(args));
            }
            return method.invoke(calculation, args);
        }

        private boolean isLoggingMethod(Method method) {
            return Collections.binarySearch(loggingMethods, method.getName()) >= 0;
        }

        private List<String> methodForAutoLogging(Method[] methods) {
            List<String> methodsToLog = new ArrayList<>();
            for (Method method : methods) {
                if (method.isAnnotationPresent(ALog.class)) {
                    methodsToLog.add(method.getName());
                }
            }
            Collections.sort(methodsToLog);
            return methodsToLog;
        }
    }
}
