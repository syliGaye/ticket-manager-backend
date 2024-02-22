package org.jpa.ticketmanagerbackend.utilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Utility {
    public static boolean isLong(String string) {
        try {
            Long.parseLong(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotLong(String string) {
        return !isLong(string);
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotDouble(String string) {
        return !isDouble(string);
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotInt(String string) {
        return !isInt(string);
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotFloat(String string) {
        return !isFloat(string);
    }

    public static boolean isBigDecimal(String string) {
        try {
            new BigDecimal(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotBigDecimal(String string) {
        return !isBigDecimal(string);
    }

    public static boolean isBigInteger(String string) {
        try {
            new BigInteger(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotBigInteger(String string) {
        return !isBigInteger(string);
    }

    public static boolean isShort(String string) {
        try {
            Short.parseShort(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotShort(String string) {
        return !isShort(string);
    }

    public static boolean isDigit(String string) {
        return isLong(string) || isBigDecimal(string) || isDouble(string) || isBigInteger(string) || isInt(string)
                || isFloat(string) || isShort(string);
    }

    public static boolean isNotDigit(String string) {
        return !isDigit(string);
    }

    public static boolean isSplitedArray(String string, String regex) {
        if (regex == null || regex.isEmpty())
            return false;
        try {
            string.split(regex);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isNotSplitedArray(String string, String regex) {
        return !isSplitedArray(string, regex);
    }

    public static boolean isLocalDate(String string) {
        try {
            LocalDate.parse(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isNotLocalDate(String string) {
        return !isLocalDate(string);
    }

    public static boolean isDate(String string, SimpleDateFormat format) {
        try {
            format.parse(string);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isNotDate(String string) {
        return !isDate(string, new SimpleDateFormat());
    }

    public static boolean isBoolean(String string) {
        return Boolean.parseBoolean(string);
    }

    public static boolean isNotBoolean(String string) {
        return !isBoolean(string);
    }

    public static long toLong(String string) {
        if (string != null && isLong(string)) {
            return Long.parseLong(string);
        } else {
            return -1L;
        }
    }

    public static double toDouble(String string) {
        if (string != null && isDouble(string)) {
            return Double.parseDouble(string);
        } else {
            return 0.0;
        }
    }

    public static int toInt(String string) {
        if (string != null && isInt(string)) {
            return Integer.parseInt(string);
        } else {
            return 0;
        }
    }

    public static float toFloat(String string) {
        if (string != null && isFloat(string)) {
            return Float.parseFloat(string);
        } else {
            return Float.MIN_VALUE;
        }
    }

    public static BigDecimal toBigDecimal(String string) {
        if (string != null && isBigDecimal(string)) {
            return new BigDecimal(string);
        } else {
            return BigDecimal.valueOf(0);
        }
    }

    public static BigInteger toBigInteger(String string) {
        if (string != null && isBigInteger(string)) {
            return new BigInteger(string);
        } else {
            return BigInteger.valueOf(0);
        }
    }

    public static short toShort(String string) {
        if (string != null && isShort(string)) {
            return Short.parseShort(string);
        } else {
            return Short.MIN_VALUE;
        }
    }

    public static List<String> split(String string, String regex) {
        if (string != null && regex != null && isSplitedArray(string, regex)) {
            return List.of(string.split(regex));
        } else {
            return List.of();
        }
    }

    public static LocalDate toLocalDate(String string, DateTimeFormatter format) {
        if (string != null && isLocalDate(string)) {
            if (format != null) {
                return LocalDate.parse(string, format);
            } else {
                return LocalDate.parse(string);
            }
        } else {
            return LocalDate.MIN;
        }
    }

    public static Date toDate(String string, SimpleDateFormat format) {
        try {
            return format.parse(string);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static boolean isBeforeOrEquals(Date date1, Date date2) {
        return date1.before(date2) || date1.equals(date2);
    }

    public static boolean isAfterOrEquals(Date date1, Date date2) {
        return date1.after(date2) || date1.equals(date2);
    }

    public static int getAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
