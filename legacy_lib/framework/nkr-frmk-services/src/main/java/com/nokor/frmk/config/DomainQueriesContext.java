package com.nokor.frmk.config;


import java.lang.reflect.Proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nokor.frmk.annotation.QueryResourceInvocationHandler;

/**
 * 
 * @author prasnar
 *
 */
@Configuration
public class DomainQueriesContext {
    @Bean
    public QueryResourceInvocationHandler queryInvocationHandler() {
        return new QueryResourceInvocationHandler();
    }


    @Bean
    public SettingQueries settingQueries() {
        return getProxy(SettingQueries.class);
    }

    private <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, queryInvocationHandler());
    }
}
