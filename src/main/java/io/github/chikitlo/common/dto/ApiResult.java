package io.github.chikitlo.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * ApiResult
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/21 00:58
 */
public record ApiResult<T>(String code,
                           String message,
                           T data,
                           @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime time) {
    public ApiResult {
        if (data == null) {
            data = (T) Collections.emptyList();
        }

        if (time == null) {
            time = LocalDateTime.now();
        }
    }

    public int getSize() {
        if (data instanceof Collection<?> collection) {
            return collection.size();
        }

        return data != null ? 1 : 0;
    }

    public static <T> ApiResult<T> of(String code, String message, T data) {
        return new ApiResult<>(code, message, data, LocalDateTime.now());
    }

    public static <T> ApiResult<T> of(ApiStatus status, T data) {
        return of(status.getCode(), status.getReason(), data);
    }

    public static ApiResult<Object> ok() {
        return of(ApiStatus.SUCCESSFUL, Collections.emptyList());
    }

    public static <T> ApiResult<T> ok(T data) {
        return of(ApiStatus.SUCCESSFUL, data);
    }

    public static <T> ApiResult<T> ok(String message, T data) {
        return of(ApiStatus.SUCCESSFUL.getCode(), message, data);
    }

    public static ApiResult<Object> fail() {
        return of(ApiStatus.FAILED, Collections.emptyList());
    }

    public static ApiResult<Object> fail(String message) {
        return of(ApiStatus.FAILED.getCode(), message, Collections.emptyList());
    }

    public static <T> ApiResult<T> fail(String message, T data) {
        return of(ApiStatus.FAILED.getCode(), message, data);
    }
}