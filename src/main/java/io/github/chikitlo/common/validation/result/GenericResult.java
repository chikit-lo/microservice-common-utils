package io.github.chikitlo.common.validation.result;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * GenericResult
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 02:54
 */
@Data
public abstract class GenericResult<T> {
    private boolean isSuccess;
    protected List<T> errors = Collections.emptyList();

    public int getErrorNumber() {
        return errors.isEmpty() ? 0 : errors.size();
    }
}