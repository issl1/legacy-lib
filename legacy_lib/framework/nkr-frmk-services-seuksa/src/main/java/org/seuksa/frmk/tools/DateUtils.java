package org.seuksa.frmk.tools;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author prasnar
 * @version $Revision$
 */
public class DateUtils {
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

	/** The maximum date possible. */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);

    private static final int[] NON_WORKING_FIXED_DAYS = new int[] {
        1,
        121,
        128,
        195,
        227,
        305,
        315,
        359
	};
	private static final int[] NON_WORKING_MOBILE_DAYS = new int[] {
	        0,
	        1,
	        39,
	        49
	};


 	public static final long MILLISECOND_IN_DAY = 86400000;
	public static final String FORMAT_YYYYMMDD_HHMMSS_NOSEP = "yyyyMMdd_HH:mm:ss";
	public static final String FORMAT_YYYYMMDD_NOSEP = "yyyyMMdd";
    public static final String FORMAT_DDMMYYYY_SLASH = "dd/MM/yyyy";
    public static final String FORMAT_DDMMYYYY_HHMM_SLASH = "dd/MM/yyyy HH:mm";
    public static final String FORMAT_DDMMYYYY_MINUS = "dd-MM-yyyy";
    public static final String FORMAT_DDMMYYYY_HHMM_A_SLASH = "dd/MM/yyyy hh:mm a";
    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDD_HHMMSS_SLASH = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMAT_YYYYMMDD_SLASH = "yyyy/MM/dd";
    public static final String GENERIC_DISPLAY_FORMAT = "E, dd MMM yyyy";
    public static final String TIME_DISPLAY_FORMAT = "HH mm ss";

    public static final String FORMAT_STR_DDMMYY_NOSEP = "ddMMyy";
    public static final String FORMAT_STR_DDMMYYYY_NOSEP = "ddMMyyyy";

    public static String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
    public static String DEFAULT_TIME_FORMAT = "HH:mm";
    public static String DEFAULT_DATE_TIME_FORMAT = "dd-MMM-yyyy HH:mm";
    public static String JTDS_DATE_FORMAT = "yyyy-MM-dd";

    public static String MONTH_YEAR_FORMAT = "MMM-yyyy";
    public static String YEAR_FORMAT = "yyyy";
    public static String DEFAULT_DATE_TIME_SEARCH_FORMAT = "yyyy-MM-dd 00:00:00";
    public static String SHORT_DATE_FORMAT = "dd-MMM-yy";

    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static long dateDiff(Date date1,  Date date2) {
        // Get msec from each, and subtract.
        long diff = date2.getTime() - date1.getTime();
        return diff;
    }
    
    /**
     * 
     * @return
     */
    public static Date today() {
        return Calendar.getInstance().getTime();
    }
    
    /**
     * 
     * @return
     */
    public static String todayFullNoSeparator() {
        return date2String(today(), FORMAT_YYYYMMDD_HHMMSS_NOSEP);
    }
    
    /**
     * 
     * @return
     */
    public static String todayFull() {
        return date2String(today(), FORMAT_YYYYMMDD_HHMMSS);
    }
    
    /**
     * 
     * @return
     */
    public static String todayShort() {
        return date2String(today(), FORMAT_YYYYMMDD);
    }
    
    /**
     * 
     * @return
     */
    public static Date todayH00M00S00() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }
    
    /**
     * 
     * @return
     */
    public static Date todayH23M59S59() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }
    
    /**
     * 
     * @param dt
     * @return
     * @throws ParseException
     */
    public static String date2StringDDMMYYYY_HHMM_SLASH(Date dt) {
        return date2String(dt, FORMAT_DDMMYYYY_HHMM_SLASH);
    }
    


    
    /**
     * 
     * @param dt
     * @return
     * @throws ParseException
     */
    public static Date string2DateDDMMYYYY_HHMM(String dt) throws ParseException {
        try {
            return string2Date(dt, FORMAT_DDMMYYYY_HHMM_SLASH);
        } catch (Exception e) {
            return string2Date(dt, FORMAT_DDMMYYYY_HHMM_SLASH);
        }
    }

    /**
     * 
     * @param dt
     * @return
     * @throws ParseException
     */
    public static Date string2DateDDMMYYYY(String dt) {
        try {
            return string2Date(dt, FORMAT_DDMMYYYY_SLASH);
        } catch (Exception e) {
            return string2Date(dt, FORMAT_DDMMYYYY_MINUS);
        }
    }
    
    public static Date string2DateDDMMYYYYNoSeparator(String dt) {
        return string2Date(dt, FORMAT_STR_DDMMYYYY_NOSEP);
    }
    
    public static Date string2DateDDMMYYNoSeparator(String dt) {
        return string2Date(dt, FORMAT_STR_DDMMYYYY_NOSEP);
    }

    /**
     * 
     * @param dt
     * @return
     * @throws ParseException
     */
    public static String date2StringYYYYMMDDNoSeparator(Date dt) {
        return date2String(dt, FORMAT_YYYYMMDD_NOSEP);
    }
    
    /**
     * 
     * @param dt
     * @return
     * @throws ParseException
     */
    public static String date2StringYYYYMMDD_HHMMSS(Date dt) {
        return date2String(dt, FORMAT_YYYYMMDD_HHMMSS);
    }

    /**
     * 
     * @param dt
     * @return
     * @throws ParseException
     */
    public static String date2StringDDMMYYYY_SLASH(Date dt) {
        return date2String(dt, FORMAT_DDMMYYYY_SLASH);
    }
    
    /**
     * 
     * @param dt
     * @return
     * @throws ParseException
     */
    public static String date2StringDDMMYYYY_MINUS(Date dt) {
        return date2String(dt, FORMAT_DDMMYYYY_MINUS);
    }

    /**
     * 
     * @param dt
     * @param sdf
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String dt, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
			return sdf.parse(dt);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			return minDate(); 
		}
    }
    
    /**
     * 
     * @return
     */
    public static Date minDate() {
    	Calendar cal = Calendar.getInstance();
		cal.set(1901, 01, 01);
		return cal.getTime();
    }

    /**
     * 
     * @param dt
     * @param nbYears
     * @return
     */
    public static Date addYearsDate(Date dt, int nbYears) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.YEAR, nbYears);
        return cal.getTime();
    }

    /**
     * 
     * @param dt
     * @param nbDays
     * @return
     */
    public static Date addDaysDate(Date dt, int nbDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DATE, nbDays);
        return cal.getTime();
    }
    
    /**
     * 
     * @param dt
     * @param nbHours
     * @return
     */
    public static Date addHoursDate(Date dt, int nbHours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.HOUR, nbHours);
        return cal.getTime();
    }

    /**
     * 
     * @param dt
     * @param nbMonths
     * @return
     */
    public static Date addMonthsDate(Date dt, int nbMonths) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MONTH, nbMonths);
        return cal.getTime();
    }

    /**
     * 
     * @param range
     * @return
     */
    public static Date[] getDateRange(EnumDateRange range) {
        if (range == EnumDateRange.YESTERDAY) {
            return getDateRangeYesterday();
        }
        if (range == EnumDateRange.TODAY) {
            return getDateRangeToday();
        }
        
        if (range == EnumDateRange.LAST_WEEK) {
            return getDateRangeLastWeek();
        }
        if (range == EnumDateRange.CURRENT_WEEK) {
            return getDateRangeCurrentWeek();
        }
        
        if (range == EnumDateRange.LAST_MONTH) {
            return getDateRangeLastMonth();
        }
        if (range == EnumDateRange.CURRENT_MONTH) {
            return getDateRangeCurrentMonth();
        }
        
        if (range == EnumDateRange.LAST_YEAR) {
            return getDateRangeLastYear();
        }
        if (range == EnumDateRange.CURRENT_YEAR) {
            return getDateRangeCurrentYear();
        }
        
        return null;
    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeToday() {
        Calendar start = new GregorianCalendar();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar end = new GregorianCalendar();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        return new Date[] { start.getTime(), start.getTime() };
    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeYesterday() {
        Calendar start = new GregorianCalendar();
        start.add(Calendar.DATE, -1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar end = new GregorianCalendar();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        return new Date[] { start.getTime(), start.getTime() };
    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeCurrentWeek() {
        Calendar start = new GregorianCalendar();
        while (start.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            start.add(Calendar.DATE, -1);
        }
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DATE, 6);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        return new Date[] { start.getTime(), end.getTime() };
    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeLastWeek() {
        Calendar start = new GregorianCalendar();
        while (start.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            start.add(Calendar.DATE, -1);
        }
        start.add(Calendar.DATE, -7);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DATE, 6);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        return new Date[] { start.getTime(), end.getTime() };

    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeCurrentMonth() {
        Calendar start = new GregorianCalendar();
        start.set(Calendar.DATE, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        
        Calendar end = new GregorianCalendar();
        end.set(Calendar.DATE, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        return new Date[] { start.getTime(), end.getTime() };
    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeLastMonth() {
        Calendar start = new GregorianCalendar();
        start.add(Calendar.MONTH, -1);
        start.set(Calendar.DATE, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.set(Calendar.DATE, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        return new Date[] { start.getTime(), end.getTime() };
    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeCurrentYear() {
        Calendar start = new GregorianCalendar();
        start.set(start.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        Calendar end = new GregorianCalendar();
        end.set(start.get(Calendar.YEAR), 11, 31, 23, 59, 59);

        return new Date[] { start.getTime(), end.getTime() };
    }
    
    /**
     * 
     * @return
     */
    public static Date[] getDateRangeLastYear() {
        Calendar start = new GregorianCalendar();
        start.set(start.get(Calendar.YEAR) - 1, 0, 1, 0, 0, 0);
        Calendar end = new GregorianCalendar();
        end.set(start.get(Calendar.YEAR), 11, 31, 23, 59, 59);

        return new Date[] { start.getTime(), end.getTime() };
    }
    
    /**
     * 
     * @param dt
     * @return
     */
    public static final String getGenericDisplayFormat(String dt) {
        Calendar cal = string2Calendar(dt, "-");
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DateUtils.GENERIC_DISPLAY_FORMAT);
        return (sdf.format(cal.getTime()));
    }

    /**
     * 
     * @param dt
     * @param tzString
     * @return
     */
    public static final String getGenericDisplayFormat(String dt, String tzString) {
        Calendar cal = stringToCalendar(dt, "-", tzString);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DateUtils.GENERIC_DISPLAY_FORMAT);
        return (sdf.format(cal.getTime()));
    }

    public static final String formatDate(Date dt, String format) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(cal.getTime()));
    }

    public static final String getCurrentDate(String format) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(cal.getTime()));
    }

    /**
     * 
     * @param dt
     * @return
     */
    public static final String getTimeFromDate(Date dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);

        StringBuffer ret = new StringBuffer();
        ret.append(cal.get(Calendar.HOUR_OF_DAY));
        ret.append(":");
        ret.append(cal.get(Calendar.MINUTE));
        ret.append(":");
        ret.append(cal.get(Calendar.SECOND));

        return ret.toString();
    }

    public static final String getTimeFromDate(Date dt, String tzString) {
        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dt);
            gc.setTimeZone(TimeZone.getTimeZone(tzString));
            StringBuffer ret = new StringBuffer();
            ret.append(gc.get(Calendar.HOUR));
            ret.append(":");
            ret.append(gc.get(Calendar.MINUTE));
            ret.append(" ");
            if (gc.get(Calendar.AM_PM) == 0) {
                ret.append("AM");
            } else {
                ret.append("PM");
            }
            return ret.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param dt
     * @param tzString
     * @return
     */
    public static final String getDateTimeFromDate(Date dt, String tzString) {
        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dt);
            gc.setTimeZone(TimeZone.getTimeZone(tzString));
            StringBuffer ret = new StringBuffer();
            ret.append(gc.get(Calendar.YEAR));
            ret.append("-");
            ret.append(gc.get(Calendar.MONTH) - 1);
            ret.append("-");
            ret.append(gc.get(Calendar.DATE));
            ret.append(" ");
            ret.append(gc.get(Calendar.HOUR));
            ret.append(":");
            ret.append(gc.get(Calendar.MINUTE));
            ret.append(" ");
            if (gc.get(Calendar.AM_PM) == 0) {
                ret.append("AM");
            } else {
                ret.append("PM");
            }
            return ret.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param dt
     * @param tzString
     * @param dateformat
     * @return
     */
    public static final String dateToString(Date dt, String tzString, String dateformat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.setTimeZone(TimeZone.getTimeZone(tzString));
        return calendar2String(cal, dateformat);
    }

    /**
     * 
     * @param dt
     * @param dateformat
     * @return
     */
    public static final String date2String(Date dt, String dateformat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return calendar2String(cal, dateformat);
    }
    
    /**
     * 
     * @param dt
     * @return
     */
    public static final String date2StringDB(Date dt) {
        return date2String(dt, DateUtils.FORMAT_YYYYMMDD_HHMMSS);
    }
    
    /**
     * 
     * @param dt
     * @return
     */
    public static final String date2StringDB_YYYYMMDD(Date dt) {
        return date2String(dt, DateUtils.FORMAT_YYYYMMDD);
    }

    /**
     * 
     * @param cal
     * @param dateformat
     * @return
     */
    public static final String calendar2String(Calendar cal, String dateformat) {
        StringBuffer ret = new StringBuffer();

        String separator = new String();
        int iSep = dateformat.indexOf('-');
        if (iSep > 0) {
            separator = "-";
        } else {
            iSep = dateformat.indexOf('/');
            if (iSep > 0) {
                separator = "/";
            }
        }

        if (iSep == 2) {
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.DATE)), 2, '0'));
            ret.append(separator);
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, '0'));
            ret.append(separator);
            ret.append(cal.get(Calendar.YEAR));
        } else if (iSep == 4) {
            ret.append(cal.get(Calendar.YEAR));
            ret.append(separator);
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, '0'));
            ret.append(separator);
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.DATE)), 2, '0'));
            ret.append(" ");
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)), 2, '0'));
            ret.append(":");
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MINUTE)), 2, '0'));
            ret.append(":");
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.SECOND)), 2, '0'));
            // ret.append(":");
            // ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MILLISECOND)),
            // 2, '0'));
        } else if (FORMAT_YYYYMMDD_NOSEP.equals(dateformat)) {
            ret.append(cal.get(Calendar.YEAR));
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, '0'));
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.DATE)), 2, '0'));
        } else if (FORMAT_YYYYMMDD_HHMMSS_NOSEP.equals(dateformat)) {
            ret.append(cal.get(Calendar.YEAR));
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, '0'));
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.DATE)), 2, '0'));
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)), 2, '0'));
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MINUTE)), 2, '0'));
            ret.append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.SECOND)), 2, '0'));
        } else {
        	DateFormat dateFormatter = new SimpleDateFormat(dateformat);
        	ret.append(dateFormatter.format(cal.getTime()));
        }

        return ret.toString();
    }

    /**
     * 
     * @param date
     * @param delim
     * @return
     */
    public static final Calendar string2Calendar(String date, String delim) {
        String[] split = StringUtils.splitByWholeSeparator(date, delim);
        int yr = Integer.parseInt(split[0]);
        int mo = Integer.parseInt(split[1]) - 1; /*
                                                         * Calendar month is
                                                         * zero indexed, string
                                                         * months are not
                                                         */
        int dt = Integer.parseInt(split[2]);

        GregorianCalendar gc = new GregorianCalendar(yr, mo, dt);
        return gc;
    }

    /**
     * 
     * @param date
     * @param delim
     * @param tzString
     * @return
     */
    public static final Calendar stringToCalendar(String date, String delim, String tzString) {
        String[] split = StringUtils.splitByWholeSeparator(date, delim);
        int yr = Integer.parseInt(split[0]);
        int mo = Integer.parseInt(split[1]) - 1; /*
                                                         * Calendar month is
                                                         * zero indexed, string
                                                         * months are not
                                                         */
        int dt = Integer.parseInt(split[2]);

        GregorianCalendar gc = new GregorianCalendar(yr, mo, dt);
        gc.setTimeZone(TimeZone.getTimeZone(tzString));
        return gc;
    }

    /**
     * 
     * @param utimezonestring
     * @return
     */
    public static final GregorianCalendar getCurrentCalendar(String utimezonestring) {
        try {
            GregorianCalendar gc = new GregorianCalendar();

            gc.setTimeZone(TimeZone.getTimeZone(utimezonestring));
            return gc;
        } catch (Exception e) {
            // If exception, return server TimeStamp
            return new GregorianCalendar();
        }
    }

    /**
     * 
     * @param day
     * @return
     */
    public static final String getDayString(int day) {
        switch (day) {
        case Calendar.SUNDAY:
            return "SUNDAY";
        case Calendar.MONDAY:
            return "MONDAY";
        case Calendar.TUESDAY:
            return "TUESDAY";
        case Calendar.WEDNESDAY:
            return "WEDNESDAY";
        case Calendar.THURSDAY:
            return "THURSDAY";
        case Calendar.FRIDAY:
            return "FRIDAY";
        case Calendar.SATURDAY:
            return "SATURDAY";
        }
        return "";
    }

    /**
     * 
     * @param dt
     * @return
     */
    public static int getNbDaysInMonth(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 
     * @param dt
     * @return
     */
    public static int getYear(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 
     * @param dt
     * @return
     */
    public static int getMonth(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 
     * @param dt
     * @return
     */
    public static int getDay(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DATE);
    }
    
    /**
     * 
     * @param dt
     * @return
     */
    public static int getHour(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.HOUR);
    }
    
    /**
     * 
     * @param dt
     * @return
     */
    public static int getMinute(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.MINUTE);
    }
    
   

    public static boolean isWorkingDay(final Date date) {
        final GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return false;
        }

        final int year = calendar.get(Calendar.YEAR);
        for (int i = 0; i < NON_WORKING_FIXED_DAYS.length; i++) {
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            if (calendar.isLeapYear(calendar.get(Calendar.YEAR)) && dayOfYear > 59) {
                dayOfYear--;
            }
            if (dayOfYear == NON_WORKING_FIXED_DAYS[i]) {
                return false;
            }
        }

        final Calendar easterCalendar = Easter.findHolyDay(year);
        final int easterDayOfYear = easterCalendar.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < NON_WORKING_MOBILE_DAYS.length; i++) {
            final int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            if (dayOfYear == easterDayOfYear + NON_WORKING_MOBILE_DAYS[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * 
     * @param dateCandidate
     * @param datePattern
     * @return
     */
    public static boolean isDate(final String dateCandidate, final String datePattern) {
        try {
            if (dateCandidate == null || datePattern == null) {
                return false;
            }
            final SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            sdf.parse(dateCandidate);
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        return isDate(date, FORMAT_YYYYMMDD_HHMMSS);
    }


    public static int getNumberMonthOfTwoDates(final Date date1, final Date date2) {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        final int month1 = calendar1.get(Calendar.MONTH);
        final int year1 = calendar1.get(Calendar.YEAR);
        final Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        final int month2 = calendar2.get(Calendar.MONTH);
        final int year2 = calendar2.get(Calendar.YEAR);
        final int nubMonth = ((year1 - year2) * 12) + (month1 - month2);
        return nubMonth + 1;
    }

    public static String getMonthYearLabel(final Date date) {
        return getDateLabel(date, MONTH_YEAR_FORMAT);
    }

    public static int getNumberYearOfTwoDates(final Date date1, final Date date2) {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        final int year1 = calendar1.get(Calendar.YEAR);
        final int month1 = calendar1.get(Calendar.MONTH);
        final int day1 = calendar1.get(Calendar.DAY_OF_MONTH);

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        final int year2 = calendar2.get(Calendar.YEAR);
        final int month2 = calendar2.get(Calendar.MONTH);
        final int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        int numberYear = year1 - year2;
        if (month1 < month2 || (month2 == month1 && day1 < day2)) {
            numberYear--;
        }
        return numberYear;
    }


    /**
     * 
     * @param dateLabel
     * @return
     */
    public static Time getSqlTime(final String dateLabel) {
        final Date utilDate = getTime(dateLabel);
        return new Time(utilDate.getTime());
    }

    /**
     * Method used to get diff in days between two dates
     * @param date1 : Date
     * @param date2 : Date
     * @return long
     */
    public static Long getDiffInDays(final Date date1, final Date date2) {
        if (date1 != null && date2 != null) {
            /*
             * In correct result.
             * 31/13/2015 - 14/03/2015 ==> 291 (It is Incorrect). The correct da
             * final Long diffInDays = (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
             * return diffInDays 
             */
        	final Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);            
            final Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
        	long diffDay = 0;
			if (calendar1.before(calendar2)) {
				diffDay = -countDiffDay(calendar1, calendar2);
			} else {
				diffDay = countDiffDay(calendar2, calendar1);
			}
			return diffDay;
        }
        return null;
    }
    
    private static Long countDiffDay(Calendar calendar1, Calendar calendar2) {
		Calendar date = (Calendar) calendar1.clone();
		long daysBetween = 0;
		while (date.before(calendar2)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
    }

    /**
     * Method used to get diff in days between two dates and plus one day.
     * @param date1 : Date
     * @param date2 : Date
     * @return long
     */
    public static Long getDiffInDaysPlusOneDay(final Date date1, final Date date2) {
        Long diffInDays = getDiffInDays(date1, date2);
        if (diffInDays != null) {
            diffInDays = diffInDays + 1;
        }
        return diffInDays;
    }
    

    private static final Map REGEX_FORMAT = new HashMap();

    static {
        REGEX_FORMAT.put("\\d{2}:\\d{2}", "HH:mm");
        REGEX_FORMAT.put("\\d{2}:\\d{2}:\\d{2}", "HH:mm:ss");
        REGEX_FORMAT.put("\\d{2}/\\d{2}/\\d{2}", "dd/MM/yy");
        REGEX_FORMAT.put("\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}", "dd/MM/yy HH:mm");
        REGEX_FORMAT.put("\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}", "dd/MM/yy HH:mm:ss");
        REGEX_FORMAT.put("\\d{2}/\\d{2}/\\d{4}", "dd/MM/yyyy");
        REGEX_FORMAT.put("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}", "dd/MM/yyyy HH:mm");
        REGEX_FORMAT.put("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}", "dd/MM/yyyy HH:mm:ss");
    }

    public static Date getDateSmartly(final String dateLabel) {
        final Iterator iterator = REGEX_FORMAT.keySet().iterator();
        while (iterator.hasNext()) {
            final String regex = (String) iterator.next();
            if (Pattern.matches(regex, dateLabel)) {
                return getDate(dateLabel, (String) REGEX_FORMAT.get(regex));
            }
        }
        return null;
    }

    public static Date getDate(final String dateLabel) {
        return getDate(dateLabel, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 
     * @param dateLabel
     * @param formatPattern
     * @return
     */
    public static Date getDate(final String dateLabel, final String formatPattern) {
        if (dateLabel != null && formatPattern != null) {
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
                return sdf.parse(dateLabel);
            }
            catch (ParseException pe) {
            	logger.error(pe.getMessage());
                return null;
            }
        }
        return null;
    }
    
    /**
     * 
     * @return
     */
    public static Date getDateAtBeginningOfLastMonth() {
    	return getDateAtBeginningOfLastNbMonths(-1);
    }
    	

    /**
     * 
     * @param nbMonths
     * @return
     */
    public static Date getDateAtBeginningOfLastNbMonths(int nbMonths) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -nbMonths);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }    

    public static Date getDateAtBeginningOfYear() {
    	return getDateAtBeginningOfYear((Date)null);
    }

    public static Date getDateAtBeginningOfLastYear() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateAtBeginningOfYear(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        if (date !=null) {
        	calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateAtBeginningOfYear(final String dateLabel) {
        return getDateAtBeginningOfYear(getDate(dateLabel));
    }

    public static Date getDateAtEndOfYear() {
    	return getDateAtEndOfYear((Date)null);
    }

    public static Date getDateAtEndOfLastYear() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateAtBeginningOfLastYear());
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }
    
    public static Date getDateAtEndOfYear(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateAtBeginningOfYear(date));
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date getDateAtEndOfYear(final String dateLabel) {
        return getDateAtEndOfYear(getDate(dateLabel));
    }
    
    
    public static Date getDateAtBeginningOfMonth() {
    	return getDateAtBeginningOfMonth((Date)null);
    }

    public static Date getDateAtBeginningOfMonth(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        if (date !=null) {
        	calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateAtBeginningOfMonth(final String dateLabel) {
        return getDateAtBeginningOfMonth(getDate(dateLabel));
    }

    public static Date getDateAtEndOfMonth() {
    	return getDateAtEndOfMonth((Date)null);
    }

    public static Date getDateAtEndOfLastMonth() {
        final Calendar calendar = Calendar.getInstance();
    	calendar.setTime(getDateAtBeginningOfLastMonth());
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date getDateAtEndOfMonth(final Date date) {
        final Calendar calendar = Calendar.getInstance();
    	calendar.setTime(getDateAtBeginningOfMonth(date));
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date getDateAtEndOfMonth(final String dateLabel) {
        return getDateAtEndOfMonth(getDate(dateLabel));
    }

    public static Date getDateAtBeginningOfWeek() {
    	return getDateAtBeginningOfWeek((Date)null);
    }

    public static Date getDateAtBeginningOfWeek(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        if (date !=null) {
        	calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        while (calendar.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        return calendar.getTime();
    }

    public static Date getDateAtBeginningOfWeek(final String dateLabel) {
        return getDateAtBeginningOfWeek(getDate(dateLabel));
    }

    public static Date getDateAtEndOfWeek() {
    	return getDateAtEndOfWeek((Date)null);
    }

    public static Date getDateAtEndOfWeek(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        if (date !=null) {
        	calendar.setTime(getDateAtBeginningOfWeek(date));
        }
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date getDateAtEndOfWeek(final String dateLabel) {
        return getDateAtEndOfWeek(getDate(dateLabel));
    }

    public static Date getDateAtBeginningOfDay(final Date date) {
    	if (date == null) {
            return null;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateAtBeginningOfDay(final String dateLabel) {
        return getDateAtBeginningOfDay(getDate(dateLabel));
    }

    public static Date getDateAtEndOfDay(final Date date) {
    	if (date == null) {
            return null;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateAtBeginningOfDay(date));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date getDateAtEndOfDay(final String dateLabel) {
        return getDateAtEndOfDay(getDate(dateLabel));
    }

    public static Date getTime(final String dateLabel) {
        return getDate(dateLabel, DEFAULT_TIME_FORMAT);
    }

    public static Date getDateTime(final String dateTimeLabel) {
        return getDate(dateTimeLabel, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String getDateLabel(final Date date, final String formatPattern) {
        if (date != null && formatPattern != null) {
            return DateFormatUtils.format(date, formatPattern);
        }
        return null;
    }

    public static String getDateLabel(final Date date) {
        return getDateLabel(date, DEFAULT_DATE_FORMAT);
    }

    public static String getTimeLabel(final Date date) {
        return getDateLabel(date, DEFAULT_TIME_FORMAT);
    }

    public static String getDateTimeLabel(final Date date) {
        return getDateLabel(date, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String getWeekLabel(final Date date) {
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("ww");
        c.setMinimalDaysInFirstWeek(4);
        formatter.setCalendar(c);
        return formatter.format(date);
    }

    public static Date plus(final Date date, final int days) {
        final String day = DateFormatUtils.format(date, "dd");
        final String year = DateFormatUtils.format(date, "yyyy");
        final String month = getMonthStr(date);
        return getDate(new Long(day).intValue() + days + "/" + month + "/" + year);
    }

    public static Date plusYear(final Date date, final int years) {
        //        final String day = DateFormatUtils.format(date, "dd");
        //        final String year = DateFormatUtils.format(date, "yyyy");
        //        final String month = getMonth(date);
        //        return getDate(day + "-" + month + "-" + (new Long(year).intValue() + years));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    public static Date plusMonth(final Date date, final int months) {
        //        final String day = DateFormatUtils.format(date, "dd");
        //        final String year = DateFormatUtils.format(date, "yyyy");
        //        final String month = getMonth(date);
        //        return getDate(day + "-" + (new Long(month).intValue() + months) + "-" + year);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    public static Date minusMonth(final Date date, final int months) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months * (-1));
        return calendar.getTime();
    }

    public static String getYearStr(final Date date) {
        return DateFormatUtils.format(date, "yy");
    }

    public static String getMonthStr(final Date date) {
        return DateFormatUtils.format(date, "MM");
    }
    

    /**
     * Easter - compute the day on which Easter falls.
     * 
     * In the Christian religion, Easter is possibly the most important holiday of
     * the year, so getting its date <I>just so </I> is worthwhile.
     * 
     * @author: Ian F. Darwin, http://www.darwinsys.com/, based on a detailed
     *          algorithm in Knuth, vol 1, pg 155.
     * 
     * @Version: $Id: DateUtils.java,v 1.1 2008/09/15 13:17:12 nmetriau Exp $ Written in C,
     *           Toronto, 1988. Java version 1996.
     * 
     * @Note: It's not proven correct, although it gets the right answer for years
     *        around the present.
     */
    static final class Easter {

        /*
         * Compute the day of the year that Easter falls on. Step names E1 E2 etc.,
         * are direct references to Knuth, Vol 1, p 155. @exception
         * IllegalArgumentexception If the year is before 1582 (since the algorithm
         * only works on the Gregorian calendar).
         */
        public static final Calendar findHolyDay(final int year) {
            if (year <= 1582) {
                throw new IllegalArgumentException("Algorithm invalid before April 1583");
            }
            int golden, century, x, z, d, epact, n;

            golden = (year % 19) + 1; /* E1: metonic cycle */
            century = (year / 100) + 1; /* E2: e.g. 1984 was in 20th C */
            x = (3 * century / 4) - 12; /* E3: leap year correction */
            z = ((8 * century + 5) / 25) - 5; /* E3: sync with moon's orbit */
            d = (5 * year / 4) - x - 10;
            epact = (11 * golden + 20 + z - x) % 30; /* E5: epact */
            if ((epact == 25 && golden > 11) || epact == 24)
                epact++;
            n = 44 - epact;
            n += 30 * (n < 21 ? 1 : 0); /* E6: */
            n += 7 - ((d + n) % 7);
            if (n > 31) { /* E7: */
                return new GregorianCalendar(year, 4 - 1, n - 31); /* April */
            }
            else {
                return new GregorianCalendar(year, 3 - 1, n); /* March */
            }
        }
    }

    /**
     * Checks whether data interval is  a valid  checkDate interval
     *
     * @param startDate - start checkDate
     * @param endDate   - end checkDate
     * @return true if interval is valid and false if it is not
     */
    public static boolean isDateIntervalValid(Date startDate, Date endDate) {
        boolean isValid = false;
        if (startDate != null) {
            if (endDate == null) {
                isValid = true;
            } else if (startDate.before(endDate) ||
                    startDate.compareTo(endDate) == 0) {
                isValid = true;
            }
        }
        return isValid;
    }

    /**
     * Create checkDate interval.
     *
     * @param startDate - start checkDate
     * @param endDate   - end checkDate
     * @return checkDate interval
     */
    public static DateRange createInterval(Date startDate, Date endDate) {
        return new DateRange(startDate, endDate);
    }

    /**
     * Removes time details from given checkDate
     *
     * @param date - checkDate with time
     * @return checkDate without time
     */
    public static Date getDateWithoutTime(Date date) {
        if (date == null) {
            return date;
        }
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        Calendar dateCalendar2 = Calendar.getInstance();
        dateCalendar2.clear();
        dateCalendar2.set
                (
                        dateCalendar.get(Calendar.YEAR),
                        dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)
                );
        return dateCalendar2.getTime();
    }

    /**
     * Checks whether given date is withing given interval.
     *
     * @param checkDate - date to check
     * @param startDate - interval start checkDate
     * @param endDate   - interval end checkDate
     * @return true if given checkDate is withing given interval and false otherwise
     */
    public static boolean withinInterval(Date checkDate, Date startDate, Date endDate) {
        return withinInterval(checkDate, createInterval(startDate, endDate), true);
    }

    /**
     * Gets today checkDate without time.
     *
     * @return today date without time.
     */
    public static Date todayDate() {
        return getDateWithoutTime(new Date());
    }

    /**
     * Checks whether given date is withing given interval.
     * @param checkDate   - date to check
     * @param interval    - given interval
     * @param withoutTime - if true then it makes checks without time if false than it compares exact dates
     * @return @return true if given date is withing given interval and false otherwise
     */
    public static boolean withinInterval(final Date checkDate,
                                         final DateRange interval,
                                         boolean withoutTime) {

        Date date = checkDate != null ? new Date(checkDate.getTime()) : null;
        if (date == null) {
            return false;
        }

        Date creationDate = interval.getCreationDate() != null ? new Date(interval.getCreationDate().getTime()) : null;
        Date expirationDate = interval.getExpirationDate() != null ? new Date(interval.getExpirationDate().getTime()) : null;

        if (withoutTime) {
            date = getDateWithoutTime(date);
            creationDate = getDateWithoutTime(creationDate);
            expirationDate = getDateWithoutTime(expirationDate);
        }

        return creationDate == null &&
                expirationDate == null || creationDate != null &&
                ((date.compareTo(creationDate) == 0) || date.after(creationDate)) &&
                expirationDate == null || expirationDate != null &&
                ((date.compareTo(expirationDate) == 0) || date.before(expirationDate)) &&
                creationDate == null || creationDate != null &&
                expirationDate != null &&
                ((date.compareTo(creationDate) == 0) || date.after(creationDate)) &&
                ((date.compareTo(expirationDate) == 0) || date.before(expirationDate));
    }

    
    /*****************/
    /**
     * <p>Checks if two dates are on the same day ignoring time.</p>
     * @param date1  the first date, not altered, not null
     * @param date2  the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    
    /**
     * <p>Checks if a date is today.</p>
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }
    
    /**
     * <p>Checks if a calendar date is today.</p>
     * @param cal  the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }
    
    /**
     * <p>Checks if the first date is before the second date ignoring time.</p>
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if the first calendar date is before the second calendar date ignoring time.</p>
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * <p>Checks if the first date is after the second date ignoring time.</p>
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if the first calendar date is after the second calendar date ignoring time.</p>
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * <p>Checks if a date is after today and within a number of days in the future.</p>
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }
    
    /**
     * <p>Checks if a calendar date is after today and within a number of days in the future.</p>
     * @param cal the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && ! isAfterDay(cal, future));
    }
    
    /** Returns the given date with the time set to the start of the day. */
    public static Date getStart(Date date) {
        return clearTime(date);
    }
    
    /** Returns the given date with the time values cleared. */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }    

    /** Determines whether or not a date has any time values (hour, minute, 
     * seconds or millisecondsReturns the given date with the time values cleared. */

    /**
     * Determines whether or not a date has any time values.
     * @param date The date.
     * @return true iff the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    /** Returns the given date with time set to the end of the day */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /** 
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date. 
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }
    
    /** 
     * Returns the minimum of two dates. A null date is treated as being greater
     * than any non-null date. 
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }
    
    /**
     * 
     * @param dateOfBirth
     * @return
     */
    public static int getAge(Date dateOfBirth) {
    	long ageInDay = DateUtils.getDiffInDays(today(), dateOfBirth);
    	int ageInYear = (int) (ageInDay / 365);
    	return ageInYear;
    }

    /**
	 * 
	 * @param millsec
	 * @return
	 */
    public static long milliSec2Days(long millsec) {
		return (long) (millsec / (1000 * 60 * 60 * 24));
	}
    
    /**
     * 
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
	    final GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }
}
