package com.raven.appserver.config;

import com.raven.appserver.common.AuthFilter;
import com.raven.appserver.shiro.realm.LoginRealm;
import com.raven.appserver.shiro.util.CustomAtLeastOneSuccessfulStrategy;
import com.raven.appserver.shiro.util.RedisCacheSessionDao;
import com.raven.appserver.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.authc.credential.Sha256CredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @Author zxx
 * @Description Shiro配置类
 * @Date Created on 2017/11/10
 */
@Configuration
public class ShiroConfig {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("authFilter", new AuthFilter());
        shiroFilterFactoryBean.setFilters(filterChainDefinitionMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }


    private static final List<String> servicePaths = Arrays.asList("user", "group");

    private static Map<String, String> filterChainDefinitionMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        for (String serverPath : servicePaths) {
            filterChainDefinitionMap.put("/api/" + serverPath + "/**", "authFilter");
        }
        return filterChainDefinitionMap;
    }

    @Bean("securityManager")
    public SecurityManager getDefaultWebSecurityManager (
        ModularRealmAuthenticator authenticator, DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(authenticator);
        List<Realm> realms = new ArrayList<>();
        realms.add(getLoginRealm());
        securityManager.setRealms(realms);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public LoginRealm getLoginRealm() {
        LoginRealm loginRealm = new LoginRealm();
        loginRealm.setName("loginRealm");
        loginRealm.setCredentialsMatcher(getCredentialsMatcher());
        loginRealm.setAuthenticationCachingEnabled(true);
        loginRealm.setAuthenticationCacheName("authenticationCache");
        loginRealm.setAuthorizationCachingEnabled(true);
        loginRealm.setAuthorizationCacheName("authorizationCache");
        return loginRealm;
    }

    @Bean
    public RedisCacheSessionDao getRedisCacheSessionDao() {
        return new RedisCacheSessionDao();
    }

    @Bean
    public SimpleCookie getSimpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("SESSIONID");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    public ModularRealmAuthenticator getModularRealmAuthenticator() {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator
            .setAuthenticationStrategy(new CustomAtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     * 定时清理过期session
     */
    @Bean
    public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(900000);
        return scheduler;
    }

    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager (
        RedisCacheSessionDao sessionDAO, SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setGlobalSessionTimeout(Constants.GLOBAL_SESSION_TIMEOUT);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager
            .setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());
        return sessionManager;
    }

    /**
     * Shiro生命周期处理器 * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions), 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
        SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public Sha256CredentialsMatcher getCredentialsMatcher() {
        Sha256CredentialsMatcher sha256CredentialsMatcher = new Sha256CredentialsMatcher();
        sha256CredentialsMatcher.setHashAlgorithmName("SHA-1");
        return sha256CredentialsMatcher;
    }

    /**
     * cookie对象;
     */
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理对象;记住我功能
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }
}
