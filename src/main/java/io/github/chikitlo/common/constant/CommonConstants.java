package io.github.chikitlo.common.constant;

import org.apache.commons.lang3.SystemUtils;

import java.util.regex.Pattern;

/**
 * CommonConstants
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/21 03:16
 */
public final class CommonConstants {
    private CommonConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * COB date place holder
     */
    public static final String COB_DATE_PLACE_HOLDER = "#COBDate#";

    /**
     * Empty string
     */
    public static final String EMPTY_STRING = "";

    /**
     * COMMA ,
     */
    public static final String COMMA = ",";

    /**
     * COLON :
     */
    public static final String COLON = ":";

    /**
     * PERIOD .
     */
    public static final String PERIOD = ".";

    /**
     * QUESTION ?
     */
    public static final String QUESTION = "?";

    /**
     * SPACE " "
     */
    public static final String SPACE = " ";

    /**
     * PIPE |
     */
    public static final String PIPE = "|";

    /**
     * SINGLE_SLASH /
     */
    public static final String SINGLE_SLASH = "/";

    /**
     * DOUBLE_SLASH //
     */
    public static final String DOUBLE_SLASH = "//";

    /**
     * AT SIGN
     */
    public static final String AT_SIGN = "@";

    /**
     * SLASH /
     */
    public static final String SLASH = "/";

    /**
     * UNDERLINE "_"
     */
    public static final String UNDERLINE = "_";

    /**
     * HYPHEN "-"
     */
    public static final String HYPHEN = "-";

    /**
     * SEMICOLON ;
     */
    public static final String SEMICOLON = ";";

    /**
     * Defines a constant string "*".
     * This constant can be used in various scenarios, for example, as a wildcard to match all elements or items.
     */
    public static final String STAR = "*";

    /**
     * N
     */
    public static final char N = 'N';

    /**
     * Double brackets left
     */
    public static final String DOUBLE_BRACKETS_LEFT = "{{";

    /**
     * Double brackets left
     */
    public static final String DOUBLE_BRACKETS_RIGHT = "}}";

    /**
     * Double brackets left
     */
    public static final String DOUBLE_BRACKETS_LEFT_SPACE = "{ ";

    /**
     * Double brackets left
     */
    public static final String DOUBLE_BRACKETS_RIGHT_SPACE = " }";

    /**
     * Exec shell scripts
     */
    public static final String SH = "sh";

    /**
     * HTTP header
     */
    public static final String HTTP_HEADER_UNKNOWN = "unKnown";

    /**
     * HTTP X-Forwarded-For
     */
    public static final String HTTP_X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * HTTP X-Real-IP
     */
    public static final String HTTP_X_REAL_IP = "X-Real-IP";

    /**
     * User name regex
     */
    public static final Pattern REGEX_USER_NAME = Pattern.compile("^[a-zA-Z0-9._-]{3,39}$");

    /**
     * NULL String
     */
    public static final String NULL = "NULL";

    /**
     * Suffix of crc file
     */
    public static final String CRC_SUFFIX = ".crc";

    /**
     * Suffix of csv file
     */
    public static final String CSV_SUFFIX = ".csv";

    /**
     * Suffix of parquet file
     */
    public static final String PARQUET_SUFFIX = ".parquet";

    /**
     * jar
     */
    public static final String JAR = "jar";

    /**
     * -D <property>=<value>
     */
    public static final String D = "-D";

    /**
     * Select the name of the process identifier based on the operating system.
     * Use "handle" to represent the process identifier in Windows systems,
     * while using "pid" in non-Windows systems (such as Linux, macOS, etc.).
     */
    public static final String PID = SystemUtils.IS_OS_WINDOWS ? "handle" : "pid";

    /**
     * System line separator
     */
    public static final String SYSTEM_LINE_SEPARATOR = System.lineSeparator();

    /**
     * User's current working directory
     */
    public static final String USER_CURRENT_WORK_DIR = System.getProperty("user.dir");

    /**
     * Read permission
     */
    public static final int READ_PERMISSION = 2;

    /**
     * Write permission
     */
    public static final int WRITE_PERMISSION = 2 * 2;

    /**
     * Execute permission
     */
    public static final int EXECUTE_PERMISSION = 1;

    /**
     * Default admin permission
     */
    public static final int DEFAULT_ADMIN_PERMISSION = 7;

    /**
     * All permissions
     */
    public static final int ALL_PERMISSIONS = READ_PERMISSION | WRITE_PERMISSION | EXECUTE_PERMISSION;

    /**
     * Exit code success
     */
    public static final int EXIT_CODE_SUCCESS = 0;

    /**
     * Exit code failure
     */
    public static final int EXIT_CODE_FAILURE = -1;

    public static final String DOT = ".";

    public static final String UNDER_LINE = "_";

    public static final String EQUAL = "=";

    public static final String QUESTION_MARK = "?";

    public static final String AMPERSAND = "&";

    public static final String AT = "@";

    public static final String BACK_SLASH = "\\";

    public static final String POUND = "#";

    public static final String PERCENT = "%";

    public static final String DOLLAR = "$";

    public static final String ASTERISK = "*";

    public static final String OPEN_PARENTHESIS = "(";

    public static final String CLOSE_PARENTHESIS = ")";

    public static final String OPEN_BRACKET = "[";

    public static final String CLOSE_BRACKET = "]";

    public static final String OPEN_BRACE = "{";

    public static final String CLOSE_BRACE = "}";

    public static final String PLUS = "+";

    public static final String MINUS = "-";
}