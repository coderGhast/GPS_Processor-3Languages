import sentences.*;
import utilities.DateAndTime;

/**
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public class SentenceHandler {

    private DateAndTime date_and_time;
    private String elevation;
    private String latitude;
    private String longitude;
    private double latitude_offset;
    private double longitude_offset;

    private GPGSA gpgsa = null;
    private GPRMC gprmc = null;

    protected void parse(String currentSentence){
        deconstructSentence(currentSentence);
    }

    private void deconstructSentence(String sentence){
        String[] sentenceComponents = sentence.split(",");
        int sentenceType = determineType(sentenceComponents[0]);

        switch(sentenceType){
            case(1):
                gpgsa = new GPGSA(sentenceComponents);
                break;
            case(2):
                gprmc = new GPRMC(sentenceComponents);
                setDate_and_time(gprmc.getTime());
                break;
            case(3):
                GPGGA gpgga = new GPGGA(sentenceComponents);
                break;
            case(4):
                GPGSV gpgsv = new GPGSV(sentenceComponents);
                break;
            case(5):
                GPZDA gpzda = new GPZDA(sentenceComponents);
                break;
            case(6):
                GPGBS gpgbs = new GPGBS(sentenceComponents);
                break;
            default:;
        }
    }

    private int determineType(String sentenceSignifier){
        int sentenceType = 0;
        if(sentenceSignifier.equals("$GPGSA")){
            sentenceType = 1;
        } else if (sentenceSignifier.equals("$GPRMC")){
            sentenceType = 2;
        } else if (sentenceSignifier.equals("$GPGGA")){
            sentenceType = 3;
        } else if (sentenceSignifier.equals("$GPGSV")){
            sentenceType = 4;
        } else if (sentenceSignifier.equals("$GPZDA")){
            sentenceType = 5;
        } else if (sentenceSignifier.equals("$GPGBS")){
            sentenceType = 6;
        } else {
            sentenceType = 0;
            System.out.println("Sentence type: " +  sentenceSignifier + " is not handled by this application.");
        }
        return sentenceType;
    }


    private void printData(GPRMC gprmc){
        System.out.println(gprmc.getTime());
        System.out.println(gprmc.getLatitude() + ", " + gprmc.getLongitude());
    }

    public DateAndTime getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(DateAndTime date_and_time) {

        this.date_and_time = date_and_time;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getLatitude_offset() {
        return latitude_offset;
    }

    public void setLatitude_offset(double latitude_offset) {
        this.latitude_offset = latitude_offset;
    }

    public double getLongitude_offset() {
        return longitude_offset;
    }

    public void setLongitude_offset(double longitude_offset) {
        this.longitude_offset = longitude_offset;
    }
}
