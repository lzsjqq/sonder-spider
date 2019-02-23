package com.xyc.spider.core.datasource;
import com.xyc.spider.core.base.ISonderSpiderConfig;
import com.xyc.spider.core.base.SpiderConstant;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import java.util.List;
import java.util.Map;

/**
 * @author Loger
 * Date: 2018-07-30
 * TIme: 23:54
 * Description :
 */
public class JedisDao {
    private static JedisPoolConfig config;
    private ISonderSpiderConfig spiderConfig;
    private static JedisPool jedisPool;
    private String password;
    private static JedisDao jedisDao;

    private JedisDao(ISonderSpiderConfig spiderConfig) {
        this.spiderConfig = spiderConfig;
        config = new JedisPoolConfig();
        config.setMaxIdle(spiderConfig.getInt(SpiderConstant.REDIS_MAX_IDLE, 100));
        config.setMaxWaitMillis(spiderConfig.getLong(SpiderConstant.REDIS_MAX_WAIT_MILLIS, 1000 * 10));
        password = spiderConfig.getString(SpiderConstant.REDIS_PWD);
        jedisPool = new JedisPool(config, spiderConfig.getString(SpiderConstant.REDIS_HOSTS), spiderConfig.getInt(SpiderConstant.REDIS_POST, 6379));
    }

    /**
     * 采用单例模式
     *
     * @param spiderConfig
     * @return
     */
    public static JedisDao getInstance(ISonderSpiderConfig spiderConfig) {
        if (jedisDao == null) {
            synchronized (JedisDao.class) {
                if (jedisDao == null) {
                    jedisDao = new JedisDao(spiderConfig);
                }
            }
        }
        return jedisDao;
    }

    /**
     * 获取jedis
     *
     * @return
     */
    private Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        if (StringUtils.isNotBlank(password)) {
            jedis.auth(password);
        }
        return jedis;
    }


    /**
     * jedis放回连接池
     *
     * @param jedis
     */
    public void close(Jedis jedis) {
        //从源码可以分析得到，如果是使用连接池的形式，这个并非真正的close,而是把连接放回连接池中
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = getJedis();
        String val = jedis.get(key);
        close(jedis);
        return val;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    /**
     * set with expire milliseconds
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public void set(String key, String value, long seconds) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            //* @param nxxx NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key
            //     *                     *          if it already exist.
            //     *                     * @param expx EX|PX, expire time units: EX = seconds; PX = milliseconds
            jedis.set(key, value, "NX", "EX", seconds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }


    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.incr(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hget(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    /**
     * @param timeout 0表示永久 单位秒
     * @param key     key
     * @return [key, value]
     */
    public String blpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            List<String> list = jedis.blpop(timeout, key);
            return list.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public String blpop(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            List<String> list = jedis.blpop(0, key);
            return list.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public void lpush(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.lpush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        }
    }

    /**
     * @param timeout 0表示永久 单位秒
     * @param key     key
     * @return [key, value]
     */
    public String brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            List<String> list = jedis.brpop(timeout, key);
            return list.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public String brpop(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            List<String> list = jedis.brpop(0, key);
            return list.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public void rpush(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.rpush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        }
    }

    /**
     * 获取key过期时间 -1表示永久 -2表示该key不存在
     *
     * @param key
     * @return
     */
    public long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

}
