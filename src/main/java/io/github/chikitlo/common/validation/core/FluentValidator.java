package io.github.chikitlo.common.validation.core;

import io.github.chikitlo.common.validation.callback.DefaultValidateCallback;
import io.github.chikitlo.common.validation.callback.ValidateCallBack;
import io.github.chikitlo.common.validation.result.ResultCollector;
import io.github.chikitlo.common.validation.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * FluentValidator
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 16:56
 */
@Slf4j
public class FluentValidator {
    // Target element list.
    private List<ValidatorElement> validatorElementList;

    // Shared context during one validation.
    private ValidatorContext validatorContext;

    // Internal validation result.
    private ValidationResult validationResult;

    // Once validate error occurs, skip the rest elements or not. Default is false.
    private boolean isFailFast = false;

    // Default validation callback function.
    private ValidateCallBack defaultValidateCallBack = new DefaultValidateCallback();

    private FluentValidator() {
        validatorElementList = new LinkedList<>();
        validatorContext = new ValidatorContext();
        validationResult = new ValidationResult();
        validatorContext.setResult(validationResult);
    }

    public static FluentValidator newInstance() {
        return new FluentValidator();
    }

    /**
     * Put attributes to context, can be accessed during the whole validation process.
     *
     * @param key
     * @param value
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:05
     */
    public FluentValidator putAttributeToContext(String key, Object value) {
        validatorContext.setAttribute(key, value);
        return this;
    }

    /**
     * Set external context to current validation.
     *
     * @param context
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:07
     */
    public FluentValidator withContext(ValidatorContext context) {
        this.validatorContext = context;
        this.validationResult = context.getResult();
        return this;
    }

    /**
     * Fail fast: stop on first failure.
     *
     * @param
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:09
     */
    public FluentValidator failFast() {
        this.isFailFast = true;
        return this;
    }

    /**
     * Fail over: validate all elements.
     *
     * @param
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:10
     */
    public FluentValidator failOver() {
        this.isFailFast = false;
        return this;
    }

    /**
     * Add one validator element.
     *
     * @param validatorElement
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:15
     */
    public FluentValidator on(ValidatorElement validatorElement) {
        doAdd(validatorElement);
        return this;
    }

    /**
     * Add multiple validator elements.
     *
     * @param elements
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/20 14:07
     */
    public FluentValidator on(List<ValidatorElement> elements) {
        if (ObjectUtils.isNotEmpty(elements)) {
            validatorElementList.addAll(elements);
        }
        return this;
    }

    /**
     * Add validator element.
     *
     * @param fieldName
     * @param target
     * @param lineNumber
     * @param validator
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:16
     */
    public <T> FluentValidator on(String fieldName, T target, int lineNumber, Validator<T> validator) {
        doAdd(new ValidatorElement(fieldName, target, lineNumber, validator));
        return this;
    }

    /**
     * Validate all elements with default callback function.
     *
     * @param
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:27
     */
    public FluentValidator doValidate() {
        return doValidate(defaultValidateCallBack);
    }

    /**
     * Validate all elements with specific callback function.
     *
     * @param validateCallBack
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:26
     */
    public FluentValidator doValidate(ValidateCallBack validateCallBack) {
        if (validatorElementList.isEmpty()) {
            log.info("No elements to validate");
            return this;
        }

        log.info("Start validation, total elements: {}", validatorElementList.size());
        long startTime = System.currentTimeMillis();
        int validatedCount = 0;
        try {
            for (ValidatorElement element : validatorElementList) {
                Validator validator = element.getValidator();
                Object target = element.getTarget();
                ValidationError errorInfo = ValidationError.builder()
                        .fieldName(element.getFieldName())
                        .target(target)
                        .lineNumber(element.getLineNumber())
                        .build();

                try {
                    validatedCount++;
                    if (!validator.validate(validatorContext, target, errorInfo)) {
                        validationResult.setSuccess(false);

                        if (isFailFast) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    validator.onExeception(e, validatorContext, target);

                    try {
                        validateCallBack.onUncaughtException(validatorContext, validator, e, target);
                    } catch (Exception ex) {
                        throw new ValidationException(ex);
                    }
                }
            }

            if (validationResult.hasErrors()) {
                validateCallBack.onSuccess(validatorContext, validatorElementList);
            } else {
                validateCallBack.onFail(validatorContext, validatorElementList, validationResult.getErrors());
            }
        } finally {
            int elapsedTime = (int) (System.currentTimeMillis() - startTime);
            log.info("End validation, validated={}, costing {}ms with isSuccess={}", validatedCount, elapsedTime, validationResult.isSuccess());
            validationResult.setTimeElapsed(elapsedTime);
        }

        return this;
    }

    /**
     * Get validation results.
     *
     * @param resultCollector
     * @return T
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:28
     */
    public <T> T result(ResultCollector<T> resultCollector) {
        return resultCollector.toResult(validationResult);
    }

    /**
     * Set fail fast param.
     *
     * @param isFailFast
     * @return io.github.chikitlo.common.validation.core.FluentValidator
     * @throws
     * @author Jack Lo
     * @date 2025/12/13 17:29
     */
    public FluentValidator setIsFailFast(boolean isFailFast) {
        this.isFailFast = isFailFast;
        return this;
    }

    private void doAdd(ValidatorElement element) {
        validatorElementList.add(element);
    }

    static class ValidationException extends RuntimeException {
        public ValidationException(Throwable cause) {
            super(cause);
        }

        public ValidationException(String message) {
            super(message);
        }

        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}