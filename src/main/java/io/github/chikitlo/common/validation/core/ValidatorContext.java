package io.github.chikitlo.common.validation.core;

import java.util.HashMap;
import java.util.Map;

/**
 * ValidatorContext
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 01:49
 */
public class ValidatorContext {
    private Map<String, Object> attributes;
    private ValidationResult result;

    public ValidatorContext() {

    }

    public ValidatorContext(Map<String, Object> attributes, ValidationResult result) {
        this.attributes = attributes;
        this.result = result;
    }

    public void addErrorMsg(String msg) {
        result.addError(ValidationError.create(msg));
    }

    public void addError(ValidationError validationError) {
        result.addError(validationError);
    }

    public Object getAttribute(String key) {
        if (attributes != null && !attributes.isEmpty()) {
            return attributes.get(key);
        }

        return null;
    }

    public <T> T getAttribute(String key, Class<T> clazz) {
        return (T) getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }

        attributes.put(key, value);
    }

    public ValidationResult getResult() {
        return result;
    }

    public void setResult(ValidationResult result) {
        this.result = result;
    }
}