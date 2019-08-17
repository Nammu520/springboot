package com.cn.base.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.cn.base.constant.SpecialSymbol;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author dengyu
 * @desc JSON工具包
 * @date 2018-2-26
 */
public class JSONUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        initMapper(mapper);
    }

    /**
     * 初始化
     *
     * @param mapper 对象实例
     */
    private static void initMapper(ObjectMapper mapper) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
        mapper.setDateFormat(new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
    }

    /**
     * 获取实例
     *
     * @param createNew 是否创建新实例
     * @return ObjectMapper 返回数据
     */
    public static synchronized ObjectMapper newInstance(boolean createNew) {
        if (createNew) {
            return new ObjectMapper();
        } else if (mapper == null) {
            mapper = new ObjectMapper();
            initMapper(mapper);
        }
        return mapper;
    }


    /**
     * 对象转字符串
     *
     * @param obj 对象
     * @return String String
     * @throws JsonProcessingException 转换失败异常
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    /**
     * 字符串转对象
     *
     * @param json         字符串
     * @param valueTypeRef 引用类类型
     * @param <T>          泛型
     * @return 引用类实例
     * @throws IOException 异常
     */
    public static <T> T toBean(String json, TypeReference valueTypeRef) throws IOException {
        return mapper.readValue(json, valueTypeRef);
    }

    /**
     * 字符串转对象
     *
     * @param json 字符串
     * @param clz  引用类类型
     * @param <T>  泛型
     * @return 引用类实例
     * @throws IOException 异常
     */
    public static <T> T toBean(String json, Class<T> clz) throws IOException {
        return mapper.readValue(json, clz);
    }

    /**
     * 字符串转MAP
     *
     * @param json 字符串
     * @param clz  引用类类型MAP
     * @param <T>  泛型
     * @return 引用类实例
     * @throws IOException 异常
     */
    public static <T> Map<String, T> toMap(String json, Class<Map<String, T>> clz) throws IOException {
        return mapper.readValue(json, clz);
    }

    /**
     * 字符串转map
     *
     * @param json 字符串
     * @return MAP
     * @throws IOException 异常
     */
    public static Map<String, Object> toMap(String json) throws IOException {
        return mapper.readValue(json, Map.class);
    }

    /**
     * 字符串转list
     *
     * @param json 字符串
     * @param clz  list类型
     * @param <T>  泛型
     * @return list实例
     * @throws IOException 异常
     */
    public static <T> List<T> toList(String json, Class<List<T>> clz) throws IOException {
        return mapper.readValue(json, clz);
    }

    /**
     * 字符串转list
     *
     * @param json         字符串
     * @param valueTypeRef 对象类型
     * @param <T>          泛型
     * @return list实例
     * @throws IOException 异常
     */
    public static <T> List<T> toList(String json, TypeReference valueTypeRef) throws IOException {
        return mapper.readValue(json, valueTypeRef);
    }

    /**
     * 字符串转list
     *
     * @param json            字符串
     * @param collectionClass 对象类型
     * @param elementClasses  对象类型
     * @param <T>             泛型
     * @return list实例
     * @throws IOException 异常
     */
    public static <T> List<T> toList(String json, Class<?> collectionClass,
                                     Class<T> elementClasses) throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        return mapper.readValue(json, javaType);
    }


    /**
     * 从json中读取tagPath处的值 tagPath用 :分隔
     *
     * @param json    字符串
     * @param tagPath 标签路径
     * @return 列表
     * @throws Exception 异常
     */
    public static List<String> readValueFromJson(String json, String tagPath) throws Exception {
        // 返回值
        List<String> value = Lists.newArrayList();
        if (CommonUtil.isEmpty(json) || (CommonUtil.isEmpty(tagPath))) {
            return value;
        }
        ObjectMapper objectMapper = newInstance(false);
        String[] path = tagPath.split(SpecialSymbol.COLON);
        JsonNode node = objectMapper.readTree(json);
        getJsonValue(node, path, value, 1);
        return value;
    }

    /**
     * 从字符串是获取指定属性的值
     *
     * @param node      node
     * @param path      路径
     * @param values    值
     * @param nextIndex 下一索引
     */
    public static void getJsonValue(JsonNode node, String[] path, List<String> values, int nextIndex) {
        if (CommonUtil.isEmpty(node)) {
            return;
        }
        // 是路径的最后就直接取值
        if (nextIndex == path.length) {
            if (node.isArray()) {
                for (int i = 0; i < node.size(); i++) {
                    JsonNode child = node.get(i).get(path[nextIndex - 1]);
                    if (CommonUtil.isEmpty(child)) {
                        continue;
                    }
                    values.add(child.toString());
                }
            } else {
                JsonNode child = node.get(path[nextIndex - 1]);
                if (!CommonUtil.isEmpty(child)) {
                    values.add(child.toString());
                }
            }
            return;
        }
        // 判断是Node下是集合还是一个节点
        node = node.get(path[nextIndex - 1]);
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                getJsonValue(node.get(i), path, values, nextIndex + 1);
            }
        } else {
            getJsonValue(node, path, values, nextIndex + 1);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        boolean result = true;
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            result = (obj.toString().trim().length() == 0)
                    || obj.toString().trim().equalsIgnoreCase(SpecialSymbol.NULL);
        } else if (obj instanceof Collection) {
            result = ((Collection) obj).size() == 0;
        } else {
            result = ((obj == null) || (obj.toString().trim().length() < 1)) ? true : false;
        }
        return result;
    }
}
