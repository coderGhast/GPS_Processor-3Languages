package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPGGA extends GenericSentence {
    public GPGGA(String[] sentenceComponents, String date) {
        setDate_and_time(sentenceComponents[1], date);
        setLatitude(sentenceComponents[2], sentenceComponents[3].charAt(0));
        setLongitude(sentenceComponents[4], sentenceComponents[5].charAt(0));
    }
}
