package com.example.exceptionsenderspringbootstarter.properties;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ApplicationPropResolver implements PropertiesResolver {
    @Autowired
    private NotificationProperties notificationProperties;

    @Override
    public Optional<List<String>> getSource() {
        return Optional.of(notificationProperties.getMails());
    }
}
