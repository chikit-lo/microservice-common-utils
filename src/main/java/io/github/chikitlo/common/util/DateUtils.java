package io.github.chikitlo.common.util;

import io.github.chikitlo.common.constant.DateConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * DateUtils
 *
 * @author Jack Lo
 * @version 1.0
 * @date 2025/12/21 01:28
 */
@Slf4j
public final class DateUtils {
    private DateUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern(DateConstants.YYYY_MM_DD_HH_MM_SS);

    public static String getCurrentTime(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(Date date, String format, String timezone) {
        return format(date, DateTimeFormatter.ofPattern(format), timezone);
    }

    public static String format(Date date, DateTimeFormatter formatter, String timezone) {
        LocalDateTime localDateTime = StringUtils.isEmpty(timezone) ? dateToLocalDateTime(date) : dateToLocalDateTime(date, ZoneId.of(timezone));
        return format(localDateTime, formatter);
    }

    public static String format(LocalDateTime localDateTime, String format) {
        return format(localDateTime, DateTimeFormatter.ofPattern(format));
    }

    public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    public static String dateToString(Date date) {
        return format(date, YYYY_MM_DD_HH_MM_SS, null);
    }

    public static String dateToString(Date date, String timezone) {
        return format(date, YYYY_MM_DD_HH_MM_SS, timezone);
    }

    public static String dateToString(ZonedDateTime zonedDateTime) {
        return YYYY_MM_DD_HH_MM_SS.format(zonedDateTime);
    }

    public static String dateToString(ZonedDateTime zonedDateTime, String timezone) {
        return dateToString(zonedDateTime, ZoneId.of(timezone));
    }

    public static String dateToString(ZonedDateTime zonedDateTime, ZoneId zoneId) {
        return YYYY_MM_DD_HH_MM_SS.withZone(zoneId).format(zonedDateTime);
    }

    public static Date parse(String date, String format, String timezone) {
        return parse(date, DateTimeFormatter.ofPattern(format), timezone);
    }

    public static Date parse(String date, DateTimeFormatter formatter, String timezone) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        if (StringUtils.isEmpty(timezone)) {
            return localDateTimeToDate(localDateTime);
        }
        return localDateTimeToDate(localDateTime, ZoneId.of(timezone));
    }

    public static ZonedDateTime parseZonedDateTime(String date, DateTimeFormatter formatter, String timezone) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, formatter);
        if (StringUtils.isNotEmpty(timezone)) {
            return zonedDateTime.withZoneSameInstant(ZoneId.of(timezone));
        }
        return zonedDateTime;
    }

    public static Date stringToDate(String date) {
        return parse(date, YYYY_MM_DD_HH_MM_SS, null);
    }

    public static Date stringToDate(String date, String timezone) {
        return parse(date, YYYY_MM_DD_HH_MM_SS, timezone);
    }

    public static ZonedDateTime stringToZonedDateTime(String date) {
        return ZonedDateTime.ofInstant(stringToDate(date).toInstant(), ZoneId.systemDefault());
    }

    public static long diffSecond(Date date1, Date date2) {
        return (long) Math.ceil(diffMillis(date1, date2) / 1000.0);
    }

    public static long diffMillis(Date date1, Date date2) {
        return Math.abs(date1.getTime() - date2.getTime());
    }

    public static Date getDateWithInterval(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, interval);
        return calendar.getTime();
    }

    /**
     * Convert yyyyMMdd to yyyy-MM-dd.
     *
     * @param dateStr
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/21 02:57
     */
    public static String convertToIsoLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Convert yyyy-MM-dd to yyyyMMdd.
     *
     * @param dateStr
     * @return java.lang.String
     * @throws
     * @author Jack Lo
     * @date 2025/12/21 02:58
     */
    public static String convertToBasicIsoDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE).format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    /**
     * List last N months' workdays(T-1, except today) in the specified date format.
     *
     * @param month
     * @param dateFormat
     * @return java.util.List<java.lang.String>
     * @throws
     * @author Jack Lo
     * @date 2025/12/21 03:03
     */
    public static List<String> listLastNMonthsWorkdays(int month, String dateFormat) {
        List<String> workdays = new ArrayList<>();
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusMonths(month);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workdays.add(date.format(formatter));
            }
        }

        return workdays;
    }

    /**
     * List last N years' month ends in the specified date format.
     *
     * @param year
     * @param dateFormat
     * @return java.util.List<java.lang.String>
     * @throws
     * @author Jack Lo
     * @date 2025/12/21 03:12
     */
    public static List<String> listLastNYearsMonthEnds(int year, String dateFormat) {
        List<String> monthEnds = new ArrayList<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(year).withDayOfMonth(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        LocalDate current = startDate.withDayOfMonth(startDate.lengthOfMonth());
        while (current.isBefore(endDate)) {
            monthEnds.add(current.format(formatter));
            LocalDate next = current.plusMonths(1);
            current = next.withDayOfMonth(next.lengthOfMonth());
        }

        return monthEnds;
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return dateToLocalDateTime(date, zoneId);
    }

    private static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), zoneId);
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return localDateTimeToDate(localDateTime, zoneId);
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }
}