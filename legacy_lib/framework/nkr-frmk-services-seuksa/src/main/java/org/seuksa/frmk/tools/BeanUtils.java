package org.seuksa.frmk.tools;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class BeanUtils {

    /**
     * @param value
     * @return
     */
    public static boolean isDefined(final Object value) {
        if (value instanceof Definable) {
            return ((Definable) value).isDefined();
        }
        return value != null && !"".equals(value);
    }

    /**
     * @param object
     * @return
     * @throws Exception
     */
    public static Object cloneBean(final Object object) throws Exception {
        return org.apache.commons.beanutils.BeanUtils.cloneBean(object);
    }

    /**
     * @param object
     * @param completeProperty
     * @return
     * @throws Exception
     */
    public static Object getProperty(final Object object, final String completeProperty) throws Exception {
        if (null == object || StringUtils.isEmpty(completeProperty)) {
            return null;
        }
        final String[] properties = StringUtils.split(completeProperty, ".");
        Object propertyValue = object;
        for (final String property : properties) {
            propertyValue = PropertyUtils.getSimpleProperty(propertyValue, property);
        }
        return propertyValue;
    }

    /**
     * @param obj
     * @param property
     * @param value
     * @throws Exception
     */
    public static void setProperty(final Object object, final String completeProperty, final Object value) throws Exception {
        if (null == object || StringUtils.isEmpty(completeProperty)) {
            return;
        }
        final String[] properties = StringUtils.split(completeProperty, ".");
        for (final String property : properties) {
            PropertyUtils.setSimpleProperty(object, property, value);
        }
    }

    /**
     * @param object
     * @param propertyNames
     * @param values
     * @throws Exception
     */
    public static void setProperties(final Object object, final String[] propertyNames, final Object[] values) throws Exception {
        for (int i = 0; i < propertyNames.length; i++) {
            setProperty(object, propertyNames[i], values[i]);
        }
    }

    /**
     * @param string
     * @return
     */
    public static String nullsafe(final String string) {
        return isDefined(string) ? string : "";
    }

    /**
     * @param object1
     * @param object2
     * @return
     */
    public static boolean nullSafeEquals(final Object object1, final Object object2) {
        if (object1 == null) {
            return (object2 == null);
        }
        return object1.equals(object2);
    }

    public static boolean hasProperty(final Object object, final String propertyName) {
        return PropertyUtils.isReadable(object, propertyName);
    }
}
