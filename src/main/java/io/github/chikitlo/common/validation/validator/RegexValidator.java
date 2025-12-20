package io.github.chikitlo.common.validation.validator;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * RegexValidator
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 16:28
 */
public class RegexValidator implements Validator<String> {
    private String message = "";
    private String regex = "";

    public RegexValidator() {
    }

    public RegexValidator(String regex) {
        this.regex = regex;
    }

    public RegexValidator(String regex, String message) {
        this.regex = regex;
        this.message = message;
    }

    @Override
    public boolean validate(ValidatorContext context, String validationObject, ValidationError errorInfo) {
        if (StringUtils.isNotEmpty(validationObject) && !Pattern.compile(regex).matcher(validationObject).matches()) {
            errorInfo.setErrorMsg(String.format("Line: %d, %s could not match, actual value: %s. %s", errorInfo.getLineNumber(), errorInfo.getFieldName(), validationObject, message));
            context.addError(errorInfo);

            return false;
        }

        return true;
    }
}