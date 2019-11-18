package com.example.exceptionsenderspringbootstarter;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static com.example.exceptionsenderspringbootstarter.properties.ExceptionSenderConstant.*;

public class MailSourceCheckApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if (context.getEnvironment().getProperty(SYSTEM_PROPERTIES_KEY) == null &&
                context.getEnvironment().getProperty(APPLICATION_PROPERTIES_KEY) == null) {
            throw new IllegalStateException(NO_SOURCE);
        }

    }
}
