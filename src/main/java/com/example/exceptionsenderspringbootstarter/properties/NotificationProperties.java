package com.example.exceptionsenderspringbootstarter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "exception")
public class NotificationProperties {
    private List<String> mails;
}
