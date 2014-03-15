/**
 * Created by jee22 on 05/03/14.
 */
public class DataCoordinator {

    private StreamReader primary_stream;
    private StreamReader secondary_stream;
    private SentenceHandler primary_sentence_handler;
    private SentenceHandler secondary_sentence_handler;

    DataCoordinator() {
        primary_sentence_handler = new SentenceHandler();
        secondary_sentence_handler = new SentenceHandler();
    }

    protected void start() {
        primary_stream = new StreamReader("gps_data/gps_1.dat");
        secondary_stream = new StreamReader("gps_data/gps_2.dat");

        if (primary_stream != null && secondary_stream != null) {
            syncStreams();
        }


        primary_stream.closeStream();
        secondary_stream.closeStream();
    }

    /**
     * TODO:
     * 1. Make Date/Time comparable (just get on with it..)
     * 2. Write conditional for comparing the date/time, and how
     * to handle the case of stream one ahead or behind the stream
     * 3. Assign all data from the Stream (1) to variables.
     * 4. Start checking the validity and integrity of those variables each second
     * 5. If they are no good, check Stream 2.
     */
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

        /**
         * IT WORKS HORAY
         *
         * In this format at least, it takes in the stuff and makes them match.
         * The ordering of this is rather important. Any other ordering, including
         * how the Printlns run are important.
         * Eventually, I will be changing up the printlns for outputting to XML,
         * so it is vital that this ordering is kept in order for the XML
         * output to also be correct. :)
         */
        while (primary_sentence != null) {

            if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) < 0) {
                System.out.println("S1 Less than S2. \n" + primary_sentence_handler.getDate_and_time() + " \n" + secondary_sentence_handler.getDate_and_time());
                primary_sentence = primary_stream.getNextSentence();
                if (primary_sentence != null) {
                    primary_sentence_handler.parse(primary_sentence);
                }
            } else if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) > 0) {
                System.out.println("S1 Greater than S2. \n" + primary_sentence_handler.getDate_and_time() + " \n" + secondary_sentence_handler.getDate_and_time());
                secondary_sentence = secondary_stream.getNextSentence();
                if (secondary_sentence != null) {
                    secondary_sentence_handler.parse(secondary_sentence);
                }
            } else if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) == 0) {

                if (primary_sentence != null) {
                    System.out.println("S1. " + primary_sentence_handler.getDate_and_time());
                    primary_sentence_handler.parse(primary_sentence);

                }
                primary_sentence = primary_stream.getNextSentence();


                if (secondary_sentence != null) {
                    System.out.println("S2. " + secondary_sentence_handler.getDate_and_time());
                    secondary_sentence_handler.parse(secondary_sentence);

                }
                secondary_sentence = secondary_stream.getNextSentence();
            }
        }

    }
}
