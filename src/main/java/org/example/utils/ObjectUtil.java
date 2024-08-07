package org.example.utils;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * org.example
 * User: fd
 * Date: 2024/08/07 10:00
 * Description:
 * Version: V1.0
 */
public class ObjectUtil {
    public static void copyProperties(Object source, Object target, String[] properties) {
        // TODO: Implement this method
//        1. 当properties参数为空数组时，throw java.security.InvalidParameterException 异常。
//        2. source和target Object 可能是不同类型的class，但他们存在同名同类型的属性，就能进行拷贝。
//        3. 当properties参数中的属性，在source和target中同名但类型不一致时，throw RuntimeException 异常。
//        4. source Object的属性，如果是数组、对象等，需要进行深拷贝，即修改target的值不会同步修改source的值。
//        5. 只能使用 java.lang.reflect包下面的类或函数实现，不能使用第三方的库和函数，包括spring boot的。
//        6. 请补充各种异常情况和正常情况的测试代码，能处理循环引用等复杂情况者更佳。

        //1.当properties参数为空数组时，throw java.security.InvalidParameterException 异常。
        if (properties == null || properties.length == 0) {
            throw new InvalidParameterException("Properties数组为空");
        }

        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();

        for (String propertyName : properties) {
            boolean found = false;
            for (Field sourceField : sourceFields) {
                if (sourceField.getName().equals(propertyName)) {
                    found = true;
                    Field targetField = Arrays.stream(targetFields)
                            .filter(field -> field.getName().equals(propertyName))
                            .findFirst()
                            .orElse(null);

                    if (targetField == null) {
                        throw new RuntimeException("Property " + propertyName + " not found in target object");
                    }

                    //3.当properties参数中的属性，在source和target中同名但类型不一致时，throw RuntimeException 异常。
                    if (!sourceField.getType().equals(targetField.getType())) {
                        throw new RuntimeException("属性" + propertyName + " 类型不匹配");
                    }

                    try {
                        sourceField.setAccessible(true);
                        targetField.setAccessible(true);
                        Object value = sourceField.get(source);
                        if (value != null && value.getClass().isArray()) {
                            // Handle array types
                            value = deepCopyArray(value);
                        }
                        targetField.set(target, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access field", e);
                    }
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Property " + propertyName + " not found in source object");
            }
        }
    }

    private static Object deepCopyArray(Object array) {
        int length = java.lang.reflect.Array.getLength(array);
        Object copy = java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);
        for (int i = 0; i < length; i++) {
            Object element = java.lang.reflect.Array.get(array, i);
            if (element.getClass().isArray()) {
                java.lang.reflect.Array.set(copy, i, deepCopyArray(element));
            } else {
                java.lang.reflect.Array.set(copy, i, element);
            }
        }
        return copy;
    }
}
