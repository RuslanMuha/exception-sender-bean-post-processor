package com.example.exceptionsenderspringbootstarter.configuration;

import com.example.exceptionsenderspringbootstarter.service.EmailSender;
import com.example.exceptionsenderspringbootstarter.ExceptionSendingPostBeanProcessor;
import com.example.exceptionsenderspringbootstarter.service.Sender;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(NotificationProperties.class)
public class ConfigExceptionSender {


    @Bean
    public ExceptionSendingPostBeanProcessor exceptionHandlingPostBeanProcessor() {
        return new ExceptionSendingPostBeanProcessor();
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
    public ApplicationPropResolver applicationPropResolver() {
        return new ApplicationPropResolver();
    }

}
