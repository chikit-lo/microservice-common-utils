package io.github.chikitlo.common.validation.validator;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidatorContext;

/**
 * Validator
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 02:43
 */
@FunctionalInterface
public interface Validator<T> {
    boolean validate(ValidatorContext context, T validationObject, ValidationError errorInfo);

    default void onExeception(Exception exception, ValidatorContext context, T validationObject) {

    }
}