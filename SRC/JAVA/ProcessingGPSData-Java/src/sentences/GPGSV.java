package sentences;

import java.util.LinkedList;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPGSV extends GenericSentence {
    private int numOfGSVs;
    private int sentenceNumber;
    private int numOfSatellitesInAllGSVs;
    private LinkedList<Integer> satellitesWithSNR;

    public GPGSV(String[] sentenceComponents) {
        satellitesWithSNR = new LinkedList<Integer>();
        addSatellitesFromGSV(sentenceComponents);

        setSentenceNumber(Integer.valueOf(sentenceComponents[2]));
    }

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

    public int getSentenceNumber() {
        return sentenceNumber;
    }

    public void setSentenceNumber(int sentenceNumber) {
        this.sentenceNumber = sentenceNumber;
    }

    public LinkedList<Integer> getSatellitesWithSNR(){
        return satellitesWithSNR;
    }
}
