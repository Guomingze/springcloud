package com.example.login_production.shiro;

import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取RedisManager对象
 * @author NHX
 *
 */
@Component
public class MyRedisManager{
	
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	@Value("${spring.redis.expire}")
	private Integer expire;
	
	private RedisManager redisManager = null;

	public RedisManager getRedisManager() {
		if(redisManager == null) {
			synchronized (MyRedisManager.class) {
				if(redisManager == null) {					
					redisManager = new RedisManager();
				}
			}
		}
		redisManager.setHost(host);
		redisManager.setPort(port);
		redisManager.setExpire(expire);
		return redisManager;
	}
}
