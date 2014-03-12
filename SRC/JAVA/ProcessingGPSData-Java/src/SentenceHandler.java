import sentences.*;

/**
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public class SentenceHandler {

    protected void parse(String currentSentence){
        deconstructSentence(currentSentence);
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
