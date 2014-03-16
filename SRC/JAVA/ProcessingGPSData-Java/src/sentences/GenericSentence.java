package sentences;

import utilities.DateAndTime;

/**
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public abstract class GenericSentence {

    private String latitude;
    private String longitude;
    private DateAndTime date_and_time;

    public void setDate_and_time(String time, String date){
        date_and_time = new DateAndTime(time, date);
    }

    public DateAndTime getDate_and_time() {
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
