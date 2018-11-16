package org.seuksa.frmk.tools.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

/**
 * 
 * @author prasnar
 *
 */
public class MyClassUtils {

	/**
     * Get the underlying class for a type, or null if the type is a variable type.
     *
     * @param type the type
     * @return the underlying class
     */
	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} 
		if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} 
		if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		}
		return null;
	}
	
	
	/**
     * Get the actual type arguments a child class has used to extend a generic base class.
     *
     * @param baseClass the base class
     * @param childClass the child class
     * @return a list of the raw classes for the actual type arguments.
     */
    public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
        Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (!getClass(type).equals(baseClass)) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class<?>) type).getGenericSuperclass();
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) parameterizedType.getRawType();

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        Type[] actualTypeArguments;
        if (type instanceof Class) {
            actualTypeArguments = ((Class<?>) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }
    
    /**
     * 
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();

        fields.addAll(Arrays.asList( clazz.getDeclaredFields() ));

        Class<?> superClazz = clazz.getSuperclass();
        if(superClazz != null){
            fields.addAll( getAllFields(superClazz) );
        }

        return fields;
    }
    
    /**
     * 
     * @param clazz
     * @param clazzField
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz, Class<?> clazzField) {
    	List<Field> resLst = new ArrayList<>();
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        for (Field field : fields) {
        	if (field.getType().isAssignableFrom(clazzField)) {
        		resLst.add(field);
        	}
        }
        return resLst;
     }
 
   
    /**
     * 
     * @param srcClazz
     * @param typeSearched
     * @return
     */
    public static List<?> getStaticValues(Class<?> srcClazz, Class<?> typeSearched) {
		List lstRes = new ArrayList<>();
		Field[] declaredFields = srcClazz.getDeclaredFields();
		for (Field field : declaredFields) {
			try {
				if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(typeSearched)) {
		        	lstRes.add(field.get(null));
				}
			} catch (Exception e) {
				throw new IllegalStateException("Error on Field [" + field.getName() + "]", e);
			}
		}
		return lstRes;
	}
    
    /**
     * 
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getValue(Object object, String fieldName) {
    	Field field;
		try {
			field = object.getClass().getDeclaredField(fieldName);
		
	    	field.setAccessible(true);
	
	    	Class<?> targetType = field.getType();
	    	Object objectValue = targetType.newInstance();
	
	    	Object value = field.get(objectValue);
	    	
	    	return value;
		} catch (Exception e) {
			throw new IllegalStateException("Error on Field [" + fieldName + "]", e);
		}
		
    }
    
    /**
     * 
     * @param object
     * @param fieldName
     * @param value
     */
    public static void setValue(Object bean, Class clazz, String fieldName, Object value) {
		try {
			String setFieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method[] allMethods = ReflectionUtils.getAllDeclaredMethods(clazz);
			for (Method method : allMethods) {
			    if (method.getName().equals(setFieldName)) {
			    	ReflectionUtils.invokeMethod(method, bean, new Object[] { value });
			    	break;
			    }
			}
		} catch (Exception e) {
			throw new IllegalStateException("Error on Field [" + fieldName + "]", e);
		}
    }
}
