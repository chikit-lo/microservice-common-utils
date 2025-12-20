package io.github.chikitlo.common.validation.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Result
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/13 02:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Result extends GenericResult<String> {
}