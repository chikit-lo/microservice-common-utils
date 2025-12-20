package io.github.chikitlo.common.validation.callback;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidatorContext;
import io.github.chikitlo.common.validation.core.ValidatorElement;
import io.github.chikitlo.common.validation.validator.Validator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * DefaultValidateCallback
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 01:07
 */
@Slf4j
public class DefaultValidateCallback implements ValidateCallBack {
    @Override
    public void onSuccess(ValidatorContext context, List<ValidatorElement> validatorElementList) {
        log.info("Validate successfully");
    }

    @Override
    public void onFail(ValidatorContext context, List<ValidatorElement> validatorElementList, List<ValidationError> errors) {
        log.error("Totally errors: {}", errors.size());
    }

    @Override
    public void onUncaughtException(ValidatorContext context, Validator validator, Exception e, Object target) throws Exception {
        log.error("Exception occurs: {}, validator: {}, target: {}", e.getMessage(), validator.getClass().getSimpleName(), target);
    }
}