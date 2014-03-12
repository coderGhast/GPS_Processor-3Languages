/**
 * Created by jee22 on 05/03/14.
 */
public class DataCoordinator {

    private FileHandler files;
    private SentenceHandler sentenceHandler;

    DataCoordinator(){
        sentenceHandler = new SentenceHandler();
    }

    protected void start(){
        files = new FileHandler("gps_data/gps_1.dat");

        String currentSentence = null;
        do {
            currentSentence = files.getNextSentence();
            if(currentSentence != null){
                sentenceHandler.parse(currentSentence);
            }
        }
        while(currentSentence != null);
    }
}
