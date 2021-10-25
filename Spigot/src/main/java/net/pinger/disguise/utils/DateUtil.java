package net.pinger.disguise.utils;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateUtil {

    public static PeriodFormatter getFormatter() {
        return new PeriodFormatterBuilder()
                .appendYears()
                .appendSuffix(" Year ", " Years ")
                .appendMonths()
                .appendSuffix(" Month ", " Months ")
                .appendDays()
                .appendSuffix(" Day ", " Days ")
                .appendHours()
                .appendSuffix(" Hour ", " Hours ")
                .appendMinutes()
                .appendSuffix(" Minute ", " Minutes ")
                .appendSeconds()
                .appendSuffix(" Second ", " Seconds ").toFormatter();
    }

    public static String getFormattedTime(DateTime x, DateTime y) {
        return getFormatter().print(new Period(x, y)).length() > 0 ?
                getFormatter().print(new Period(x, y)) : "Now";
    }


}
