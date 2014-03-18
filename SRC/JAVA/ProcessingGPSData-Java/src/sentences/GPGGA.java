package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPGGA extends GenericSentence {
    private int typeOfFix;
    private int numOfSatellites;
    private String elevation;

    public GPGGA(String[] sentenceComponents, int day, int month, int year) {
        setStringCalendarTime(sentenceComponents[1], day, month, year);
        setLatitude(sentenceComponents[2], sentenceComponents[3].charAt(0));
        setLongitude(sentenceComponents[4], sentenceComponents[5].charAt(0));
        setTypeOfFix(Integer.parseInt(sentenceComponents[6]));
        setNumOfSatellites(Integer.parseInt(sentenceComponents[7]));
        setElevation(sentenceComponents[9]);
    }

    public void setTypeOfFix(int typeOfFix){
        this.typeOfFix = typeOfFix;
    }

    public int getTypeOfFix(){
        return typeOfFix;
    }

    public void setNumOfSatellites(int numOfSatellites){
        this.numOfSatellites = numOfSatellites;
    }

    public int getNumOfSatellites(){
        return numOfSatellites;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }
}
