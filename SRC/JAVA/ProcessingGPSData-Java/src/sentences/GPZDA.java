package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPZDA extends GenericSentence {

    public GPZDA(String[] sentenceComponents) {
        StringBuilder dateString = new StringBuilder();
        dateString.append(sentenceComponents[2]);
        dateString.append(sentenceComponents[3]);
        /**
         * Remove the '20' in '2014'. Not perfect, but best
         * you're going to get when comparing to the GPRMC
         * that doesn't include it either.
         */
        dateString.append(sentenceComponents[4].substring(2,4));

        setDate_and_time(sentenceComponents[1], dateString.toString());
    }
}
