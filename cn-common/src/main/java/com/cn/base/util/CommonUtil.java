package com.cn.base.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * @author dengyu
 * @desc
 * @date 2018/04/16
 */
public final class CommonUtil {

    /**
     * 隐藏构造器
     */
    private CommonUtil() {

    }

    /**
     * @return String
     */
    public static String generateTicket() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("t_");
        String uuid = UUID.randomUUID().toString(); //获取UUID并转化为String对象
        uuid = uuid.replace("-", "");
        stringBuffer.append(uuid);
        return stringBuffer.toString();
    }

    /**
     * @return String
     */
    public static String generateShortUuid() {
        return RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    }

    /**
     * @return String
     */
    public static String generateNickname() {
        return "车主" + RandomStringUtils.random(6, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    }

    /**
     * @param current current
     * @param whole   whole
     * @return int
     */
    public static int getRate(int current, Integer whole) {
        NumberFormat nt = NumberFormat.getInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMaximumFractionDigits(0);
        return Integer.parseInt(nt.format((float) current / whole * 100));
    }

    /**
     * @param map map
     * @return String 形如 username'chenziwen^password'1234
     */
    public static String transMapToString(Map map) {
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("'").append(null == entry.getValue() ? ""
                    : entry.getValue().toString()).append(iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }

    /**
     * @param mapString mapString 形如 username'chenziwen^password'1234
     * @return Map
     */
    public static Map transStringToMap(String mapString) {
        Map map = new HashMap();
        StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null)) {
            items = new StringTokenizer(entrys.nextToken(), "'");
        }
        return map;
    }

    /**
     * @param viewCount            viewCount
     * @param answerCount          answerCount
     * @param lastActiveExpireTime lastActiveExpireTime
     * @param createExpireTime     createExpireTime
     * @return BigDecimal
     */
    public static BigDecimal getFValue(int viewCount, int answerCount, long lastActiveExpireTime, long createExpireTime) {
        if (createExpireTime == 0) {
            createExpireTime = 1;
        }
        return BigDecimal.valueOf((viewCount * 10 + answerCount * 100 + Math.pow(1.2, 48 - lastActiveExpireTime)) / Math.sqrt(createExpireTime));
    }

    /**
     * 判断一个对象是否为空。它支持如下对象类型：
     * <ul>
     * <li>null : 一定为空
     * <li>字符串     : ""为空,多个空格也为空
     * <li>数组
     * <li>集合
     * <li>Map
     * <li>其他对象 : 一定不为空
     * </ul>
     *
     * @param obj 任意对象
     * @return 是否为空
     */
    public static final boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if ("[]".equals(String.valueOf(obj))) {
            return true;
        }
        if (obj instanceof String || obj instanceof StringBuffer) {
            return "".equals(String.valueOf(obj).trim());
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map<?, ?>) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        return false;
    }

    /**
     * 是否有空
     *
     * @param objects objects
     * @return boolean
     */
    public static final boolean hasEmpty(final Object... objects) {
        for (Object object : objects) {
            if (isEmpty(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>unicode 解码<p>
     *
     * @param unicode unicode
     * @return originalString
     */
    public static String decodeUnicode(String unicode) {
        StringBuffer str = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            str.append((char) data);
        }
        return str.length() > 0 ? str.toString() : unicode;
    }


    /**
     * @param objects objects
     * @return StringBuilder
     */
    public static StringBuilder stringAppend(Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        if (objects != null) {
            int length = objects.length;
            for (int i = 0; i < length; i++) {
                stringBuilder.append(objects[i]);
            }
        }
        return stringBuilder;
    }

    /**
     * solr检索时，转换特殊字符
     *
     * @param s 需要转义的字符串
     * @return 返回转义后的字符串
     */
    public static String escapeQueryChars(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        //查询字符串一般不会太长，挨个遍历也花费不了多少时间
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
                    || c == ':' || c == '^' || c == '[' || c == ']' || c == '\"'
                    || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
                    || c == '|' || c == '&' || c == ';' || c == '/' || c == '.'
                    || c == '$' || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 清除xss攻击的转义方法
     *
     * @param value 字符串
     * @return 转义后的字符串
     */
    public static String cleanXSS(String value) {
        if (value == null) {
            return value;
        }
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }
}
