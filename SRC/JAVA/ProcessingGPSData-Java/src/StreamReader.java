import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jee22 on 05/03/14.
 */
public class StreamReader {

    private int numLines = 1;
    private FileReader reader;
    private BufferedReader textReader;

    public StreamReader(String fileName){
        try {
            reader = new FileReader(fileName);
            textReader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getNextSentence(){
        String currentSentence = null;
        try {
            currentSentence = textReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentSentence;
    }

    protected void closeStream(){
        try {
            reader.close();
            textReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
