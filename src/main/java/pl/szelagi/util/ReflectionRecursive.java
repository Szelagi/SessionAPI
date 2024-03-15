// Author: Szelagi
// Github: https://github.com/Szelagi

package pl.szelagi.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionRecursive {
    @Deprecated
    public static Method getDeclaredMethod(Object object, String name, Class<?> ...parameterTypes) throws NoSuchMethodException, SecurityException {
        return getDeclaredMethod(object.getClass(), name, parameterTypes);
    }
    public static Method getDeclaredMethod(Class<?> classType, String name, Class<?> ...parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method;
        Class<?> currentClass = classType;
        do {
            try {
                method = currentClass.getDeclaredMethod(name, parameterTypes);
                return method;
            } catch (NoSuchMethodException ignore) {}
        } while (
                (currentClass = currentClass.getSuperclass()) != null
        );
        throw new NoSuchMethodException("ReflectionRecursive no such method '" + name + "' in class" + classType.getName());
    }
    @Deprecated
    public static Field getDeclaredField(Object object, String name) throws NoSuchFieldException, SecurityException {
        return getDeclaredField(object.getClass(), name);
    }

    public static Field getDeclaredField(Class<?> classType, String name) throws NoSuchFieldException, SecurityException {
        Field field;
        Class<?> currentClass = classType;
        do {
            try {
                field = currentClass.getDeclaredField(name);
                return field;
            } catch (NoSuchFieldException ignore) {}
        } while (
                (currentClass = currentClass.getSuperclass()) != null
        );
        throw new NoSuchFieldException("ReflectionRecursive no such field  '" + name + "' in class " + classType.getName());
    }
}
