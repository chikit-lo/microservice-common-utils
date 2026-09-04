package io.github.chikitlo.common.constant;

/**
 * DateConstants
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/20 16:06
 */
public final class DateConstants {
    private DateConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    /**
     * yyyyMMddHHmmss
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * yyyyMMdd
     */
    public static final String YYYYMMDD = "yyyyMMdd";

    /**
     * Date format of yyyyMMdd
     */
    public static final String PARAMETER_FORMAT_DATE = "yyyyMMdd";

    /**
     * Date format of yyyyMMddHHmmss
     */
    public static final String PARAMETER_FORMAT_TIME = "yyyyMMddHHmmss";

    /**
     * System date(yyyyMMddHHmmss)
     */
    public static final String PARAMETER_DATETIME = "system.datetime";

    /**
     * System date(yyyymmdd) today
     */
    public static final String PARAMETER_CURRENT_DATE = "system.biz.curdate";

    /**
     * System date(yyyymmdd) yesterday
     */
    public static final String PARAMETER_BUSINESS_DATE = "system.biz.date";

    /**
     * Month_begin
     */
    public static final String MONTH_BEGIN = "month_begin";

    /**
     * Add_months
     */
    public static final String ADD_MONTHS = "add_months";

    /**
     * Month_end
     */
    public static final String MONTH_END = "month_end";

    /**
     * Week_begin
     */
    public static final String WEEK_BEGIN = "week_begin";

    /**
     * Week_end
     */
    public static final String WEEK_END = "week_end";

    /**
     * Timestamp
     */
    public static final String TIMESTAMP = "timestamp";
}