package com.cn.base.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.cn.base.util.CommonUtil;
import com.cn.base.util.EntityUtil;
import com.cn.base.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author dengyu
 * @desc
 * @date 2018/5/12
 */
@Component
public class RedisDao {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private SetOperations<String, Object> setOps;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> hashOps;

    /**
     * 删除key值的所有数据，包括列表，键值对，hash等
     *
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除key值的所有数据，包括列表，键值对，hash等
     *
     * @param key key
     */
    public void deletePrefix(String key) {
        Set<String> keys = redisTemplate.keys(CommonUtil.stringAppend(key, "*").toString());
        redisTemplate.delete(keys);
    }


    /**
     * @param keys keys
     */
    public void deleteKeys(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 设置key,value对
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     */
    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * @param key   key
     * @param value value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key   key
     * @param value value
     * @return boolean
     */
    public boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * redis incr的使用，将键的整数值增加
     *
     * @param key  key
     * @param step step
     * @return 增加数
     */
    public long setIncr(final String key, long step) {
        return redisTemplate.opsForValue().increment(key, step);
    }

    /**
     * 根据key获取value
     *
     * @param key key
     * @return value
     */
    public String get(final String key) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * @param key key
     * @param obj obj
     * @return push数量
     * @throws JsonProcessingException JsonProcessingException
     */
    public long lpush(final String key, Object obj) throws JsonProcessingException {
        final String value = JSONUtil.toJson(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    /**
     * @param key   key
     * @param value value
     * @param size  size
     * @return 是否成功
     */
    public boolean zAddAndTrim(final String key, final Integer value, final int size) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                Boolean result = connection.zAdd(
                        serializer.serialize(key), value, serializer.serialize(String.valueOf(value)));
                connection.zRemRange(serializer.serialize(key), 0, -size - 1);
                return result;
            }
        });
    }


    /**
     * 获取key
     *
     * @param key key
     * @return keys
     */
    public Set<String> getKeys(final String key) {
        return redisTemplate.keys(key);
    }


    /**
     * 增加value
     *
     * @param key key
     * @return key
     */
    public long getIncrValue(final String key) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] rowkey = serializer.serialize(key);
                byte[] rowval = connection.get(rowkey);
                try {
                    String val = serializer.deserialize(rowval);
                    return Long.parseLong(val);
                } catch (Exception e) {
                    return 0L;
                }
            }
        });
    }

    /**
     * 设置失效时间
     *
     * @param key     key
     * @param timeout timeout
     * @param unit    单位
     */
    public void expire(final String key, final long timeout, final TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置失效时间
     *
     * @param key     key
     * @param timeout timeout
     */
    public void expire(final String key, final long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 向列表头部添加值
     *
     * @param key key
     * @param obj obj
     */
    public void addListFromHeader(String key, Object obj) {
        redisTemplate.opsForList().leftPush(key, obj);
    }

    /**
     * 添加列表
     *
     * @param key   key
     * @param value value
     */
    public void addList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 获取列表数据
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 列表数据
     */
    public List<Object> listPop(String key, int start, int end) {
        List<Object> objs = redisTemplate.opsForList().range(key, start, end);
        return objs;
    }

    /**
     * 获取列表长度
     *
     * @param key key
     * @return size
     */
    public Long getListLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取列表最后一位值,并删除
     *
     * @param key key
     * @return 最后一个对象
     */
    public Object popListLast(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 删除列表某一位值
     *
     * @param key   key
     * @param i     index
     * @param value value
     * @return 不知道
     */
    public Long delList(String key, long i, Object value) {
        return redisTemplate.opsForList().remove(key, i, value);
    }

    /**
     * 获取列表第一位，并删除
     *
     * @param key key
     * @return 第一位的对象
     */
    public Object popListFirst(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 设置某一位值
     *
     * @param key   key
     * @param index index
     * @param value value
     */
    public void setListByIndex(String key, int index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 向列表尾部追加一组数据
     *
     * @param key    key
     * @param values values
     */
    public void addListBundle(String key, List<Object> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向列表头部追加一组数据
     *
     * @param key    key
     * @param values values
     */
    public void addListBundleFromHeader(String key, List<Object> values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 获取列表某一位值
     *
     * @param key   key
     * @param index index
     * @return Object
     */
    public Object getListByIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 功能描述: (Set集合)将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。<br>
     * 〈功能详细描述〉
     *
     * @param key    key
     * @param values values
     * @since [产品/模块版本](可选)
     */
    public void addSet(String key, Object... values) {
        setOps.add(key, values);

    }

    /**
     * @param key   key
     * @param count 随机数
     * @return 列表
     */
    public Set<Object> randomSetDistinct(String key, long count) {
        return setOps.distinctRandomMembers(key, count);

    }

    /**
     * 功能描述:(Set集合)移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 <br>
     * 〈功能详细描述〉
     *
     * @param key    key
     * @param values values
     * @since [产品/模块版本](可选)
     */
    public void removeSet(String key, Object... values) {
        setOps.remove(key, values);
    }

    /**
     * 功能描述:(Set集合)移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 <br>
     * 〈功能详细描述〉
     *
     * @param key   key
     * @param value value
     * @since [产品/模块版本](可选)
     */
    public void removeList(String key, Object value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }

    /**
     * 功能描述:(Set集合)移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 <br>
     * 〈功能详细描述〉
     *
     * @param key  key
     * @param size values
     * @since [产品/模块版本](可选)
     */
    public List<Object> popSet(String key, long size) {
        List<Object> result = Lists.newArrayList();
        if (isExist(key)) {
            for (int i = 0; i < size; i++) {
                Object pop = redisTemplate.opsForSet().pop(key);
                if (pop != null) {
                    result.add(pop);
                } else {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 功能描述:(Set集合)返回集合 key 中的所有成员 <br>
     * 〈功能详细描述〉
     *
     * @param key key
     * @return key 中的所有成员
     * @since [产品/模块版本](可选)
     */
    public Set<Object> getMembers(String key) {
        Set<Object> members = setOps.members(key);
        return members;
    }

    /**
     * 功能描述:(Set集合)返回集合 key 中的所有成员 <br>
     * 〈功能详细描述〉
     *
     * @param key key
     * @return key 中的所有成员
     * @since [产品/模块版本](可选)
     */
    public Set<Object> getZsetMembers(String key) {
        Set<Object> members = redisTemplate.opsForZSet().reverseRange(key, 0, -1);
        return members;
    }

    /**
     * set Hash列表值(String,Long)
     *
     * @param key key
     * @param m   m
     */
    public void setAllHash(String key, Map<String, Object> m) {
        hashOps.putAll(key, m);
    }

    /**
     * set Object对象到hash表,字段必须为基本数据类型或包装类,已经时间和String类型
     *
     * @param key key
     * @param obj obj
     */
    public void setObjectToHash(String key, Object obj) {
        Map<String, String> stringMap = EntityUtil.objectToHash(obj);
        hashOps.putAll(key, stringMap);
    }

    /**
     * get Object对象到 根据hashKey;
     *
     * @param key key
     * @param t   t
     * @param <T> 类型
     * @return 对应的对象
     */
    public <T> T getHashToObjectBykey(String key, Class t) {
        Map<String, Object> entries = hashOps.entries(key);
        return EntityUtil.hashToObject(entries, t);

    }

    /**
     * get Hash列表值
     *
     * @param key key
     * @return Hash列表值
     */
    public Map<String, Object> getAllHash(String key) {
        return hashOps.entries(key);
    }

    /**
     * * 判断某个key是否存在
     *
     * @param key key
     * @return 是否存在
     */
    public Boolean isExist(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * * 向hash指定key加一
     *
     * @param key     key
     * @param hashKey hashKey
     * @param i       i
     */
    public long incrHash(String key, String hashKey, Integer i) {
        return hashOps.increment(key, hashKey, i);
    }

    /**
     * 增加hash字段
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public void putHashKey(String key, String hashKey, Object value) {
        hashOps.put(key, hashKey, value);
    }

    /**
     * 检测key是否存在
     *
     * @param key     key
     * @param hashKey hashKey
     * @return 是否存在
     */
    public boolean isHashKeyExist(String key, String hashKey) {
        return hashOps.hasKey(key, hashKey);
    }

    /**
     * 获取hash字段
     *
     * @param key     key
     * @param hashKey hashKey
     * @return hash中的值
     */
    public Object getHashKey(String key, String hashKey) {
        return hashOps.get(key, hashKey);
    }

    /**
     * 删除hash字段
     *
     * @param key     key
     * @param hashKey hashKey
     */
    public void delHashKey(String key, String hashKey) {
        hashOps.delete(key, hashKey);
    }

    /**
     * 批量获取值
     *
     * @param key      key
     * @param hashKeys hashKey
     * @return 值列表
     */
    public List<Object> multiGetHashKey(String key, List<String> hashKeys) {
        return hashOps.multiGet(key, hashKeys);
    }


    /**
     * 当 key 不存在时，返回 -2 。 当 code 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 code 的剩余生存时间
     *
     * @param key key
     * @return 返回 -2 。 当 code 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 code 的剩余生存时间
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }


    /**
     * 给指定的集合添加 字段和分数
     *
     * @param key   key
     * @param var   var
     * @param score score
     */
    public void addZset(String key, Object var, double score) {
        redisTemplate.opsForZSet().add(key, var, score);
    }


    /**
     * 批量添加, 实现了ZSetOperations.TypedTuple<>接口的的set集合 对象内要有getVule,和getSorce方法;
     *
     * @param key  key
     * @param var2 var2
     */
    public void addZset(String key, Set<ZSetOperations.TypedTuple<Object>> var2) {
        redisTemplate.opsForZSet().add(key, var2);
    }

    /**
     * 给指定的集合移除字段
     *
     * @param key key
     * @param var var
     */
    public void removeZset(String key, Object var) {
        redisTemplate.opsForZSet().remove(key, var);
    }


    /**
     * Redis Zscore 命令返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
     *
     * @param key key
     * @param var var
     */
    public Double zScore(String key, Object var) {
        return redisTemplate.opsForZSet().score(key, var);
    }

    /**
     * 增加score
     *
     * @param key   key
     * @param var   var
     * @param score 分类
     */
    public void incrZset(String key, Object var, double score) {
        redisTemplate.opsForZSet().incrementScore(key, var, score);
    }

    /**
     * 取范围
     *
     * @param key   key
     * @param start 分数值
     * @param stop  分数值
     * @return 排序值
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long stop) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, stop);
    }

    /**
     * 获取排序后的结果
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return Set
     */
    public Set<Object> reverseRange(final String key, final long start, final long end) {
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(key, start, end);
        return objects;
    }

    /**
     * @param key key
     * @param clz clz
     * @param <T> 泛型
     * @return 泛型
     * @throws IOException IOException
     */
    public <T> T getObject(String key, Class<T> clz) throws IOException {
        Object result = redisTemplate.opsForValue().get(key);
        if (result != null) {
            return JSONUtil.toBean(String.valueOf(result), clz);
        }
        return null;
    }

    /**
     * @param key          key
     * @param valueTypeRef valueTypeRef
     * @param <T>          泛型
     * @return 泛型
     * @throws IOException IOException
     */
    public <T> T getObject(String key, TypeReference valueTypeRef) throws IOException {
        Object result = redisTemplate.opsForValue().get(key);
        if (result != null) {
            return JSONUtil.toBean(String.valueOf(result), valueTypeRef);
        }
        return null;
    }

    /**
     * @param key key
     * @param obj obj
     * @throws IOException IOException
     */
    public void setObject(String key, Object obj) throws IOException {
        set(key, JSONUtil.toJson(obj));
    }

    /**
     * @param key    key
     * @param obj    obj
     * @param expire expire
     * @throws IOException IOException
     */
    public void setObjectAndExpire(String key, Object obj, long expire) throws IOException {
        set(key, JSONUtil.toJson(obj), expire);
    }

    /**
     * 原子查询修改
     *
     * @param key      key
     * @param value    value
     * @param maxValue maxValue
     * @param expire   缓存失效时间
     * @return 增加的值
     */
    public Integer getAndAdd(final String key, final Integer value, final Integer maxValue, final Long expire) {
        return redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                int result;
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] bytes = connection.get(serializer.serialize(key));
                String val = serializer.deserialize(bytes);
                if (StringUtils.isNotBlank(val)) {
                    int i = Integer.parseInt(val);
                    if (i >= maxValue) {
                        result = 0;
                    } else {
                        result = i + value > maxValue ? maxValue - i : value;
                    }
                } else {
                    result = value;
                }
                connection.set(serializer.serialize(key), serializer.serialize(String.valueOf(value)));
                connection.expire(serializer.serialize(key), expire);
                return result;
            }
        });
    }


    /**
     * 原子查询修改
     *
     * @param key      key
     * @param subKey   subKey
     * @param value    value
     * @param maxValue maxValue
     * @param expire   缓存失效时间
     * @return 增加的值
     */
    public Integer hGetAndAdd(
            final String key, final String subKey, final Integer value, final Integer maxValue, final Long expire) {
        return redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                int result;
                // connection.openPipeline();
                try {
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    byte[] bytes = connection.hGet(serializer.serialize(key), serializer.serialize(subKey));
                    String val = serializer.deserialize(bytes);
                    int i = 0;
                    if (StringUtils.isNotBlank(val)) {
                        i = Integer.parseInt(val);
                        if (i >= maxValue) {
                            result = 0;
                        } else {
                            result = i + value > maxValue ? maxValue - i : value;
                        }
                    } else {
                        result = value;
                    }
                    connection.hSet(serializer.serialize(key),
                            serializer.serialize(subKey),
                            serializer.serialize(String.valueOf(i + result)));
                    connection.expire(serializer.serialize(key), expire);
                } finally {
                    // connection.closePipeline();

                }
                return result;
            }
        });
    }

    /**
     * 功能描述:setNx
     *
     * @param key key
     * @return 是否设置成功，成功说明不存在这个key
     */
    public Boolean setNx(String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.setNX(serializer.serialize(key), serializer.serialize(key));
            }
        });
    }


    /**
     * 功能描述:hSetNx
     *
     * @param key    key
     * @param subKey subKey
     * @return 是否设置成功，成功说明不存在这个key
     */
    public Boolean hSetNx(String key, String subKey) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.hSetNX(
                        serializer.serialize(key),
                        serializer.serialize(subKey),
                        serializer.serialize(subKey));
            }
        });
    }


    /**
     * 删除hash 中的键值对
     *
     * @param key    key
     * @param subKey subKey
     */
    public void hDelete(String key, String... subKey) {
        redisTemplate.opsForHash().delete(key, subKey);
    }


    /**
     * HyperLogLog  添加用户数
     *
     * @param key   key
     * @param value value
     */
    public void pfadd(String key, String value) {
        redisTemplate.opsForHyperLogLog().add(key, value);
    }

    /**
     * HyperLogLog  统计用户数
     *
     * @param key key
     * @return 用户数
     */
    public Long pfcount(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }
}
