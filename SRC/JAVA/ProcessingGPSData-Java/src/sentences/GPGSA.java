package sentences;

/**
 * Created by jee22 on 05/03/14.
 */
public class GPGSA extends GenericSentence {

    private String fixSelection;
    private int typeOfFix;
    private int numSatellites;

    public GPGSA(String[] sentenceComponents){
        setFixSelection(sentenceComponents[1]);
        setTypeOfFix(Integer.parseInt(sentenceComponents[2]));
        setNumSatellites(countSatellites(sentenceComponents));
    }

    private int countSatellites(String[] sentenceComponents){
        int n = 0;
        for(int i = 3; i < 15; i++){
            if(!sentenceComponents[i].equals("")){
                n++;
            }
        }
        return n;
    }

    public int getTypeOfFix() {
        return typeOfFix;
    }

    private void setTypeOfFix(int typeOfFix) {
        this.typeOfFix = typeOfFix;
    }

    public String getFixSelection() {
        return fixSelection;
    }

    private void setFixSelection(String fixSelection) {
        this.fixSelection = fixSelection;
    }

    public int getNumSatellites() {
        return numSatellites;
    }

    private void setNumSatellites(int numSatellites) {
        this.numSatellites = numSatellites;
    }
}
