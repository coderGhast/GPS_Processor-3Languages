/**
 * Created by jee22 on 05/03/14.
 */
public class DataCoordinator {

    private StreamReader primary_stream;
    private StreamReader secondary_stream;
    private SentenceHandler primary_sentence_handler;
    private SentenceHandler secondary_sentence_handler;

    DataCoordinator(){
        primary_sentence_handler = new SentenceHandler();
        secondary_sentence_handler = new SentenceHandler();
    }

    protected void start(){
        primary_stream = new StreamReader("gps_data/gps_1.dat");
        secondary_stream = new StreamReader("gps_data/gps_2.dat");

        if(primary_stream != null && secondary_stream != null){
            syncStreams();
        }



        primary_stream.closeStream();
        secondary_stream.closeStream();
    }

    private void syncStreams(){
        String primary_sentence = null;
        String secondary_sentence = null;

        while(primary_sentence_handler.getDate_and_time() == null){
            primary_sentence = primary_stream.getNextSentence();
            primary_sentence_handler.parse(primary_sentence);
        }
        while(secondary_sentence_handler.getDate_and_time() == null){
            secondary_sentence = secondary_stream.getNextSentence();
            secondary_sentence_handler.parse(secondary_sentence);
        }

        while(primary_sentence != null){
            if(!primary_sentence_handler.getDate_and_time().equals(secondary_sentence_handler.getDate_and_time())){
                while(!primary_sentence_handler.getDate_and_time().equals(secondary_sentence_handler.getDate_and_time())){
                    primary_sentence = primary_stream.getNextSentence();
                    if(primary_sentence != null){
                        /**
                         *
                         * THIS WORKS, BUT ONLY SO FAR AS STREAM 1 BEING 'BEHIND' STREAM 2.
                         *
                         * MUST MAKE THE TIME/DATE COMPARABLE TO BE LESS THAN/GREATER THAN,
                         * SO THAT WE MAY FIND OUT WHICH STREAM NEEDS TO READ IN MORE DATA.
                         *
                         * ONCE THIS METHOD IS COMPLETE, WE CAN BE SURE THAT EACH SENTENCE
                         * FROM EACH STREAM IS BEING READ IN AND KEPT UP TO DATE WITH THE
                         * OTHER STREAM, SO WE CAN START TREATING STREAM 1 AS OUR MAIN STREAM
                         * AND START LOOKING FOR 'WEAK' SIGNALS TO PROMPT US ASKING FOR
                         * DATA FROM STREAM 2.
                         *
                         * TODO:
                         * 1. Make Date/Time comparable (just get on with it..)
                         * 2. Write conditional for comparing the date/time, and how
                         * to handle the case of stream one ahead or behind the stream
                         * 3. Assign all data from the Stream (1) to variables.
                         * 4. Start checking the validity and integrity of those variables each second
                         * 5. If they are no good, check Stream 2.
                         *
                         */
                        primary_sentence_handler.parse(primary_sentence);
                        System.out.println("S1. " + primary_sentence_handler.getDate_and_time());
                    }
                }
            }
            primary_sentence = primary_stream.getNextSentence();
            if(primary_sentence != null){
                primary_sentence_handler.parse(primary_sentence);
                System.out.println("S1. " + primary_sentence_handler.getDate_and_time());
            }

            secondary_sentence = secondary_stream.getNextSentence();
            if(secondary_sentence != null){
                secondary_sentence_handler.parse(secondary_sentence);
                System.out.println("S2. " + secondary_sentence_handler.getDate_and_time());
            }
        }

    }
}
