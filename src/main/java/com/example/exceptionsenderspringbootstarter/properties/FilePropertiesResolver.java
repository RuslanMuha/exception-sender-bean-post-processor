package com.example.exceptionsenderspringbootstarter.properties;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.exceptionsenderspringbootstarter.properties.ExceptionSenderConstant.SYSTEM_PROPERTIES_KEY;

public class FilePropertiesResolver implements PropertiesResolver {

    @Autowired
    Environment env;

    @SneakyThrows
    @Override
    public List<String> getSource() {
        String path = env.getProperty(SYSTEM_PROPERTIES_KEY);
        return Files.lines(Paths.get(path))
                .collect(Collectors.toList());

    }
}
