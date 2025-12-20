package io.github.chikitlo.common.validation.core;

import lombok.Builder;
import lombok.Data;

/**
 * ValidationError
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 01:54
 */
@Builder
@Data
public class ValidationError {
    private String fieldName;
    private Object target;
    private int lineNumber;
    private String errorMsg;

    public static ValidationError create(String errorMsg) {
        return ValidationError.builder().errorMsg(errorMsg).build();
    }
}