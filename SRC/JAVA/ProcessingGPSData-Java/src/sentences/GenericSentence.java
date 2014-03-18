package sentences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public abstract class GenericSentence {

    private String latitude;
    private String longitude;
    private Calendar date_and_time = new GregorianCalendar();

    public void setStringCalendarTime(String time, int day, int month, int year){
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(2, 4));
        int second = Integer.parseInt(time.substring(4, 6));
        /**
         * Year, Month, Day, Hour, Minute, Second
         */
        date_and_time.set(year, month, day, hour, minute, second);

    }

    public void setDate_and_time(String time, String date){
        int year = Integer.parseInt(date.substring(4,6)) + 2000;
        int month = Integer.parseInt(date.substring(2, 4)) - 1;
        int day = Integer.parseInt(date.substring(0, 2));
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(2, 4));
        int second = Integer.parseInt(time.substring(4, 6));
        /**
         * Year, Month, Day, Hour, Minute, Second
         */
        date_and_time.set(year, month, day, hour, minute, second);
    }

    public Calendar getDate_and_time() {
        return date_and_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String stringLatitude, char compass) {
        StringBuffer latString = new StringBuffer(stringLatitude);
        int periodIndex = latString.indexOf(".");
        latString = latString.deleteCharAt(periodIndex);
        latString.insert(periodIndex - 2, ".");

        if (compass == 'S') {
            latString.insert(0, "-");
        }
        latitude = latString.toString();
    }

    public void setLongitude(String stringLongitude, char compass) {
        StringBuffer longString = new StringBuffer(stringLongitude);
        int periodIndex = longString.indexOf(".");
        longString = longString.deleteCharAt(periodIndex);
        longString.insert(periodIndex - 2, ".");

        if (compass == 'W') {
            longString.insert(0, "-");
        }
        longitude = longString.toString();
    }
}
