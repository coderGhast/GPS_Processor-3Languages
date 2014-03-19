import sentences.*;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * SentenceHandler deals with taking in sentences from
 * the StreamReaders, determines what type of sentences
 * they are and then assigns data to the correct place
 * in order to be used in plotting a route.
 *
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public class SentenceHandler {
    /**
     * The date and time of the current/last known sentence.
     */
    private Calendar date_and_time;
    /**
     * The last registered elevation.
     */
    private String elevation;
    /**
     * The last registered Latitude.
     */
    private String latitude;
    /**
     * The last registered Longitude.
     */
    private String longitude;
    /**
     * How many satellites were last registered that
     * had SNRs between 30 and 35.
     */
    private int signalsBetween30And35 = 0;
    /**
     * How many satellites were last registered that had
     * SNRs above 35 (exceptional!)
     */
    private int signalsAbove35 = 0;
    /**
     * The list of all satellites from all recent GPGSVs in
     * the last GPGSV collection that had SNRs.
     */
    private LinkedList<Integer> satellitesWithSNR;
    /**
     * The expected GPGSV number of the current collection.
     */
    private int expectedGSVNumber = 1;

    /**
     * Start the class with a new list for the satellites.
     */
    public SentenceHandler(){
        satellitesWithSNR = new LinkedList<Integer>();
    }

    /**
     * Sent a sentence to the DECONSTRUCTION.
     * @param currentSentence The sentence to be parsed.
     */
    protected void parse(String currentSentence){
        deconstructSentence(currentSentence);
    }

    /**
     * Takes a sentence and finds out what type it is and
     * passes it to the appropriate method.
     * @param sentence The sentence to be deconstructed.
     */
    private void deconstructSentence(String sentence){
        String[] sentenceComponents = sentence.split(",");
        int sentenceType = determineType(sentenceComponents[0]);

        switch(sentenceType){
            case(1):
                /**
                 * Do nothing with a GPGSA. No info is taken
                 * from it.
                 */
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
            default:
        }
    }

    /**
     * Determine the type of the sentence from the first
     * value in the .dat CSV file.
     * @param sentenceSignifier The start of the sentence
     * @return The integer value that represents the sentence.
     */
    private int determineType(String sentenceSignifier){
        int sentenceType;
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

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPRMC(String[] sentenceComponents){
        GPRMC current_sentence = new GPRMC(sentenceComponents);
        if(this.getDate_and_time() == null){
            setDate_and_time(current_sentence.getDate_and_time());
        } else if(date_and_time.compareTo(current_sentence.getDate_and_time()) != 0){
            setDate_and_time(current_sentence.getDate_and_time());
        }

        compareLatitude(current_sentence);
        compareLongitude(current_sentence);
    }

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPGGA(String[] sentenceComponents){
        GPGGA current_sentence;
      if(date_and_time != null){
            current_sentence = new GPGGA(sentenceComponents, date_and_time.get(Calendar.DAY_OF_MONTH), date_and_time.get(Calendar.MONTH), date_and_time.get(Calendar.YEAR));
            if(date_and_time.compareTo(current_sentence.getDate_and_time()) != 0){
                setDate_and_time(current_sentence.getDate_and_time());
            }
          compareLatitude(current_sentence);
          compareLongitude(current_sentence);
          setElevation(current_sentence.getElevation());
        }
    }

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPGSV(String[] sentenceComponents){
        GPGSV current_sentence = new GPGSV(sentenceComponents);

        if(current_sentence.getSentenceNumber() == expectedGSVNumber){
            satellitesWithSNR.addAll(current_sentence.getSatellitesWithSNR());
        } else {
            expectedGSVNumber = current_sentence.getSentenceNumber();
            satellitesWithSNR.clear();
            satellitesWithSNR.addAll(current_sentence.getSatellitesWithSNR());
        }
        checkForValidSignal();
        expectedGSVNumber++;
    }

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPZDA(String[] sentenceComponents){
        GPZDA current_sentence = new GPZDA(sentenceComponents);
        if(this.getDate_and_time() == null){
            setDate_and_time(current_sentence.getDate_and_time());
        } else {
            if(date_and_time.compareTo(current_sentence.getDate_and_time()) != 0 ){
                setDate_and_time(current_sentence.getDate_and_time());
            }
        }
    }

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPGBS(String[] sentenceComponents){
        GPGBS current_sentence;
        if(this.getDate_and_time() != null){
            current_sentence = new GPGBS(sentenceComponents, date_and_time.get(Calendar.DATE), date_and_time.get(Calendar.MONTH), date_and_time.get(Calendar.YEAR));
            if(date_and_time.compareTo(current_sentence.getDate_and_time()) != 0 ){
                setDate_and_time(current_sentence.getDate_and_time());
            }
        }
    }

    /**
     * Check to see how many SNR values in a list are between
     * 30 and 34, or 35 or above.
     */
    private void checkForValidSignal(){
        signalsBetween30And35 = 0;
        signalsAbove35 = 0;
        for(int current_satellite : satellitesWithSNR){
            if( current_satellite >= 30 && current_satellite < 35){
                signalsBetween30And35++;
            }
            if (current_satellite >= 35){
                signalsAbove35++;
            }
        }
    }

    /**
     * Compare the current Latitude value with one just parsed in.
     * @param current_sentence The full sentence.
     */
    private void compareLatitude(GenericSentence current_sentence){
        if(this.getLatitude() == null){
            this.setLatitude(current_sentence.getLatitude());
        } else if(!this.getLatitude().equals(current_sentence.getLatitude())){
            this.setLatitude(current_sentence.getLatitude());
        }
    }

    /**
     * Compare the current Longitude value with one just parsed in.
     * @param current_sentence The full sentence.
     */
    private void compareLongitude(GenericSentence current_sentence){
        if(this.getLongitude() == null){
            this.setLongitude(current_sentence.getLongitude());
        } else if(!this.getLongitude().equals(current_sentence.getLongitude())){
            this.setLongitude(current_sentence.getLongitude());
        }
    }

    /**
     * Get the date and time.
     * @return Calendar date and time.
     */
    public Calendar getDate_and_time() {
        return date_and_time;
    }

    /**
     * Set the date and time
     * @param date_and_time The date and time to be set.
     */
    public void setDate_and_time(Calendar date_and_time) {
        this.date_and_time = (Calendar) date_and_time.clone();
    }

    /**
     * Get the elevation currently set.
     * @return The elevation last held here.
     */
    public String getElevation() {
        return elevation;
    }

    /**
     * Set the passed elevation as the current.
     * @param elevation The elevation to be set.
     */
    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    /**
     * Get the last recorded Latitude.
     * @return The Latitude.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Set the passed Latitude
     * @param latitude to be set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the last recorded Longitude.
     * @return The Longitude.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Set the passed Longitude
     * @param longitude to be set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Get the number of satellites with signals between 30 and 35.
     * @return The amount.
     */
    public int getSignalsBetween30And35() {
        return signalsBetween30And35;
    }

    /**
     * Get the number of satellites with signals 35 and above.
     * @return The amount.
     */
    public int getSignalsAbove35() {
        return signalsAbove35;
    }
}
