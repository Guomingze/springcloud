package com.example.login_production.utils;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * jedis-utils
 * 
 * @author NHX
 *
 */
@Component
public class JedisUtil {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
	
	
	/**
	 * Status-OK
	 */
	public final static String OK = "OK";

	/**
	 * 静态注入JedisPool连接池
	 * 本来是正常注入JedisUtil，可以在Controller和Service层使用，但是重写Shiro的CustomCache无法注入JedisUtil
	 * 现在改为静态注入JedisPool连接池，JedisUtil直接调用静态方法即可
	 * https://blog.csdn.net/W_Z_W_888/article/details/79979103
	 */
	private static JedisPool jedisPool;

//	@Autowired
//	public JedisUtil(JedisPool jedisPool) {
//	    JedisUtil.jedisPool = jedisPool;
//	}

	  /**
     * 初始化Redis连接池
     */
   static {
//		//redisURL 与 redisPort 的配置文件
//        String configFile = "production.properties";
//        if (PropKit.getBoolean("devMode")) {
//            configFile = "dev.properties";
//        }
 
        JedisPoolConfig config = new JedisPoolConfig();
		//设置最大连接数（100个足够用了，没必要设置太大）
		config.setMaxTotal(100);
		//最大空闲连接数
		config.setMaxIdle(10);
		//获取Jedis连接的最大等待时间（50秒） 
		config.setMaxWaitMillis(50 * 1000);
		//在获取Jedis连接时，自动检验连接是否可用
		config.setTestOnBorrow(true);
		//在将连接放回池中前，自动检验连接是否有效
		config.setTestOnReturn(true);
		//自动测试池中的空闲连接是否都是可用连接
		config.setTestWhileIdle(true);
		//创建连接池
		jedisPool = new JedisPool(config, "localhost",
					6379);

    }
	
	/**
	 * 获取Jedis实例
	 * 
	 * @param
	 * @return redis.clients.jedis.Jedis
	 * @author Wang926454
	 * @date 2018/9/4 15:47
	 */
	public static synchronized Jedis getJedis() {
		
		System.out.println("==============");
		System.out.println(jedisPool);
		System.out.println("==============");
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
//			throw new CustomException("获取Jedis资源异常:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 释放Jedis资源
	 * 
	 * @param
	 * @return void
	 * @author Wang926454
	 * @date 2018/9/5 9:16
	 */
	public static void closePool() {
		try {
			jedisPool.close();
		} catch (Exception e) {
//			throw new CustomException("释放Jedis资源异常:" + e.getMessage());
		}
	}

	/**
	 * 获取redis键值-object
	 * 
	 * @param key
	 * @return java.lang.Object
	 * @author Wang926454
	 * @date 2018/9/4 15:47
	 */
	public static Object getObject(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			byte[] bytes = jedis.get(key.getBytes());
			if (StringUtil.isNotNull(new String(bytes))) {
				return SerializableUtil.unserializable(bytes);
			}
		} catch (Exception e) {
//			throw new CustomException("获取Redis键值getObject方法异常:key=" + key + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/**
	 * 设置redis键值-object
	 * 
	 * @param key
	 * @param value
	 * @return java.lang.String
	 * @author Wang926454
	 * @date 2018/9/4 15:49
	 */
	public static String setObject(String key, Object value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.set(key.getBytes(), SerializableUtil.serializable(value));
		} catch (Exception e) {
			System.out.println(jedis);
			System.err.println("出错了");
			e.printStackTrace();
//			throw new CustomException(
//					"设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return key;
	}

	/**
	 * 设置redis键值-object-expiretime
	 * 
	 * @param key
	 * @param value
	 * @param expiretime
	 * @return java.lang.String
	 * @author Wang926454
	 * @date 2018/9/4 15:50
	 */
	public static String setObject(String key, Object value, int expiretime) {
		String result = "";
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.set(key.getBytes(), SerializableUtil.serializable(value));
			if (OK.equals(result)) {
				jedis.expire(key.getBytes(), expiretime);
			}
			return result;
		} catch (Exception e) {
//			throw new CustomException(
//					"设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return result;
	}

	/**
	 * 获取redis键值-Json
	 * 
	 * @param key
	 * @return java.lang.Object
	 * @author Wang926454
	 * @date 2018/9/4 15:47
	 */
	public static String getJson(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
//			throw new CustomException("获取Redis键值getJson方法异常:key=" + key + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return key;
	}

	/**
	 * 设置redis键值-Json
	 * 
	 * @param key
	 * @param value
	 * @return java.lang.String
	 * @author Wang926454
	 * @date 2018/9/4 15:49
	 */
	public static String setJson(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
//			throw new CustomException(
//					"设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return value;
	}

	/**
	 * 设置redis键值-Json-expiretime
	 * 
	 * @param key
	 * @param value
	 * @param expiretime
	 * @return java.lang.String
	 * @author Wang926454
	 * @date 2018/9/4 15:50
	 */
	public static String setJson(String key, String value, int expiretime) {
		String result = "";
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.set(key, value);
			if (OK.equals(result)) {
				jedis.expire(key, expiretime);
			}
			return result;
		} catch (Exception e) {
//			throw new CustomException(
//					"设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return result;
	}

	/**
	 * 删除key
	 * 
	 * @param key
	 * @return java.lang.Long
	 * @author Wang926454
	 * @date 2018/9/4 15:50
	 */
	public static Long delKey(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.del(key.getBytes());
		} catch (Exception e) {
//			throw new CustomException("删除Redis的键delKey方法异常:key=" + key + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/**
	 * key是否存在
	 * 
	 * @param key
	 * @return java.lang.Boolean
	 * @author Wang926454
	 * @date 2018/9/4 15:51
	 */
	public static Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key.getBytes());
		} catch (Exception e) {
//			throw new CustomException("查询Redis的键是否存在exists方法异常:key=" + key + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/**
	 * 模糊查询获取key集合
	 * 
	 * @param key
	 * @return java.util.Set<java.lang.String>
	 * @author Wang926454
	 * @date 2018/9/6 9:43
	 */
	public static Set<String> keysS(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(key);
		} catch (Exception e) {
//			throw new CustomException("模糊查询Redis的键集合keysS方法异常:key=" + key + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/**
	 * 模糊查询获取key集合
	 * 
	 * @param key
	 * @return java.util.Set<java.lang.String>
	 * @author Wang926454
	 * @date 2018/9/6 9:43
	 */
	public static Set<byte[]> keysB(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(key.getBytes());
		} catch (Exception e) {
//			throw new CustomException("模糊查询Redis的键集合keysB方法异常:key=" + key + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/**
	 * 获取过期剩余时间
	 * 
	 * @param key
	 * @return java.lang.String
	 * @author Wang926454
	 * @date 2018/9/11 16:26
	 */
	public static Long getExpireTime(String key) {
		Long result = -2L;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.ttl(key);
			return result;
		} catch (Exception e) {
//			throw new CustomException("获取Redis键过期剩余时间getExpireTime方法异常:key=" + key + " cause=" + e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return result;
	}
}
