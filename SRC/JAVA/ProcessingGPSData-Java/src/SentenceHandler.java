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
     * Start the class with a new list for the satellites.
     */
    public SentenceHandler(){

    }

    /**
     * Sent a sentence to the DECONSTRUCTION.
     * @param currentSentence The sentence to be parsed.
     */
    protected void parse(Stream streamer, String currentSentence){
        deconstructSentence(streamer, currentSentence);
    }

    /**
     * Takes a sentence and finds out what type it is and
     * passes it to the appropriate method.
     * @param sentence The sentence to be deconstructed.
     */
    private void deconstructSentence(Stream streamer, String sentence){
        if(sentence != null){
            String[] sentenceComponents = sentence.split(",");
            if (sentenceComponents[0].equals("$GPRMC")){
                handleGPRMC(streamer, sentenceComponents);
            } else if (sentenceComponents[0].equals("$GPGGA")){
                handleGPGGA(streamer, sentenceComponents);
            } else if (sentenceComponents[0].equals("$GPGSV")){
                handleGPGSV(streamer, sentenceComponents);
            }
        }
    }

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPRMC(Stream streamer, String[] sentenceComponents){
        /**
         * Set the date and time. The GPRMC is the only sentence, aside GPZDA,
         * that provides a Date for the timestamps..
         */
        streamer.setDateAndTime(sentenceComponents[1], sentenceComponents[9]);
        streamer.setLatitude(sentenceComponents[3], sentenceComponents[4].charAt(0));
        streamer.setLongitude(sentenceComponents[5], sentenceComponents[6].charAt(0));
    }

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPGGA(Stream streamer, String[] sentenceComponents){
        streamer.updateTime(sentenceComponents[1]);
        streamer.setLatitude(sentenceComponents[2], sentenceComponents[3].charAt(0));
        streamer.setLongitude(sentenceComponents[4], sentenceComponents[5].charAt(0));
        streamer.setElevation(sentenceComponents[9]);
    }

    /**
     *
     * @param sentenceComponents The full sentence.
     */
    private void handleGPGSV(Stream streamer, String[] sentenceComponents){
        /**
         * Looks to see if this is the first sentence of the current group. If it
         * is, then the amounts of SNR are reset.
         */
        if(sentenceComponents[2].equals("1")){
            streamer.set35AndAboveSNR(0);
            streamer.setSignalsBetween30And35(0);
        }
        String snr;
        /**
         * For all of the amounts of Satellites and SNR in the sentence,
         * go through and send them to be added into the Stream. First
         * check that they are not the last of the line, by looking
         * for the asterisk of the check sum.
         *
         * If the asterisk is there, get everything from before it.
         */
        for(int i = 7; i < sentenceComponents.length; i+=4){
            snr = sentenceComponents[i];
            if(snr.contains("*")){
                int asterisk = snr.indexOf('*');
                snr = snr.substring(0, asterisk);
            }

            if(!snr.equals("")){
                streamer.calcSNRAmounts(snr);
            }
        }
    }
}
