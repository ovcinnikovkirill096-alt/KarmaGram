package org.mvel2.util;

public class ReflectionUtil {
    public static String getSetter(String str) {
        char[] cArr = new char[str.length() + 3];
        cArr[0] = 's';
        cArr[1] = 'e';
        cArr[2] = 't';
        cArr[3] = Character.toUpperCase(str.charAt(0));
        for (int length = str.length() - 1; length != 0; length--) {
            cArr[length + 3] = str.charAt(length);
        }
        return new String(cArr);
    }

    public static String getGetter(String str) {
        char[] charArray = str.toCharArray();
        char[] cArr = new char[charArray.length + 3];
        cArr[0] = 'g';
        cArr[1] = 'e';
        cArr[2] = 't';
        cArr[3] = Character.toUpperCase(charArray[0]);
        System.arraycopy(charArray, 1, cArr, 4, charArray.length - 1);
        return new String(cArr);
    }

    public static String getIsGetter(String str) {
        char[] charArray = str.toCharArray();
        char[] cArr = new char[charArray.length + 2];
        cArr[0] = 'i';
        cArr[1] = 's';
        cArr[2] = Character.toUpperCase(charArray[0]);
        System.arraycopy(charArray, 1, cArr, 3, charArray.length - 1);
        return new String(cArr);
    }

    public static String getPropertyFromAccessor(String str) {
        char[] charArray = str.toCharArray();
        int i = 1;
        if (charArray.length > 3 && charArray[1] == 'e' && charArray[2] == 't') {
            int length = charArray.length - 3;
            char[] cArr = new char[length];
            char c = charArray[0];
            if (c != 'g' && c != 's') {
                return str;
            }
            cArr[0] = Character.toLowerCase(charArray[3]);
            while (i < length) {
                cArr[i] = charArray[i + 3];
                i++;
            }
            return new String(cArr);
        }
        if (charArray.length <= 2 || charArray[0] != 'i' || charArray[1] != 's') {
            return str;
        }
        int length2 = charArray.length - 2;
        char[] cArr2 = new char[length2];
        cArr2[0] = Character.toLowerCase(charArray[2]);
        while (i < length2) {
            cArr2[i] = charArray[i + 2];
            i++;
        }
        return new String(cArr2);
    }

    public static Class<?> toNonPrimitiveType(Class<?> cls) {
        if (!cls.isPrimitive()) {
            return cls;
        }
        if (cls == Integer.TYPE) {
            return Integer.class;
        }
        if (cls == Long.TYPE) {
            return Long.class;
        }
        if (cls == Double.TYPE) {
            return Double.class;
        }
        if (cls == Float.TYPE) {
            return Float.class;
        }
        if (cls == Short.TYPE) {
            return Short.class;
        }
        if (cls == Byte.TYPE) {
            return Byte.class;
        }
        return cls == Character.TYPE ? Character.class : Boolean.class;
    }

    public static Class<?> toNonPrimitiveArray(Class<?> cls) {
        if (!cls.isArray() || !cls.getComponentType().isPrimitive()) {
            return cls;
        }
        if (cls == int[].class) {
            return Integer[].class;
        }
        if (cls == long[].class) {
            return Long[].class;
        }
        if (cls == double[].class) {
            return Double[].class;
        }
        if (cls == float[].class) {
            return Float[].class;
        }
        if (cls == short[].class) {
            return Short[].class;
        }
        if (cls == byte[].class) {
            return Byte[].class;
        }
        return cls == char[].class ? Character[].class : Boolean[].class;
    }

    public static Class<?> toPrimitiveArrayType(Class<?> cls) {
        if (!cls.isPrimitive()) {
            throw new RuntimeException(cls + " is not a primitive type");
        }
        if (cls == Integer.TYPE) {
            return int[].class;
        }
        if (cls == Long.TYPE) {
            return long[].class;
        }
        if (cls == Double.TYPE) {
            return double[].class;
        }
        if (cls == Float.TYPE) {
            return float[].class;
        }
        if (cls == Short.TYPE) {
            return short[].class;
        }
        if (cls == Byte.TYPE) {
            return byte[].class;
        }
        return cls == Character.TYPE ? char[].class : boolean[].class;
    }

    public static boolean isAssignableFrom(Class<?> cls, Class<?> cls2) {
        return cls.isAssignableFrom(cls2) || areBoxingCompatible(cls, cls2);
    }

    private static boolean areBoxingCompatible(Class<?> cls, Class<?> cls2) {
        if (cls.isPrimitive()) {
            return isPrimitiveOf(cls2, cls);
        }
        return cls2.isPrimitive() && isPrimitiveOf(cls, cls2);
    }

    private static boolean isPrimitiveOf(Class<?> cls, Class<?> cls2) {
        if (cls2 == Integer.TYPE) {
            return cls == Integer.class;
        }
        if (cls2 == Long.TYPE) {
            return cls == Long.class;
        }
        if (cls2 == Double.TYPE) {
            return cls == Double.class;
        }
        if (cls2 == Float.TYPE) {
            return cls == Float.class;
        }
        if (cls2 == Short.TYPE) {
            return cls == Short.class;
        }
        if (cls2 == Byte.TYPE) {
            return cls == Byte.class;
        }
        if (cls2 == Character.TYPE) {
            return cls == Character.class;
        }
        return cls2 == Boolean.TYPE && cls == Boolean.class;
    }
}
