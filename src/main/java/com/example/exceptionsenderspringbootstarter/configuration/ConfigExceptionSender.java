package com.example.exceptionsenderspringbootstarter.configuration;

import com.example.exceptionsenderspringbootstarter.properties.ApplPropertiesResolver;
import com.example.exceptionsenderspringbootstarter.properties.ExceptionSenderConstant;
import com.example.exceptionsenderspringbootstarter.properties.FilePropertiesResolver;
import com.example.exceptionsenderspringbootstarter.properties.NotificationProperties;
import com.example.exceptionsenderspringbootstarter.service.EmailSender;
import com.example.exceptionsenderspringbootstarter.ExceptionSendingBeanPostProcessor;
import com.example.exceptionsenderspringbootstarter.service.Sender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import static com.example.exceptionsenderspringbootstarter.properties.ExceptionSenderConstant.APPLICATION_PROPERTIES_KEY;
import static com.example.exceptionsenderspringbootstarter.properties.ExceptionSenderConstant.SYSTEM_PROPERTIES_KEY;


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
    @ConditionalOnProperty(SYSTEM_PROPERTIES_KEY)
    public FilePropertiesResolver filePropertiesResolver() {
        return new FilePropertiesResolver();
    }

    @Bean
    @ConditionalOnProperty(APPLICATION_PROPERTIES_KEY)
    public ApplPropertiesResolver applicationPropResolver() {
        return new ApplPropertiesResolver();
    }

}
