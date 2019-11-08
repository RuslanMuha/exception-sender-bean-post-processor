package com.example.exceptionsenderspringbootstarter;

import com.example.exceptionsenderspringbootstarter.configuration.PropertiesResolver;
import com.example.exceptionsenderspringbootstarter.service.Sender;
import com.example.exceptionsenderspringbootstarter.annotation.SendException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExceptionSendingPostBeanProcessor implements BeanPostProcessor {

    @Autowired
    private Sender sender;
    @Autowired
    private List<PropertiesResolver> propertiesResolver;
    private Map<String, Class> map = new HashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(SendException.class)) {
            map.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = map.get(beanName);
        if (beanClass != null) {
            Annotation annotation = beanClass.getAnnotation(SendException.class);
            if (annotation instanceof SendException) {
                SendException sendException = (SendException) annotation;
                return getProxy(bean, beanClass, sendException.value());
            }
        }
        return bean;
    }

    private Object getProxy(Object bean, Class beanClass, Class<? extends Throwable>[] exceptionClasses) {
        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
            try {
                return method.invoke(bean, args);
            } catch (InvocationTargetException ex) {
                ifEqualsSend(exceptionClasses, ex.getCause());
                throw ex.getCause();
            }


        });
    }

    private void ifEqualsSend(Class<? extends Throwable>[] exceptionClasses, Throwable ex) {
        for (Class e : exceptionClasses) {
            if (e.getName().equals(ex.getClass().getName())) {
                propertiesResolver.forEach(prop -> {
                    if(prop.getSource().isPresent()){
                        List<String> mails = prop.getSource().get();
                        sender.send(mails, ex.getClass().getName());
                    }
                });

            }
        }
    }


}
