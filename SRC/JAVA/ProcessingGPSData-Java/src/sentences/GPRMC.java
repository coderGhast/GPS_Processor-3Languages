package sentences;

import utilities.DateAndTime;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPRMC extends GenericSentence{

    private String status;

    public GPRMC(String[] sentenceComponents) {
        setDate_and_time(sentenceComponents[1], sentenceComponents[9]);
        setStatus(sentenceComponents[2]);
        setLatitude(sentenceComponents[3], sentenceComponents[4].charAt(0));
        setLongitude(sentenceComponents[5], sentenceComponents[6].charAt(0));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
