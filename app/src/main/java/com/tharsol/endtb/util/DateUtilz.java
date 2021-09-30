package com.tharsol.endtb.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Locale;

public class DateUtilz
{

    /**
     * HH:mm like this 14:12
     */
    public static final String TIME_FORMAT_24H_WITHOUT_SEC = "HH:mm";
    public static final String TIME_FORMAT_MINT_SEC = "mm:ss";
    public static final String TIME_FORMAT_24H = "HH:mm:ss";
    public static final String TIME_FORMAT_12H_WITHOUT_SEC = "hh:mm aa";
    public static final String DATE_FORMAT_MONTH_NAME = "MMMMM";

    public static final String DATE_FORMAT_CHART_STANDARD = "MMM dd";
    public static final String DATE_FORMAT_STANDARD = "MMM dd, yyyy";
    public static final String DATE_FORMAT_DASHBOARD_SAVE = "yyyyMM";
    public static final String DATE_FORMAT_EXPENSE = "dd MMM, yyyy";
    public static final String DATE_ONLY_FORMAT = "dd";
    public static final String DATE_FORMAT_MONTH_YEAR = "MMM, yyyy";
    public static final String DATE_FORMAT_FULL_MONTH_STANDARD = "MMMM dd, yyyy";
    public static final String DATE_TIME_FORMAT_STANDARD = "MMM dd, yyyy hh:mm aa";
    public static final String DATE_TIME_REPORT_HEADER_STANDARD = "EEEE, dd MMM, yyyy";
    public static final String DATE_FORMAT_BACKEND_STANDARD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_BACKSLASH_STANDARD = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT_BACKEND_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_BACKEND_STANDARDITWH_T = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_TIME_FORMAT_DASHES_T = "yyyy-MM-dd HH-mm-ss";


    public static DateTime parTime24HWithoutSec(String time)
    {
        if (TextUtils.isEmpty(time)) return new DateTime();
        return DateTime.parse(time, DateTimeFormat.forPattern(TIME_FORMAT_24H_WITHOUT_SEC).withLocale(Locale.ENGLISH));
    }

    public static String formatNotificationDate(String date)
    {
        DateTime mDateTime = DateTime.parse(date, ISODateTimeFormat.dateTime());
        return mDateTime.toString(DATE_FORMAT_STANDARD, Locale.ENGLISH);
    }

  public static String formatChartDate(String date)
    {
        DateTime mDateTime = parseBackendStandard(date);
        return mDateTime.toString(DATE_FORMAT_CHART_STANDARD, Locale.ENGLISH);
    }

    public static String formatNotificationDate(DateTime mDateTime)
    {
        return mDateTime.toString(DATE_FORMAT_STANDARD, Locale.ENGLISH);
    }

    /**
     * Returns a formatter that combines a full date and time, separated by a 'T' (yyyy-MM-dd'T'HH:mm:ss.SSSZZ).
     * The time zone offset is 'Z' for zero, and of the form '±HH:mm' for non-zero.
     * The parser is strict by default, thus time string 24:00 cannot be parsed.
     *
     * @param date {@link DateTime}
     * @return
     */
    public static String formatISODateTime(DateTime date)
    {
        return ISODateTimeFormat.dateTime().print(date);
    }

/**
     * Returns a formatter that combines a full date and time, separated by a 'T' (yyyy-MM-dd'T'HH:mm:ss.SSSZZ).
     * The time zone offset is 'Z' for zero, and of the form '±HH:mm' for non-zero.
     * The parser is strict by default, thus time string 24:00 cannot be parsed.
     *
     * @return datetime
     */
    public static String getCurrentISODateTime()
    {
        return ISODateTimeFormat.dateTime().print(DateTime.now());
    }

    /**
     * it will return date format like yyyy-MM-dd HH-mm-ss
     *
     * @param dateTime
     * @return object of {@link String} as yyyy-MM-dd HH-mm-ss
     */
    public static String getDashesDateTime(DateTime dateTime)
    {
        return dateTime.toString(DATE_TIME_FORMAT_DASHES_T);
    }

