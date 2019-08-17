package com.cn.base.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dengyu
 * @desc 实体工具类
 * @date 2018/5/12
 */
public final class EntityUtil {
    /**
     * 隐藏构造器
     */
    private EntityUtil() {
    }

    static {
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ByteConverter(null), Byte.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class type, Object value) {
                if (value == null) {
                    return null;
                }
                return new Date(Long.valueOf(value.toString()));
            }
        }, Date.class);
    }

    /**
     * @param obj obj
     * @return Map
     */
    public static Map<String, String> objectToHash(Object obj) {
        try {
            Map<String, String> map = new HashMap<>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (!property.getName().equals("class")) {
                    if (property.getReadMethod().invoke(obj) != null) {
                        // 时间类型会错乱所以吧时间手动转换成long;
                        if (property.getReadMethod().invoke(obj) != null) {
                            if ("java.util.Date".equals(property.getPropertyType().getTypeName())) {
                                Date invoke = (Date) property.getReadMethod().invoke(obj);
                                long time = invoke.getTime();
                                map.put(property.getName(), String.valueOf(time));
                            } else {
                                map.put(property.getName(), "" + property.getReadMethod().invoke(obj));
                            }
                        }
                    }
                }
            }
            return map;
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param map map
     * @param t   t
     * @param <T> 泛型
     * @return 泛型
     */
    public static <T> T hashToObject(Map<?, ?> map, Class t) {
        try {
            Object obj = t.newInstance();
            BeanUtils.populate(obj, (Map) map);
            return (T) obj;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
