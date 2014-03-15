package sentences;

import utilities.DateAndTime;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPRMC {

    private DateAndTime date_and_time;
    private String status;
    private String latitude;
    private String longitude;

    public GPRMC(String[] sentenceComponents) {
        date_and_time = new DateAndTime(sentenceComponents[1], sentenceComponents[9]);
        setStatus(sentenceComponents[2]);
        setLatitude(sentenceComponents[3], sentenceComponents[4].charAt(0));
        setLongitude(sentenceComponents[5], sentenceComponents[6].charAt(0));
    }

    public DateAndTime getTime() {
        return date_and_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
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

    public String getLongitude() {
        return longitude;
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
