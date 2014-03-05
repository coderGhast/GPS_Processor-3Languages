package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPRMC {

    private GPSTime time;
    private String status;
    private String latitude;
    private String longitude;

    public GPRMC(String[] sentenceComponents) {
        setTime(sentenceComponents[1], sentenceComponents[9]);
        setStatus(sentenceComponents[2]);
        setLatitude(sentenceComponents[3], sentenceComponents[4].charAt(0));
        setLongitude(sentenceComponents[5], sentenceComponents[6].charAt(0));
    }

    public GPSTime getTime() {
        return time;
    }

    public void setTime(String stringTime, String stringDate) {
        time = new GPSTime();
        time.setHours(Integer.parseInt(stringTime.substring(0,2)));
        time.setMinutes(Integer.parseInt(stringTime.substring(2, 4)));
        time.setSeconds(Integer.parseInt(stringTime.substring(4,6)));

        time.setDay(Integer.parseInt(stringDate.substring(0,2)));
        time.setMonth(Integer.parseInt(stringDate.substring(2,4)));
        time.setYear(Integer.parseInt(stringDate.substring(4,6)));
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
        double tempLat = Double.parseDouble(stringLatitude);
        if(compass == 'S'){
            tempLat *= -1;
        }
        latitude = String.valueOf(tempLat);
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String stringLongitude, char compass) {
        double tempLng = Double.parseDouble(stringLongitude);
        if(compass == 'W'){
            tempLng *= -1;
        }
        longitude = String.valueOf(tempLng);
    }
}
