package com.example.exceptionsenderspringbootstarter.properties;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ApplPropertiesResolver implements PropertiesResolver {
    @Autowired
    private NotificationProperties notificationProperties;

    @Override
    public List<String> getSource() {
        return notificationProperties.getMails();
    }
}
