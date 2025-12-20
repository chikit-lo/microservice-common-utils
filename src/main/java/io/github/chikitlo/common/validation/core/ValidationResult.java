package io.github.chikitlo.common.validation.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ValidationResult
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 01:52
 */
@Data
public class ValidationResult {
    private boolean isSuccess = true;
    private List<ValidationError> errors = new ArrayList<>();
    private int timeElapsed;

    public void addError(ValidationError error) {
        errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}