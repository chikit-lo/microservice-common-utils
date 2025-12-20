package io.github.chikitlo.common.validation.validator;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidatorContext;

/**
 * NumericFieldValidator
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 16:24
 */
public class NumericFieldValidator implements Validator<String> {
    private final String regex = "[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?";
    private RegexValidator regexValidator;

    public NumericFieldValidator() {
        regexValidator = new RegexValidator(regex, "Expected numeric value.");
    }

    @Override
    public boolean validate(ValidatorContext context, String validationObject, ValidationError errorInfo) {
        return regexValidator.validate(context, validationObject, errorInfo);
    }
}