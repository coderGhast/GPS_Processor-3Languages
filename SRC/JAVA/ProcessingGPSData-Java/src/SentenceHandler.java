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
    private int currentFix;
    private boolean good_signal;
    private double latitude_offset;
    private double longitude_offset;


    protected void parse(String currentSentence){
        deconstructSentence(currentSentence);
    }

    private void deconstructSentence(String sentence){
        String[] sentenceComponents = sentence.split(",");
        int sentenceType = determineType(sentenceComponents[0]);
        GenericSentence current_sentence;

        switch(sentenceType){
            case(1):
                GPGSA gpgsa = new GPGSA(sentenceComponents);
                setCurrentFix(gpgsa.getFixType());
                break;
            case(2):
                current_sentence = new GPRMC(sentenceComponents);
                if(this.getDate_and_time() == null){
                    setDate_and_time(current_sentence.getDate_and_time());
                } else if(this.getDate_and_time().compareTo(current_sentence.getDate_and_time()) != 0){
                    setDate_and_time(current_sentence.getDate_and_time());
                }
                if(this.getLatitude() != current_sentence.getLatitude()){
                    setLatitude(current_sentence.getLatitude());
                }
                if(this.getLongitude() != current_sentence.getLongitude()){
                    setLongitude(current_sentence.getLongitude());
                }
                break;
            case(3):
                if(this.getDate_and_time() == null){
                    current_sentence = new GPGGA(sentenceComponents, null);
                } else {
                    current_sentence = new GPGGA(sentenceComponents, String.valueOf(this.getDate_and_time().getDate()));
                    if(this.getDate_and_time().compareTo(current_sentence.getDate_and_time()) != 0){
                        setDate_and_time(current_sentence.getDate_and_time());
                    }
                }

                if(this.getLatitude() != current_sentence.getLatitude()){
                    this.setLatitude(current_sentence.getLatitude());
                }
                if(this.getLongitude() != current_sentence.getLongitude()){
                    this.setLongitude(current_sentence.getLongitude());
                }
                break;
            case(4):
                current_sentence = new GPGSV(sentenceComponents);
                break;
            case(5):
                current_sentence = new GPZDA(sentenceComponents);
                break;
            case(6):
                current_sentence = new GPGBS(sentenceComponents);
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

    public int getCurrentFix(){
        return currentFix;
    }

    public void setCurrentFix(int currentFix){
        this.currentFix = currentFix;
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
