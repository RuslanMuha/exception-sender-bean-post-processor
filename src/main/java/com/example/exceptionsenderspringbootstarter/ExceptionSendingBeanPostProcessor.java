package com.example.exceptionsenderspringbootstarter;

import com.example.exceptionsenderspringbootstarter.properties.PropertiesResolver;
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


public class ExceptionSendingBeanPostProcessor implements BeanPostProcessor {
//BeanPostProcessor allows you to configure beans before they get into the container.
//The “Change Of Responsibility” pattern is involved here.

    @Autowired
    private Sender sender;
    @Autowired
    private List<PropertiesResolver> propertiesResolver;
    private final Map<String, Class> map = new HashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //sets up the object
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(SendException.class)) {
            // save the metadata of the original bean,
            // such as the BeanPostProcessor working according to the “Change Of Responsibility” pattern,
            // on the method "postProcessAfterInitialization"
            //we can already get the proxy, losing the original metadata of the bin
            map.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //changes the behavior of an object
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
        //Each proxy class has one public constructor that takes one argument, an implementation of the InvocationHandler interface.
        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
            // InvocationHandler can intercept all method calls that are accessed by the proxy object.
            try {
                return method.invoke(bean, args);

                //the value returned by the invoke method will become the return value of the method call in the proxy instance.
                //If the declared return value of the interface method is a primitive type,
                //then the return value of invoke must be an instance of the corresponding primitive wrapper class
            } catch (InvocationTargetException ex) {
                // InvocationTargetException is a checked exception,
                // which throws an exception thrown by the called method or constructor.

                // UndeclaredThrowableException instance contains undeclared checked exception
                // if the exception cannot be assigned to any of the types of exceptions declared in the throws clause of the method
                ifEqualsSend(exceptionClasses, ex.getCause());

                //so that no one notices ours intervention, we throw the original exception, which we caught back
                throw ex.getCause();

            }

        });
    }

    private void ifEqualsSend(Class<? extends Throwable>[] exceptionClasses, Throwable ex) {
        for (Class e : exceptionClasses) {
            if (e.getName().equals(ex.getClass().getName())) {
                propertiesResolver.forEach(prop -> {
                    if (prop.getSource().isPresent()) {
                        List<String> mails = prop.getSource().get();
                        sender.send(mails, ex.getClass().getName());
                    }
                });

            }
        }
    }


}
