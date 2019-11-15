package com.example.exceptionsenderspringbootstarter.configuration;

import com.example.exceptionsenderspringbootstarter.properties.ApplPropertiesResolver;
import com.example.exceptionsenderspringbootstarter.properties.FilePropertiesResolver;
import com.example.exceptionsenderspringbootstarter.properties.NotificationProperties;
import com.example.exceptionsenderspringbootstarter.service.EmailSender;
import com.example.exceptionsenderspringbootstarter.ExceptionSendingBeanPostProcessor;
import com.example.exceptionsenderspringbootstarter.service.Sender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(NotificationProperties.class)
public class ConfigExceptionSender {


    @Bean
    public ExceptionSendingBeanPostProcessor exceptionHandlingPostBeanProcessor() {
        return new ExceptionSendingBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public Sender emailSender() {
        return new EmailSender();
    }

    @Bean
    public FilePropertiesResolver filePropertiesResolver() {
        return new FilePropertiesResolver();
    }

    @Bean
    @ConditionalOnProperty(value = "exception.mails")
    public ApplPropertiesResolver applicationPropResolver() {
        return new ApplPropertiesResolver();
    }

}
