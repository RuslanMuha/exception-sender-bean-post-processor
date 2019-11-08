package com.example.exceptionsenderspringbootstarter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.exceptionsenderspringbootstarter.Constant.*;


public class ExceptionSendingPostBeanProcessor implements BeanPostProcessor {

    @Autowired
    private Sender sender;
    @Autowired
    private NotificationProperties notificationProperties;
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
                Optional<List<String>> mailsFromPath = getPath();
                if(!mailsFromPath.isPresent() && notificationProperties.getMails()==null){
                    throw new IllegalArgumentException(NO_RECIPIENT);
                }
                    List<String> mails = mailsFromPath.orElseGet(() -> notificationProperties.getMails());
                    sender.send(mails, ex.getClass().getName());
            }
        }
    }

    private Optional<List<String>> getPath() {
        String path = System.getProperty(KEY);
        if (path != null) {
            try {
                return Optional.of(Files.lines(Paths.get(path)).collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

}
