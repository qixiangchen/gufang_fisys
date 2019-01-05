package com.gf.acl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class ShiroConfiguration {

    //将自己的验证方式加入容器
    @Bean
    public GfShiroRealm gfShiroRealm() {
    	GfShiroRealm gfShiroRealm = new GfShiroRealm();
    	gfShiroRealm.setCachingEnabled(false);
        return gfShiroRealm;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager()
    {
    	DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(gfShiroRealm());
//        DefaultWebSessionManager sm = createSessionManager();
//        List<SessionListener> listeners = new ArrayList<SessionListener>();
//        listeners.add(mySessionListener());
//        sm.setSessionListeners(listeners);
//        sm.setSessionDAO(sessionDAO());
//        securityManager.setSessionManager(sm);

        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager)
    {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String,String> map = new HashMap<String, String>();
		//登出
		map.put("/logout","logout");
		map.put("/logindone","anon");
		//对所有用户认证
		map.put("/*.action","authc");
		//过滤权限检查链接
		map.put("/static/**", "anon");
		map.put("/*.jsp", "anon");
		//登录
		shiroFilterFactoryBean.setLoginUrl("/");
		//首页
		shiroFilterFactoryBean.setSuccessUrl("/main.action");
		//错误页面，认证不通过跳转
		shiroFilterFactoryBean.setUnauthorizedUrl("/nopriv.jsp");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
    @Bean
	public HandlerExceptionResolver solver(){
		HandlerExceptionResolver handlerExceptionResolver=new ShiroExceptionResolver();
		return handlerExceptionResolver;
	}

//    @Bean
//    public DefaultWebSessionManager createSessionManager()
//    {
//    	DefaultWebSessionManager sm = new DefaultWebSessionManager();
//    	return sm;
//    }
//    
//    @Bean
//    public SessionListener mySessionListener()
//    {
//    	MySessionListener msl = new MySessionListener();
//    	return msl;
//    }
//
//    @Bean
//    public RedisManager redisManager() {
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost("192.168.25.30");
//        redisManager.setPort(6379);
//        redisManager.setExpire(1800);// 配置过期时间
//        System.out.println("配置redis连接设置##########");
//        return redisManager;
//    }
//    
//    public RedisCacheManager cacheManager() {
//        RedisCacheManager redisCacheManager = new RedisCacheManager();
//        redisCacheManager.setRedisManager(redisManager());
//        return redisCacheManager;
//    }
//
//    @Bean
//    public SessionDAO sessionDAO() {
//        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
//        redisSessionDAO.setRedisManager(redisManager());
//        System.out.println("设置redisSessionDAO");
//        return redisSessionDAO;
//    }

}
