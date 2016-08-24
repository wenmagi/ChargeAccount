package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

class JavaObjectDeserializer implements ObjectDeserializer {

    public final static JavaObjectDeserializer instance = new JavaObjectDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType instanceof TypeVariable) {
                TypeVariable<?> componentVar = (TypeVariable<?>) componentType;
                componentType = componentVar.getBounds()[0];
            }

            List<Object> list = new ArrayList<Object>();
            parser.parseArray(componentType, list);
            Class<?> componentClass;
            if (componentType instanceof Class) {
                componentClass = (Class<?>) componentType;

                Object[] array = (Object[]) Array.newInstance(componentClass, list.size());
                list.toArray(array);
                return (T) array;
            } else {
                return (T) list.toArray();
            }

        }

        return (T) parser.parse(fieldName);
    }
}
