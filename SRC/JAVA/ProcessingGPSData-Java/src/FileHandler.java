import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import sentences.*;

/**
 * Created by jee22 on 05/03/14.
 */
public class FileHandler {

    private int numLines = 1;

    public FileHandler(){
    }

    protected void readGPS_data(String fileName){
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader textReader = new BufferedReader(reader);

            String currentSentence;

            /**
             * While there are still lines (or for the sake of this
             * application 'While there is still data streaming from
             * the GPS devices'), get the next line and store it in
             * the String 'currentSentence'.
             */
            while((currentSentence = textReader.readLine()) != null){

                deconstructSentence(currentSentence);

                currentSentence = null;
            }

            textReader.close();
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deconstructSentence(String sentence){
        String[] sentenceComponents = sentence.split(",");
        int sentenceType = determineType(sentenceComponents[0]);

        switch(sentenceType){
            case(1):
                GPGSA gpgsa = new GPGSA(sentenceComponents);
                break;
            case(2):
                GPRMC gprmc = new GPRMC(sentenceComponents);
                printData(gprmc);
                break;
            case(3):
                GPGGA gpgga = new GPGGA(sentenceComponents);
                break;
            case(4):
                GPGSV gpgsv = new GPGSV(sentenceComponents);
                break;
            case(5):
                GPZDA gpzda = new GPZDA(sentenceComponents);
                break;
            case(6):
                GPGBS gpgbs = new GPGBS(sentenceComponents);
                break;
            default:;
        }
    }

    private int determineType(String sentenceSignifier){
        int sentenceType = 0;
        if(sentenceSignifier.equals("$GPGSA")){
            sentenceType = 1;
        } else if (sentenceSignifier.equals("$GPRMC")){
            sentenceType = 2;
        } else if (sentenceSignifier.equals("$GPGGA")){
            sentenceType = 3;
        } else if (sentenceSignifier.equals("$GPGSV")){
            sentenceType = 4;
        } else if (sentenceSignifier.equals("$GPZDA")){
            sentenceType = 5;
        } else if (sentenceSignifier.equals("$GPGBS")){
            sentenceType = 6;
        } else {
            sentenceType = 0;
            System.out.println("Sentence type: " +  sentenceSignifier + " is not handled by this application.");
        }
        return sentenceType;
    }

    private void printData(GPRMC gprmc){
        GPSTime tempTime = gprmc.getTime();
        System.out.println("============");
        System.out.println(tempTime.getDay() + "-" + tempTime.getMonth() + "-" + tempTime.getYear());
        System.out.println(tempTime.getHours() + ":" + tempTime.getMinutes() + ":" + tempTime.getSeconds());
        System.out.println(gprmc.getLatitude() + ", " + gprmc.getLongitude());
    }

}
