package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPRMC {

    private String time;
    private String date;
    private String status;
    private String latitude;
    private String longitude;

    public GPRMC(String[] sentenceComponents) {
        setTime(sentenceComponents[1]);
        setDate(sentenceComponents[9]);
        setStatus(sentenceComponents[2]);
        setLatitude(sentenceComponents[3], sentenceComponents[4].charAt(0));
        setLongitude(sentenceComponents[5], sentenceComponents[6].charAt(0));
    }

    public String getTime() {
        return appendDate(date) + appendTime(time);
    }

    public void setTime(String stringTime) {
        time = stringTime;
    }

    public void setDate(String stringDate){
        date = stringDate;
    }

    private String appendDate(String stringDate){
        StringBuilder sb = new StringBuilder();
        sb.append(20);
        sb.append(stringDate.substring(4, 6));
        sb.append("-");
        sb.append(stringDate.substring(2, 4));
        sb.append("-");
        sb.append(stringDate.substring(0, 2));

        return sb.toString();
    }

    private String appendTime(String stringTime){
        StringBuilder sb = new StringBuilder();
        sb.append("T");
        sb.append(stringTime.substring(0,2));
        sb.append(":");
        sb.append(stringTime.substring(2,4));
        sb.append(":");
        sb.append(stringTime.substring(4,6));
        sb.append("Z");

        return sb.toString();
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
