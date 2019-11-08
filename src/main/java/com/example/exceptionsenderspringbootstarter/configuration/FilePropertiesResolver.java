package com.example.exceptionsenderspringbootstarter.configuration;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.exceptionsenderspringbootstarter.configuration.Constant.KEY;

public class FilePropertiesResolver implements PropertiesResolver {

    @SneakyThrows
    @Override
    public Optional<List<String>> getSource() {

        String path = System.getProperty(KEY);
        return path != null ? Optional.of(Files.lines(Paths.get(path))
                .collect(Collectors.toList())):Optional.empty();

    }
}
