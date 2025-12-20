package io.github.chikitlo.common.validation.validator;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * NotEmptyFieldValidator
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 16:15
 */
public class NotEmptyFieldValidator implements Validator<String> {
    private String message = "";

    public NotEmptyFieldValidator() {
    }

    public NotEmptyFieldValidator(String message) {
        this.message = message;
    }

    @Override
    public boolean validate(ValidatorContext context, String validationObject, ValidationError errorInfo) {
        if (StringUtils.isEmpty(validationObject)) {
            errorInfo.setErrorMsg(String.format("Line: %d, %s could not be empty. %s", errorInfo.getLineNumber(), errorInfo.getFieldName(), message));
            context.addError(errorInfo);

            return false;
        }
        return true;
    }
}