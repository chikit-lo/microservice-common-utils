package io.github.chikitlo.common.validation.validator;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

/**
 * NotNullFieldValidator
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 16:21
 */
public class NotNullFieldValidator implements Validator<Object> {
    private String message = "";

    public NotNullFieldValidator() {
    }

    public NotNullFieldValidator(String message) {
        this.message = message;
    }

    @Override
    public boolean validate(ValidatorContext context, Object validationObject, ValidationError errorInfo) {
        if (ObjectUtils.isEmpty(validationObject)) {
            errorInfo.setErrorMsg(String.format("Line: %d, %s could not be null. %s", errorInfo.getLineNumber(), errorInfo.getFieldName(), message));
            context.addError(errorInfo);

            return false;
        }

        return true;
    }
}