package com.example.exceptionsenderspringbootstarter.configuration;

import java.util.List;
import java.util.Optional;

public interface PropertiesResolver {
    Optional<List<String>> getSource();

}
