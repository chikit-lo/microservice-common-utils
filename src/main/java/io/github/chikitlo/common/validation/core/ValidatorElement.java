package io.github.chikitlo.common.validation.core;

import io.github.chikitlo.common.validation.validator.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * ValidatorElement
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 02:42
 */
@Builder
@AllArgsConstructor
@Getter
@ToString
public class ValidatorElement {
    private String fieldName;
    private Object target;
    private int lineNumber;
    private Validator validator;
}