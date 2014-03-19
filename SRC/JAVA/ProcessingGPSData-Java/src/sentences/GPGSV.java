package sentences;

import java.util.LinkedList;

/**
 * Handle all data in a GPGSV sentence.
 *
 * Created by jee22 on 05/03/14.
 */
public class GPGSV extends GenericSentence {
    /**
     * What number of sentence is this in the current
     * sent out sentence collection?
     */
    private int sentenceNumber;
    /**
     * A list of Integers (SNRs) of the satellites
     * that have SNRs.
     */
    private LinkedList<Integer> satellitesWithSNR;

    /**
     * Dissect the data from the String of GPGSVs.
     * @param sentenceComponents The sentence as String.
     */
    public GPGSV(String[] sentenceComponents) {
        satellitesWithSNR = new LinkedList<Integer>();
        addSatellitesFromGSV(sentenceComponents);
        sentenceNumber = Integer.valueOf(sentenceComponents[2]);
    }

    /**
     * Go through the sentence and take out the SNRs from the
     * sentence and add them to the list.
     * @param sentenceComponents The sentence as a Srting
     */
    private void addSatellitesFromGSV(String[] sentenceComponents) {
        for (int i = 7; i <= sentenceComponents.length; i += 4) {
            String satelliteSNR = sentenceComponents[i];

            /**
             * Remove the checksum from the final SNR of the last satellite.
             */
            if (sentenceComponents[i].contains("*")) {
                satelliteSNR = sentenceComponents[i].substring(0, sentenceComponents[i].indexOf('*'));
            }

            /**
             * Check that the SNR value is not null/missing.
             */
            if (!satelliteSNR.equals("")) {
                satellitesWithSNR.add(Integer.parseInt(satelliteSNR));
            }
        }
    }

    /**
     * Get the sentence number.
     * @return The sentence number.
     */
    public int getSentenceNumber() {
        return sentenceNumber;
    }

    /**
     * Get the list of satellites with SNR values.
     * @return The satellite list.
     */
    public LinkedList<Integer> getSatellitesWithSNR(){
        return satellitesWithSNR;
    }
}
