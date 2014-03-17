package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPGBS extends GenericSentence {
    public GPGBS(String[] sentenceComponents, String date) {
        setDate_and_time(sentenceComponents[1], date);
    }
}
