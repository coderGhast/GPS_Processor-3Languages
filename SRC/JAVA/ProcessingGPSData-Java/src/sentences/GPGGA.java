package sentences;

/**
 * Get the relevant data from a GPGGA.
 *
 * Created by jee22 on 05/03/14.
 */
public class GPGGA extends GenericSentence {
    /**
     * What type of fix is this?
     */
    private int typeOfFix;

    /**
     * The elevation of the device.
     */
    private String elevation;

    /**
     * Take the details for the sentence on it's creation.
     * @param sentenceComponents - The String containing the full sentence.
     * @param day - The last recorded day from a sentence containing the date.
     * @param month - The last recorded month from a sentence containing the date.
     * @param year - The last recorded year from a sentence containing the date.
     */
    public GPGGA(String[] sentenceComponents, int day, int month, int year) {
        setStringCalendarTime(sentenceComponents[1], day, month, year);
        setLatitude(sentenceComponents[2], sentenceComponents[3].charAt(0));
        setLongitude(sentenceComponents[4], sentenceComponents[5].charAt(0));
        setTypeOfFix(Integer.parseInt(sentenceComponents[6]));
        setElevation(sentenceComponents[9]);
    }

    /**
     * Set the type of fix.
     * @param typeOfFix The type of fix.
     */
    public void setTypeOfFix(int typeOfFix){
        this.typeOfFix = typeOfFix;
    }

    /**
     * Get the type of fix
     * @return return the type of fix.
     */
    public int getTypeOfFix(){
        return typeOfFix;
    }

    /**
     * Get the elevation
     * @return The elevation.
     */
    public String getElevation() {
        return elevation;
    }

    /**
     * Set the elevation
     * @param elevation - The elevation to be set.
     */
    public void setElevation(String elevation) {
        this.elevation = elevation;
    }
}
