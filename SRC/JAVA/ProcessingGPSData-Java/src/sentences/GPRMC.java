package sentences;

/**
 * Handling the data about a GPRMC.
 *
 * Created by jee22 on 05/03/14.
 */
public class GPRMC extends GenericSentence {
    /**
     * Create a GPRMC sentence status thing from the String of sentencedom.
     * @param sentenceComponents The full sentence.
     */
    public GPRMC(String[] sentenceComponents) {
        /**
         * Set the date and time. The GPRMC is the only sentence, aside GPZDA,
         * that provides a Date for the timestamps..
         */
        setDate_and_time(sentenceComponents[1], sentenceComponents[9]);
        setLatitude(sentenceComponents[3], sentenceComponents[4].charAt(0));
        setLongitude(sentenceComponents[5], sentenceComponents[6].charAt(0));
    }

}
