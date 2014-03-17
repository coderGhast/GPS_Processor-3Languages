import sentences.*;
import utilities.DateAndTime;

import java.util.LinkedList;

/**
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public class SentenceHandler {

    private DateAndTime date_and_time;
    private String elevation;
    private String latitude;
    private String longitude;
    private int typeOfFix;
    private int fixQuality;
    private boolean valid_signal;
    private double latitude_offset;
    private double longitude_offset;
    private LinkedList<Integer> satellitesWithSNR;
    private int expectedGSVNumber = 1;

    public SentenceHandler(){
        satellitesWithSNR = new LinkedList<Integer>();
    }

    protected void parse(String currentSentence){
        deconstructSentence(currentSentence);
    }

    private void deconstructSentence(String sentence){
        String[] sentenceComponents = sentence.split(",");
        int sentenceType = determineType(sentenceComponents[0]);

        switch(sentenceType){
            case(1):
                handleGPGSA(sentenceComponents);
                break;
            case(2):
                handleGPRMC(sentenceComponents);
                break;
            case(3):
                handleGPGGA(sentenceComponents);
                break;
            case(4):
                handleGPGSV(sentenceComponents);
                break;
            case(5):
                handleGPZDA(sentenceComponents);
                break;
            case(6):
                handleGPGBS(sentenceComponents);
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

    private void handleGPGSA(String[] sentenceComponents){
        GPGSA gpgsa = new GPGSA(sentenceComponents);
        setTypeOfFix(gpgsa.getTypeOfFix());
    }

    private void handleGPRMC(String[] sentenceComponents){
        GPRMC current_sentence = new GPRMC(sentenceComponents);
        if(this.getDate_and_time() == null){
            setDate_and_time(current_sentence.getDate_and_time());
        } else if(this.getDate_and_time().compareTo(current_sentence.getDate_and_time()) != 0){
            setDate_and_time(current_sentence.getDate_and_time());
        }

        compareLatitude(current_sentence);
        compareLongitude(current_sentence);
    }

    private void handleGPGGA(String[] sentenceComponents){
        GPGGA current_sentence;
        if(this.getDate_and_time() == null){
            current_sentence = new GPGGA(sentenceComponents, null);
        } else {
            current_sentence = new GPGGA(sentenceComponents, String.valueOf(this.getDate_and_time().getTime()));
            int timeDifference = this.getDate_and_time().compareTo(current_sentence.getDate_and_time());
            if(timeDifference != 0 ){
                setDate_and_time(current_sentence.getDate_and_time());
            }
        }

        compareLatitude(current_sentence);
        compareLongitude(current_sentence);
        setTypeOfFix(current_sentence.getTypeOfFix());
        setElevation(current_sentence.getElevation());
    }

    private void handleGPGSV(String[] sentenceComponents){
        GPGSV current_sentence = new GPGSV(sentenceComponents);

        if(current_sentence.getSentenceNumber() == expectedGSVNumber){
            satellitesWithSNR.addAll(current_sentence.getSatellitesWithSNR());
        } else {
            expectedGSVNumber = current_sentence.getSentenceNumber();
            satellitesWithSNR.clear();
            satellitesWithSNR.addAll(current_sentence.getSatellitesWithSNR());
        }
        expectedGSVNumber++;
        //System.out.println("Num of satellitesWithSNR at end of GPGSV: " + satellitesWithSNR.size());
    }

    public int getNumSatellitesWithSNR(){
        return satellitesWithSNR.size();
    }

    private void handleGPZDA(String[] sentenceComponents){

    }

    private void handleGPGBS(String[] sentenceComponents){

    }

    private void compareLatitude(GenericSentence current_sentence){
        if(this.getLatitude() != current_sentence.getLatitude()){
            this.setLatitude(current_sentence.getLatitude());
        }
    }

    private void compareLongitude(GenericSentence current_sentence){
        if(this.getLongitude() != current_sentence.getLongitude()){
            this.setLongitude(current_sentence.getLongitude());
        }
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

    public int getTypeOfFix(){
        return typeOfFix;
    }

    public void setTypeOfFix(int typeOfFix){
        this.typeOfFix = typeOfFix;
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
