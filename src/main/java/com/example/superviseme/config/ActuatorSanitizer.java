package com.example.superviseme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.SanitizableData;
import org.springframework.boot.actuate.endpoint.SanitizingFunction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class ActuatorSanitizer implements SanitizingFunction {

    private static final String[] REGEX_PARTS = {"*", "$", "^", "+"};
    private static final Set<String> DEFAULT_KEYS_TO_SANITIZE = Set.of("path", "username", "password", "home");
    private final List<Pattern> keysToSanitize = new ArrayList<>();

    private ActuatorSanitizer(@Value("${management.enpoint.additionalKeysToSanitize:}") List<String> additionalKeys){
        additionalKeysToSanitize(DEFAULT_KEYS_TO_SANITIZE);
        additionalKeysToSanitize(additionalKeys);
    }

    private void additionalKeysToSanitize(Collection<String> additionalKeys) {
        for(String key:additionalKeys){
            this.keysToSanitize.add(getPattern(key));
        }
    }

    private Pattern getPattern(String value) {
        if(isRegex(value)){
            return Pattern.compile(value, Pattern.CASE_INSENSITIVE);
        }
        return Pattern.compile(".*" + value + "$", Pattern.CASE_INSENSITIVE);
    }

    private boolean isRegex(String key) {
        for(String part:REGEX_PARTS){
            if(key.contains(part)){
                return true;
            }
        }
        return false;
    }

    @Override
    public SanitizableData apply(SanitizableData data) {
        if(data.getValue()==null){
            return data;
        }
        for (Pattern pattern: keysToSanitize){
            if(pattern.matcher(data.getKey()).matches()){
                return data.withValue(SanitizableData.SANITIZED_VALUE);
            }
        }
        return data;
    }
}

