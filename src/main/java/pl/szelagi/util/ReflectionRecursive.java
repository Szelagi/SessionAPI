// Author: Szelagi
// Github: https://github.com/Szelagi

package pl.szelagi.util;

import java.lang.reflect.Method;

public class ReflectionRecursive {
    public static Method getDeclaredMethod(Object object, String name, Class<?> ...parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method;
        Class<?> currentClass = object.getClass();
        do {
            try {
                method = currentClass.getDeclaredMethod(name, parameterTypes);
                return method;
            } catch (NoSuchMethodException ignore) {}
        } while (
                (currentClass = currentClass.getSuperclass()) != null
        );
        throw new NoSuchMethodException("ReflectionRecursive no such method: " + name + " in " + object.getClass().getName());
    }
}
