import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Takes a file name and opens
 * it to be processed as a GPS
 * .dat file.
 *
 * Created by jee22 on 05/03/14.
 */
public class StreamReader {
    private FileReader reader;
    private BufferedReader textReader;

    /**
     * Start the class by opening the File.
     * @param fileName The file to be read in.
     */
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

    /**
     * Read the next sentence (line) of the file.
     * @return The full sentence.
     */
    protected String getNextSentence(){
        String currentSentence = null;
        try {
            currentSentence = textReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentSentence;
    }

    /**
     * End reading the stream/file.
     */
    protected void closeStream(){
        try {
            reader.close();
            textReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
