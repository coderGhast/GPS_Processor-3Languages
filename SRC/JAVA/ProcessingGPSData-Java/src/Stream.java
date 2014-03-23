import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Details all the data and methods to do with
 * a Stream as the sentences are parsed through
 * the application.
 *
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public class Stream {
    /**
     * The date and time of the current/last known sentence.
     */
    private Calendar date_and_time;
    /**
     * The last registered elevation.
     */
    private double elevation;
    /**
     * The last registered Latitude.
     */
    private double latitude;
    /**
     * The last registered Longitude.
     */
    private double longitude;
    /**
     * How many satellites were last registered that
     * had SNRs between 30 and 35.
     */
    private int signalsBetween30And35 = 0;
    /**
     * How many satellites were last registered that had
     * SNRs above 35 (exceptional!)
     */
    private int signals35AndAbove = 0;


    /**
     * Get the date and time.
     *
     * @return Calendar date and time.
     */
    public Calendar getDate_and_time() {
        return date_and_time;
    }

    /**
     * Get the elevation currently set.
     *
     * @return The elevation last held here.
     */
    public double getElevation() {
        return elevation;
    }

    /**
     * Set the passed elevation as the current.
     *
     * @param string_elevation The elevation to be set.
     */
    public void setElevation(String string_elevation) {

        this.elevation = Double.parseDouble(string_elevation);
    }

    /**
     * Get the last recorded Latitude.
     *
     * @return The Latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the Latitude based on a passed String and the facing of
     * the direction.
     *
     * @param stringLatitude - Latitude position
     * @param compass        - Direction of compass
     */
    public void setLatitude(String stringLatitude, char compass) {
        /**
         * Move the period index of the Latitude to be correct for
         * GPX format.
         */
        int periodIndex = stringLatitude.indexOf(".");
        int start_of_minutes = periodIndex - 2;
        int degrees = Integer.parseInt(stringLatitude.substring(0, start_of_minutes));
        String string_minutes = stringLatitude.substring(start_of_minutes, stringLatitude.length());
        double minutes = Double.parseDouble(string_minutes);

        minutes = degrees + (minutes / 60);

        /**
         * If the compass is South, make the Latitude negative.
         */
        if (compass == 'S') {
            minutes = minutes * -1;
        }

        latitude = minutes;
    }

    /**
     * Set the Latitude based on a double.
     * @param lat Latitude
     */
    public void setLatitudeVal(double lat){
        latitude = lat;
    }

    /**
     * Get the last recorded Longitude.
     *
     * @return The Longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the Longitude based on a passed String and the facing of
     * the direction.
     *
     * @param stringLongitude - Longitude position
     * @param compass         - Direction of compass
     */
    public void setLongitude(String stringLongitude, char compass) {
        /**
         * Move the period index of the Longitude to be correct for
         * GPX format.
         */
        int periodIndex = stringLongitude.indexOf(".");
        int start_of_minutes = periodIndex - 2;
        int degrees = Integer.parseInt(stringLongitude.substring(0, start_of_minutes));
        String string_minutes = stringLongitude.substring(start_of_minutes, stringLongitude.length());
        double minutes = Double.parseDouble(string_minutes);

        minutes = minutes / 60;
        minutes = degrees + minutes;

        /**
         * If the compass is South, make the Latitude negative.
         */
        if (compass == 'W') {
            minutes = minutes * -1;
        }

        longitude = minutes;
    }

    /**
     * Set the Longitude based on a double
     * @param lng Longitude
     */
    public void setLongitudeVal(double lng){
        longitude = lng;
    }

    /**
     * Get the number of satellites with signals between 30 and 35.
     *
     * @return The amount.
     */
    public int getSignalsBetween30And35() {
        return signalsBetween30And35;
    }

    public void setSignalsBetween30And35(int snr_num){
        signalsBetween30And35 = snr_num;
    }

    /**
     * Get the number of satellites with signals 35 and above.
     *
     * @return The amount.
     */
    public int getSignals35AndAbove() {
        return signals35AndAbove;
    }

    /**
     * Set the SNR values above 35
     * @param snr_num
     */
    public void set35AndAboveSNR(int snr_num){
        signals35AndAbove = snr_num;
    }

    /**
     * Increment the SNR value integer of where an snr value fits,
     * if it fits, to keep track of the signal strength and number
     * of satellites.
     * @param snr SNR value.
     */
    public void calcSNRAmounts(String snr){
        int snr_val = Integer.parseInt(snr);
        if(snr_val >= 35){
            signals35AndAbove++;
        } else if (snr_val >= 30 && snr_val < 35){
            signalsBetween30And35++;
        }
    }

    /**
     * Set the Time of a Sentence when it only has the Time and no Date
     *
     * @param time  - The Time, as a full String, from a sentence.
     */
    public void updateTime(String time) {
        if(date_and_time != null){
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(2, 4));
            int second = Integer.parseInt(time.substring(4, 6));

            date_and_time.set(Calendar.HOUR_OF_DAY, hour);
            date_and_time.set(Calendar.MINUTE, minute);
            date_and_time.set(Calendar.SECOND, second);

            date_and_time.set(Calendar.MILLISECOND, 0);
        }
    }

    /**
     * Setting the date and time based on given Strings from
     * the sentences.
     *
     * @param time - The Time String from a sentence
     * @param date - The Date String from a sentence.
     */
    public void setDateAndTime(String time, String date) {
        date_and_time = new GregorianCalendar();
        int year = Integer.parseInt(date.substring(4, 6)) + 2000;
        int month = Integer.parseInt(date.substring(2, 4)) - 1;
        int day = Integer.parseInt(date.substring(0, 2));
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(2, 4));
        int second = Integer.parseInt(time.substring(4, 6));
        /**
         * Year, Month, Day, Hour, Minute, Second
         */
        date_and_time.set(year, month, day, hour, minute, second);
        /**
         * Set the milliseconds of each Calendar to avoid the compareTo
         * giving incorrect comparisons when the time IS the same, but
         * milliseconds are messed up due to default implementation.
         */
        date_and_time.set(Calendar.MILLISECOND, 0);
    }
}