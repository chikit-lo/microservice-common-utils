package io.github.chikitlo.common.validation.result;

import io.github.chikitlo.common.validation.core.ValidationResult;

/**
 * ResultCollector
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 16:10
 */
public interface ResultCollector<T> {
    T toResult(ValidationResult result);
}