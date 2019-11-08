package com.example.exceptionsenderspringbootstarter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "exception")
public class NotificationProperties {
    private List<String> mails;
}
