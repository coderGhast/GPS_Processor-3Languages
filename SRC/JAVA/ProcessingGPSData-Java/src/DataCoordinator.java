import java.util.Calendar;

/**
 * Created by jee22 on 05/03/14.
 */
public class DataCoordinator {

    private XMLCreator xmlCreator;
    private Calendar lastRecordedTime;

    private StreamReader primary_stream;
    private StreamReader secondary_stream;
    private SentenceHandler primary_sentence_handler;
    private SentenceHandler secondary_sentence_handler;

    DataCoordinator() {
        primary_sentence_handler = new SentenceHandler();
        secondary_sentence_handler = new SentenceHandler();
        xmlCreator = new XMLCreator();
    }

    protected void start() {
        System.out.println(xmlCreator.startXML());
        primary_stream = new StreamReader("gps_data/gps_1.dat");
        secondary_stream = new StreamReader("gps_data/gps_2.dat");

        if (primary_stream != null && secondary_stream != null) {
            syncStreams();


        }

        primary_stream.closeStream();
        secondary_stream.closeStream();
        System.out.println(xmlCreator.endXML());
    }

    private void syncStreams() {
        String primary_sentence = null;
        String secondary_sentence = null;

        while (primary_sentence_handler.getDate_and_time() == null) {
            primary_sentence = primary_stream.getNextSentence();
            primary_sentence_handler.parse(primary_sentence);
        }
        while (secondary_sentence_handler.getDate_and_time() == null) {
            secondary_sentence = secondary_stream.getNextSentence();
            secondary_sentence_handler.parse(secondary_sentence);
        }

        while (primary_sentence != null) {
            lastRecordedTime = primary_sentence_handler.getDate_and_time();

            String s1_time = xmlCreator.formatDateAndTime(primary_sentence_handler.getDate_and_time());
            String s2_time = xmlCreator.formatDateAndTime(secondary_sentence_handler.getDate_and_time());

            if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) < 0) {
                //System.out.println("S1 Less than S2. \n" + s1_time + " \n" + s2_time);
                primary_sentence = primary_stream.getNextSentence();
                if (primary_sentence != null) {
                    primary_sentence_handler.parse(primary_sentence);
                    if(primary_sentence_handler.getDate_and_time().compareTo(lastRecordedTime) != 1 && primary_sentence_handler.getDate_and_time().compareTo(lastRecordedTime) != 0){
                        //System.out.println("HALP HALP: " + primary_sentence_handler.getDate_and_time().compareTo(lastRecordedTime));
                    }
                }

            } else if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) > 0) {
                //System.out.println("S1 Greater than S2. \n" + s1_time + " \n" + s2_time);
                secondary_sentence = secondary_stream.getNextSentence();
                if (secondary_sentence != null) {
                    secondary_sentence_handler.parse(secondary_sentence);
                }

            } else {

                if (primary_sentence != null) {
                    //System.out.println("S1. " + s1_time);
                    primary_sentence_handler.parse(primary_sentence);

                    System.out.println(xmlCreator.formatLatitudeAndLongitude(primary_sentence_handler.getLatitude(), primary_sentence_handler.getLongitude()));
                    System.out.println(xmlCreator.formatElevation(primary_sentence_handler.getElevation()));
                    System.out.println(xmlCreator.formatDateAndTime(primary_sentence_handler.getDate_and_time()));
                    System.out.println(xmlCreator.endWaypoint());

                }
                primary_sentence = primary_stream.getNextSentence();


                if (secondary_sentence != null) {
                    //System.out.println("S2. " + s2_time);
                    secondary_sentence_handler.parse(secondary_sentence);

                    //System.out.println("S2 Latitude: " + secondary_sentence_handler.getLatitude());

                }
                secondary_sentence = secondary_stream.getNextSentence();

            }

            //System.out.println("S1. Num satellites with SNR: " + primary_sentence_handler.getNumSatellitesWithSNR() + " " + primary_sentence_handler.getDate_and_time());
            //System.out.println("S2. Num satellites with SNR: " + secondary_sentence_handler.getNumSatellitesWithSNR());
        }

    }


}
