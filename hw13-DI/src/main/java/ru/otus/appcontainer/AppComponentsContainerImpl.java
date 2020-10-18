package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        final var allMethods = loadAllAnnotatedMethods(configClass.getDeclaredMethods());
        for (Method method : allMethods) {
            final var component = makeComponent(method);
            if (component == null) {
                throw new RuntimeException("Component not created");
            }
            appComponents.add(component);
            final String name = method.getAnnotation(AppComponent.class).name();
            appComponentsByName.put(name, component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (configClass == null) {
            throw new IllegalArgumentException("Given class is null");
        }
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object object : appComponents) {
            if (componentClass.isAssignableFrom(object.getClass())) {
                return (C) object;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        final Object component = appComponentsByName.get(componentName);
        return (component == null) ? null : (C) component;
    }

    private List<Method> loadAllAnnotatedMethods(Method[] methods) {
        return Arrays.stream(methods).filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
    }

    private <C> C makeComponent(Method method) {
        final var classWithMethod = method.getDeclaringClass();
        final Class<?>[] argsTypes = method.getParameterTypes();
        final var args = getArgsObject(argsTypes);
        try {
            final var instance = classWithMethod.getDeclaredConstructor().newInstance();
            return (C) method.invoke(instance, args);
        } catch (Exception e) {
            return null;
        }
    }

    private Object[] getArgsObject(Class<?>[] argsTypes) {
        final List<Object> args = new ArrayList<>();
        for (Class<?> type : argsTypes) {
            final var appComponent = getAppComponent(type);
            if (appComponent == null) {
                throw new RuntimeException(String.format("No component of type %s in the context", type));
            }
            args.add(appComponent);
        }
        return args.toArray();
    }
}
