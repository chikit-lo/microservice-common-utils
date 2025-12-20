package io.github.chikitlo.common.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ApiStatus
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/21 01:02
 */
@Getter
public enum ApiStatus {
    SUCCESSFUL("00000", Series.SUCCESSFUL, "Success"),
    CLIENT_ERROR("A0001", Series.CLIENT_ERROR, "Client Error"),
    PARAMETER_ERROR("A0100", Series.CLIENT_ERROR, "Request Parameter Error"),
    VERIFICATION_ERROR("A0101", Series.CLIENT_ERROR, "Parameter Verification Error"),
    FILE_VALIDATION_ERROR("A0102", Series.CLIENT_ERROR, "File Validation Error"),
    FILE_EXTENSION_NOT_SUPPORTED("A0103", Series.CLIENT_ERROR, "Unsupported File Extension"),
    EMPTY_UPLOAD_FILE("A0104", Series.CLIENT_ERROR, "Empty Upload File"),
    LOGIN_EXCEPTION("A0200", Series.CLIENT_ERROR, "Login Exception"),
    AUTHORIZATION_EXCEPTION("A0300", Series.CLIENT_ERROR, "Authorization Exception"),
    PERMISSION_DENIED("A0301", Series.CLIENT_ERROR, "Permission Denied"),
    UPLOAD_EXCEPTION("A0400", Series.CLIENT_ERROR, "File Upload Exception"),
    FAILED("B0001", Series.SERVER_ERROR, "System Error"),
    TIME_OUT("B0100", Series.SERVER_ERROR, "Execution Timeout"),
    RESOURCE_NOT_FOUND("B0200", Series.SERVER_ERROR, "Resource Not Found"),
    EMPTY_DATASET("C0001", Series.SUCCESSFUL, "No Data");

    private final String code;
    private final Series series;
    private final String reason;

    private static final Map<String, ApiStatus> CODE_INDEX;

    static {
        Map<String, ApiStatus> map = new HashMap<>();
        for (ApiStatus item : values()) {
            map.put(item.code, item);
        }

        CODE_INDEX = Collections.unmodifiableMap(map);
    }

    ApiStatus(String code, Series series, String reason) {
        this.code = code;
        this.series = series;
        this.reason = reason;
    }

    public static ApiStatus fromCode(String code) {
        if (code == null) {
            return null;
        }

        return CODE_INDEX.get(code);
    }

    public enum Series {
        SUCCESSFUL,
        CLIENT_ERROR,
        SERVER_ERROR
    }
}