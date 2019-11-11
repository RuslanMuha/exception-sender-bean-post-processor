package com.example.exceptionsenderspringbootstarter.properties;

import java.util.List;
import java.util.Optional;

public interface PropertiesResolver {
    Optional<List<String>> getSource();


}
