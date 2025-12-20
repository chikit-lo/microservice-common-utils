package io.github.chikitlo.common.validation.result;

import io.github.chikitlo.common.validation.core.ValidationError;
import io.github.chikitlo.common.validation.core.ValidationResult;

/**
 * ResultCollectors
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 16:11
 */
public class ResultCollectors {
    static class SimpleResultCollectorImpl implements ResultCollector<Result> {
        @Override
        public Result toResult(ValidationResult result) {
            Result ret = new Result();
            if (result.isSuccess()) {
                ret.setSuccess(true);
            } else {
                ret.setSuccess(false);
                ret.setErrors(result.getErrors().stream().map(ValidationError::getErrorMsg).toList());
            }

            return ret;
        }

        public static ResultCollector<Result> toSimple() {
            return new SimpleResultCollectorImpl();
        }
    }
}