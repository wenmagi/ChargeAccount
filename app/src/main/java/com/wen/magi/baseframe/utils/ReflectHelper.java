package com.wen.magi.baseframe.utils;

import java.lang.reflect.Method;

/**
 * Created by MVEN on 16/5/4.
 */
public class ReflectHelper {

    /**
     * call object's nostatic method by mechodName when running time
     *
     * @param owner      the target object
     * @param methodName this objects method name
     * @param args
     * @return
     * @throws Exception
     */
    public Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        Class[] argsClass = null;
        if (args != null) {
            argsClass = new Class[args.length];
            int i = 0;
            for (Object object : args) {
                argsClass[i] = object.getClass();
                i++;
            }
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(ownerClass, args);

    }

    /**
     * call object's static method by mechodName when running time
     *
     * @param className  className
     * @param methodName methodName
     * @param args
     * @return
     * @throws Exception
     */
    public Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
        Class ownerClass = Class.forName(className);
        Class[] argsClass = null;
        if (args != null) {
            argsClass = new Class[args.length];
            int i = 0;
            for (Object object : args) {
                argsClass[i] = object.getClass();
                i++;
            }
        }

        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(null, args);
    }
}