    /**
     * @param date 2019-07-20T12:39:20.092794+05:00
     * @return object of {@link DateTime}
     */
    public static DateTime parseISODateTime(String date)
    {
        return DateTime.parse(date, ISODateTimeFormat.dateTime());
    }

    /**
     * Standard backend format is yyyy-MM-dd
     *
     * @param date as yyyy-MM-dd
     * @return object of {@link DateTime}
     */
    public static DateTime parseBackendStandard(String date)
    {
        return DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT_BACKEND_STANDARD).withLocale(Locale.ENGLISH));
    }

    /**
     * Standard backend format is yyyy-MM-dd
     *
     * @param date as {@link DateTime}
     * @return object of {@link String}
     */
    public static String formatBackendStandard(DateTime date)
    {
        return date.toString(DATE_FORMAT_BACKEND_STANDARD, Locale.ENGLISH);
    }

    /**
     * Standard backend format is dd/MM/yyyy
     *
     * @param date as {@link DateTime}
     * @return object of {@link String}
     */
    public static String formatBackSlashStandard(DateTime date)
    {
        return date.toString(DATE_FORMAT_BACKSLASH_STANDARD, Locale.ENGLISH);
    }

    /**
     * Format date as only month name like October
     *
     * @param date {@link @DateTime}
     * @return object format as name like October
     */
    public static String formatMonthName(DateTime date)
    {
        return date.toString(DATE_FORMAT_MONTH_NAME, Locale.ENGLISH);
    }

    /**
     * Standard backend format is yyyy-MM-dd
     *
     * @param date as yyyy-MM-dd
     * @return object format like MMM dd, yyyy
     */
    public static String formatStandardViewDate(String date)
    {
        return parseBackendStandard(date).toString(DATE_FORMAT_STANDARD, Locale.ENGLISH);
    }

    /**
     * Standard backend format is yyyy-MM-dd
     *
     * @param date as yyyy-MM-dd
     * @return object format like MMMM dd, yyyy
     */
    public static String formatStandardViewFullDate(String date)
    {
        return parseBackendStandard(date).toString(DATE_FORMAT_FULL_MONTH_STANDARD, Locale.ENGLISH);
    }

    public static String formatStandardViewDate(DateTime dateTime)
    {
        return dateTime.toString(DATE_FORMAT_STANDARD, Locale.ENGLISH);
    }

  public static String formatHeaderViewDate(DateTime dateTime)
    {
        return dateTime.toString(DATE_TIME_REPORT_HEADER_STANDARD, Locale.ENGLISH);
    }

    public static String formatExpenseViewDate(DateTime from, DateTime to)
    {
        return from.toString(DATE_ONLY_FORMAT, Locale.ENGLISH)
                + " to " + to.toString(DATE_FORMAT_EXPENSE, Locale.ENGLISH);
    }

    /**
     * @param dateTime non null object of {@link DateTime}
     * @return object format like MMM dd, yyyy hh:mm aa as {@link String}
     */
    public static String formatStandardViewDateTime(DateTime dateTime)
    {
        return dateTime.toString(DATE_TIME_FORMAT_STANDARD, Locale.ENGLISH);
    }

    /**
     * Date format should be this MMM dd, yyyy
     *
     * @param date format MMM dd, yyyy
     * @return object of string format like yyyy-MM-dd
     */
    public static String formatFromStandardViewDate(@NonNull String date)
    {
        if (TextUtils.isEmpty(date)) return "";
        return DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT_STANDARD).withLocale(Locale.ENGLISH))
                .toString(DATE_FORMAT_BACKEND_STANDARD, Locale.ENGLISH);
    }

    /**
     * Date format should be this MMM dd, yyyy
     * return current datetime on empty string
     *
     * @param date format MMM dd, yyyy
     * @return object of {@link DateTime} or current datetime if date is empty
     */
    public static DateTime parseStandardViewDate(@NonNull String date)
    {
        if (date.equalsIgnoreCase("")) return DateTime.now();
        return DateTime.parse(date, DateTimeFormat.forPattern(DATE_FORMAT_STANDARD).withLocale(Locale.ENGLISH));
    }

    /**
     * Date format should be this hh:mm aa
     * return current datetime on empty string
     *
     * @param date format MMM dd, yyyy
     * @return object of {@link DateTime} or current datetime if date is empty
     */
    public static DateTime parse12HWithoutSecDate(@NonNull String date)
    {
        if (date.equalsIgnoreCase("")) return DateTime.now();
        return DateTime.parse(date, DateTimeFormat.forPattern(TIME_FORMAT_12H_WITHOUT_SEC).withLocale(Locale.ENGLISH));
    }

    /**
     * Date format should be this hh:mm aa
     * return current datetime on empty string
     *
     * @param date format MMM dd, yyyy
     * @return object of {@link DateTime} or current datetime if date is empty
     */
    public static String format12HWithoutSecDate(@NonNull DateTime date)
    {
        return date.toString(TIME_FORMAT_12H_WITHOUT_SEC, Locale.ENGLISH);
    }

    /**
     * This function will return time as HH:mm
     *
     * @param dateTime non null object of {@link DateTime}
     * @return object of {@link String} as 13:26:30
     */
    public static String format24HTime(DateTime dateTime)
    {
        return dateTime.toString(TIME_FORMAT_24H_WITHOUT_SEC, Locale.ENGLISH);
    }

    /**
     * This function will return time as mm:ss
     *
     * @param dateTime non null object of {@link DateTime}
     * @return object of {@link String} as 126:30
     */
    public static String formatTimeAsMintSec(DateTime dateTime)
    {
        return dateTime.toString(TIME_FORMAT_MINT_SEC, Locale.ENGLISH);
    }

    /**
     * This function will format like "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateTime
     * @return object of {@link String} format as yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTimeFormatBackendStandard(DateTime dateTime)
    {
        return dateTime.toString(DATE_TIME_FORMAT_BACKEND_STANDARD, Locale.ENGLISH);
    }

    /**
     * This function will format like "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateTime
     * @return object of {@link String} format as yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTimeFormatBackendStandardT(DateTime dateTime)
    {
        return dateTime.toString(DATE_TIME_FORMAT_BACKEND_STANDARDITWH_T, Locale.ENGLISH);
    }

    /**
     * This function will format like "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateTime
     * @return object of {@link String} format as MMM, yyyy
     */
    public static String formatDateTimeMothYear(DateTime dateTime)
    {
        return dateTime.toString(DATE_FORMAT_MONTH_YEAR, Locale.ENGLISH);
    }

    /**
     * This function will parse like "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateTime as String object {@link com.sameelenterprises.mrep.util.DateUtilz#DATE_TIME_FORMAT_BACKEND_STANDARD}
     * @return object of {@link DateTime}
     */
    public static DateTime parseDateTimeBackendStandard(String dateTime) throws Exception
    {
        return DateTime.parse(dateTime, DateTimeFormat.forPattern(DATE_TIME_FORMAT_BACKEND_STANDARD).withLocale(Locale.ENGLISH));
    }

    /**
     * This function will parse like "yyyy-MM-dd'T'HH:mm:ss"
     *
     * @param dateTime as String object {@link com.sameelenterprises.mrep.util.DateUtilz#DATE_TIME_FORMAT_BACKEND_STANDARD}
     * @return object of {@link DateTime}
     */
    public static DateTime parseDateTimeBackendStandardT(String dateTime) throws Exception
    {
        return DateTime.parse(dateTime, DateTimeFormat.forPattern(DATE_TIME_FORMAT_BACKEND_STANDARDITWH_T).withLocale(Locale.ENGLISH));
    }

    public static int getNumberOfNights(DateTime start, DateTime end)
    {
        int nightCount = Days.daysBetween(start, end).getDays();
        DateTime leftOver = new DateTime(end.minusDays(nightCount));

        if (leftOver.withTimeAtStartOfDay().isAfter(start))
        {
            nightCount++;
        }

        return nightCount;
    }

    /**
     * It will check have more then 2 days difference
     *
     * @param date format should be yyyy-MM-dd
     * @return
     */
    public static boolean haveThreeDays(String date)
    {
        return Days.daysBetween(DateTime.now().withTimeAtStartOfDay(), parseBackendStandard(date)).getDays() > 2;
    }
}
