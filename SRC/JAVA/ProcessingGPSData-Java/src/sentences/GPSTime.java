package sentences;

/**
 * Created by jee22 on 05/03/14.
 *
 * Represents a particular time (UTC) of
 * when a fix was taken.
 *
 * A NOTE:
 * If printing/XMLing the seconds, remember
 * to check if the digit is below 10. If it
 * is below 10, add a '0' before the digit
 * in the display to the user.
 */
public class GPSTime {

    private int seconds;
    private int minutes;
    private int hours;

    private int day;
    private int month;
    private int year;

    GPSTime(){

    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }




}
