package reflectionTesting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;

public class Tester {
    private static Object o;

    private static boolean isConditionMet(Class c) {
        int before = 0;
        int after = 0;

        for (Method m: c.getDeclaredMethods()) {
            if (m.getAnnotation(BeforeSuite.class) != null) {
                before++;
            }
            if (m.getAnnotation(AfterSuite.class) != null) {
                after++;
            }

        } return  (before <=1 && after <=1);


    }

    public static void start(Class c) {
        if (isConditionMet(c) == false) {
            throw new RuntimeException();
        }

        try {
            o = c.getDeclaredConstructor().newInstance();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Method before = null;
        Method after = null;

        ArrayList<Method> methods = new ArrayList<>();
        Method[] decMethods = c.getDeclaredMethods();

        for (Method m : decMethods) {
            if (m.getAnnotation(BeforeSuite.class) != null) {
                before = m;
            } else if (m.getAnnotation(AfterSuite.class) != null) {
                after = m;
            } else if (m.getAnnotation(Test.class) != null) {
                methods.add(m);
            }
        }

        methods.sort(Comparator.comparingInt(o -> o.getAnnotation(Test.class).prio()));

        if (before != null) {
            methods.add(0, before);
        }
        if (after != null) {
            methods.add(after);
        }

        try {
            for (Method method : methods) {
                if (Modifier.isPrivate(method.getModifiers())) {
                    method.setAccessible(true);
                }
                method.invoke(o);
            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
