import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Creates and outputs a GPX file
 * from a constantly built String.
 *
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public class FileOutputter {

    private StringBuilder sb;

    /**
     * Make a new StringBuilder.
     */
    public FileOutputter(){
        sb = new StringBuilder();
    }

    /**
     * Add part of the String to the StringBuilder.
     * @param add The String to be added to the XML list.
     */
    public void addToXML(String add){
        sb.append(add);
    }

    /**
     * Write out the String to a .gpx file.
     */
    public void writeOutGPX(){
        try {

            File file = new File("java_output.gpx");

            /**
             * If file doesn't exist, create it.
             */
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            /**
             * Write out the file with the given String, built
             * during the applications lifetime.
             */
            bufferedWriter.write(sb.toString());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
