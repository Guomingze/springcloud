package com.example.login_production.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.login_production.shiro.MyRedisManager;
import com.example.login_production.shiro.MyShiroRealm;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {
	
	@Autowired
	private MyRedisManager myRedisManager;

	/**
	 * Shiro配置Bean，引入依赖之后共需要做两件事，第一是配置，第二是自定义的realm
	 */

	@Bean
	public ShiroFilterFactoryBean shirFilter(DefaultWebSecurityManager securityManager) {
		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 设置login URL
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/LoginSuccess.html");
		// 未授权的页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/index.html");
		// 使静态资源 生效
		filterChainDefinitionMap.put("/static/**", "anon");
		// 设置登录的地址可以匿名访问，因为一开始没有用户验证
		filterChainDefinitionMap.put("/login", "anon");
		// 注册
		filterChainDefinitionMap.put("/signin", "anon");
		// 异常类
		filterChainDefinitionMap.put("/Exception.class", "anon");
		// 拦截信息
		filterChainDefinitionMap.put("/*.html", "authc");
		// 退出系统的过滤器
		filterChainDefinitionMap.put("/logout", "logout");
		// 现在资源的角色
		filterChainDefinitionMap.put("/admin.html", "roles[admin]");
		// filterChainDefinitionMap.put("/user.html", "roles[user]");
		// 最后
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/*
	 * 由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码;
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashAlgorithmName("md5");
		// 散列的次数，比如散列两次，相当于md5(md5(""));
		hashedCredentialsMatcher.setHashIterations(1024);
		return hashedCredentialsMatcher;
	}

	// 这里提供密码加密后的字符串可以放在数据库使用
	public static void main(String[] args) {
		String hashAlgorithmName = "MD5";
		String credentials = "123456";
		int hashIterations = 1024;
		Object obj = new SimpleHash(hashAlgorithmName, credentials, null, hashIterations);
		System.out.println(obj);
	}

	// 将自己的验证方式加入容器,否则是不生效的
	@Bean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());// 根据上面的主方法得到加密后的密码放到数据库中
		return myShiroRealm;
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 注入自定义的realm;
		securityManager.setRealm(myShiroRealm());

		// 注入缓存管理器;
		securityManager.setCacheManager(ehCacheManager());

		// 注入会话管理器
		securityManager.setSessionManager(sessionManager());

//        securityManager.setRememberMeManager(rememberMeManager());

		return securityManager;
	}

	/*
	 * 开启shiro aop注解支持 使用代理方式;所以需要开启代码支持; 加入注解的使用，不加入这个注解不生效
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * shiro缓存管理器;
	 * 需要注入对应的其它的实体类中-->安全管理器：securityManager可见securityManager是整个shiro的核心；
	 * 这个根据需要自己配置
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		System.out.println("ShiroConfiguration.getEhCacheManager()");
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return cacheManager;
	}

	/**
	 * 配置session监听
	 *
	 * @return
	 */

//    @Bean("sessionListener")
//    public ShiroSessionListener sessionListener() {
//        ShiroSessionListener sessionListener = new ShiroSessionListener();
//        return sessionListener;
//    }

	/**
	 * 配置会话ID生成器
	 *
	 * @return
	 */
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	/**
	 * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件 MemorySessionDAO 直接在内存中进行会话维护
	 * EnterpriseCacheSessionDAO
	 * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 *
	 * @return
	 */
	@Bean
	public SessionDAO sessionDAO() {
		EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
		// 使用ehCacheManager
		enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
		// 设置session缓存的名字 默认为 shiro-activeSessionCache
		enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		// sessionId生成器
		enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
		return enterpriseCacheSessionDAO;
	}

	// 添加bean

	/**
	 * 自定义sessionManager
	 *
	 * @return
	 */
	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
//		sessionManager.setSessionDAO(sessionDAO());
		sessionManager.setSessionDAO(redisSessionDAO());
		// 注册自定义session监听
//        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
//        listeners.add(new ShiroSessionListener());
//        sessionManager.setSessionListeners(listeners);
		return sessionManager;
	}

	/**
	 * cookie管理器;
	 *
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		// rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
		// KeyGenerator keygen = KeyGenerator.getInstance("AES");
		// SecretKey deskey = keygen.generateKey();
		// System.out.println(Base64.encodeToString(deskey.getEncoded()));
		byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
		cookieRememberMeManager.setCipherKey(cipherKey);
		cookieRememberMeManager.setCookie(sessionIdCookie());
		return cookieRememberMeManager;
	}

	/**
	 * 配置保存sessionId的cookie 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理
	 * 也需要自己的cookie
	 *
	 * @return
	 */
	@Bean("sessionIdCookie")
	public SimpleCookie sessionIdCookie() {
		// 这个参数是cookie的名称
		SimpleCookie simpleCookie = new SimpleCookie("sid");
		// setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

		// setcookie()的第七个参数
		// 设为true后，只能通过http访问，javascript无法访问
		// 防止xss读取cookie
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		// maxAge=-1表示浏览器关闭时失效此Cookie
		simpleCookie.setMaxAge(10);
		return simpleCookie;
	}

	/**
	 * FormAuthenticationFilter 过滤器 过滤记住我
	 * 
	 * @return
	 */
//    @Bean
//    public FormAuthenticationFilter formAuthenticationFilter(){
//        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
//        //对应前端的checkbox的name = rememberMe
//        formAuthenticationFilter.setRememberMeParam("id");
//        return formAuthenticationFilter;
//    }

	@Bean
	public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator1() {

		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setUsePrefix(true);

		return defaultAdvisorAutoProxyCreator;
	}

	/**
	 * cacheManager 缓存 redis实现 使用的是shiro-redis开源插件
	 *
	 * @return
	 */
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}

	/**
	 * 配置shiro redisManager 使用的是shiro-redis开源插件
	 *
	 * @return
	 */
	public RedisManager redisManager() {
		return myRedisManager.getRedisManager();
	}

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis 使用的是shiro-redis开源插件
	 */
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}

}
