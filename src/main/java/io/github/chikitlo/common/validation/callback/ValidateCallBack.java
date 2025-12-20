package io.github.chikitlo.common.validation.callback;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidatorContext;
import io.github.chikitlo.common.validation.core.ValidatorElement;
import io.github.chikitlo.common.validation.validator.Validator;

import java.util.List;

/**
 * ValidateCallBack
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 01:48
 */
public interface ValidateCallBack {
    void onSuccess(ValidatorContext context, List<ValidatorElement> validatorElementList);

    void onFail(ValidatorContext context, List<ValidatorElement> validatorElementList, List<ValidationError> errors);

    void onUncaughtException(ValidatorContext context, Validator validator, Exception e, Object target) throws Exception;
}