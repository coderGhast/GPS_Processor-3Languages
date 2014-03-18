package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPGBS extends GenericSentence {
    public GPGBS(String[] sentenceComponents, int day, int month, int year) {
        setStringCalendarTime(sentenceComponents[1], day, month, year);
    }
}
