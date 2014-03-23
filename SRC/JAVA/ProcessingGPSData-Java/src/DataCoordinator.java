import java.util.Calendar;

/**
 * DataCoordinator controls the majority of the
 * flow of data throughout the application.
 * <p/>
 * It is this class that starts the reading of the files,
 * and takes the data from them, calling each line to be
 * read and sent to SentenceHandler to be dealt
 * with in the specific way of each individual sentence.
 * <p/>
 * DataCoordinator also handles the flow of the data
 * from SentenceHandler into the XMLCreator and
 * to outputting it into a gpx file.
 * <p/>
 * Created by jee22 on 05/03/14.
 */
public class DataCoordinator {

    /**
     * Creates XML
     */
    private XMLCreator xmlCreator;

    /**
     * Deals with outputting Files
     */
    private FileOutputter fileMaker;

    /**
     * Gets sentences from the primary GPS file.
     */
    private StreamReader primary_file;

    /**
     * Gets sentences from the secondary GPS file.
     */
    private StreamReader secondary_file;

    /**
     * The last recorded time of an output of data
     */
    private Calendar lastRecordedTime = null;

    /**
     * Holds whether a second was missed from the
     * timestamps or not
     */
    private boolean missedSecond = false;

    /**
     * The class to take a sentence and get all
     * of the required data from within.
     */
    private SentenceHandler sentence_handler;

    /**
     * Holds reference for the most recent sentence
     * from the primary stream.
     */
    private String primary_sentence = null;
    /**
     * As above but for the secondary stream.
     */
    private String secondary_sentence = null;

    /**
     * The latitude offset to align the secondary stream
     * with the primary stream
     */
    private double latitude_offset = 0;
    /**
     * As above, but with longitude
     */
    private double longitude_offset = 0;

    /**
     * Constructur makes a new XMLCreator.
     */
    DataCoordinator() {
        xmlCreator = new XMLCreator();
        fileMaker = new FileOutputter();
    }

    /**
     * The start of the application.
     */
    protected void run() {
        System.out.println("Starting application...");

        /**
         * Start building the GPX file.
         */
        fileMaker.addToXML(xmlCreator.startXML());
        /**
         * Make a new stream reader to read the primary
         * GPS data file.
         */
        primary_file = new StreamReader("gps_data/gps_1.dat");
        /**
         * Make a new stream reader to read the secondary
         * GPS data file.
         */
        secondary_file = new StreamReader("gps_data/gps_2.dat");

        /**
         * Begin reading and syncing the Streams.
         */
        syncStreams();


        /**
         * At the end of the streams, close the readers down.
         */
        primary_file.closeStream();
        secondary_file.closeStream();
        /**
         * Finish off building the XML.
         */
        fileMaker.addToXML(xmlCreator.endXML());
        fileMaker.writeOutGPX();

        System.out.println("End of application");
    }

    /**
     * Read in from the Streams and keep them synced throughout the
     * application's lifetime.
     */
    private void syncStreams() {
        System.out.println("Syncing Streams..");
        Stream primary_stream = new Stream();
        Stream secondary_stream = new Stream();

        /**
         * Create new sentence handlers to deal with the individual
         * streams.
         */
        sentence_handler = new SentenceHandler();

        /**
         * Read in and deal with sentences until we have timestamps for each stream.
         */
        getInitialTimestamps(primary_stream, secondary_stream);

        /**
         * While there are still waypoints to be made in Stream 1.
         */
        while (primary_sentence != null) {

            /**
             * See if there are any seconds missing from the primary stream.
             */
            checkForMissedSeconds(primary_stream, secondary_stream);
            /**
             * If there are missed seconds, do something about it.
             */
            if (missedSecond) {
                /**
                 * Check that the current time isn't the same as the last recorded time
                 * in stream 2 so we don't output duplicate timestamps.
                 */
                if (secondary_stream.getDate_and_time().compareTo(lastRecordedTime) != 0) {
                    /**
                     * Output stream 2 in place of stream 1 to the GPX.
                     */
                    applyOffsets(secondary_stream);
                    getStreamOutput(secondary_stream);

                    /**
                     * Set missed seconds to being false to say that all seconds are accounted for.
                     */
                    missedSecond = false;
                    /**
                     * Update our last recorded time with that from the replacement time from
                     * stream 2.
                     */
                    lastRecordedTime = (Calendar) secondary_stream.getDate_and_time().clone();
                }
            }

            /**
             * If the primary stream's time is further behind than the second stream's time,
             * read it in until it 'catches up' with the secondary.
             */
            if (primary_stream.getDate_and_time().compareTo(secondary_stream.getDate_and_time()) < 0) {
                /**
                 * Work with the current sentence and get the desired information.
                 */
                primary_sentence = primary_file.getNextSentence();
                sentence_handler.parse(primary_stream, primary_sentence);

            } else if (primary_stream.getDate_and_time().compareTo(secondary_stream.getDate_and_time()) > 0) {
                /**
                 * Else if the secondary stream's time is further behind the primary stream,
                 * read that in until it 'catches up' with the primary.
                 */

                /**
                 * Work with the current sentence and get the desired information.
                 */
                secondary_sentence = secondary_file.getNextSentence();
                sentence_handler.parse(secondary_stream, secondary_sentence);
            } else {

                /**
                 * ELSE, the timestamps must match, so read both in to compare.
                 */


                //==================== PRIMARY STREAM ===================

                /**
                 * Check that the stream is not empty.
                 */
                if (primary_sentence != null) {
                    if (primary_stream.getDate_and_time().compareTo(lastRecordedTime) != 0) {
                        /**
                         * Check that there is no record of missing seconds from the stream.
                         */
                        if (!missedSecond) {
                            determineBestOutput(primary_stream, secondary_stream);
                        }
                    }
                    /**
                     * Work with the current sentence and get the desired information.
                     */
                    primary_sentence = primary_file.getNextSentence();
                    sentence_handler.parse(primary_stream, primary_sentence);
                }


                //================= SECONDARY STREAM ====================


                /**
                 * Work with the current sentence and get the desired information.
                 */
                secondary_sentence = secondary_file.getNextSentence();
                sentence_handler.parse(secondary_stream, secondary_sentence);


            }

            /**
             * Update the Latitude and Longitude offsets between the primary
             * and secondary streams.
             */
            if (primary_stream.getLatitude() != 0 && secondary_stream.getLatitude() != 0) {
                updateOffsets(primary_stream, secondary_stream);
            }

        }
    }

    /**
     * Get the core data from a stream and output it for XML.
     *
     * @param streamer - The stream to be XMLified.
     */
    private void getStreamOutput(Stream streamer) {
        fileMaker.addToXML(xmlCreator.formatLatitudeAndLongitude(streamer.getLatitude(), streamer.getLongitude()));
        fileMaker.addToXML(xmlCreator.formatElevation(streamer.getElevation()));
        fileMaker.addToXML(xmlCreator.formatDateAndTime(streamer.getDate_and_time()));
        fileMaker.addToXML(xmlCreator.endWaypoint());
    }

    /**
     * When first reading in the streams, read them in until
     * we get an initial timestamp for each stream.
     *
     * @param primary_stream   - S1.
     * @param secondary_stream - S2.
     */
    private void getInitialTimestamps(Stream primary_stream, Stream secondary_stream) {
        /**
         * While there is not yet a timestamp, keep reading in a line and parsing it
         * as a sentence.
         */
        while (primary_stream.getDate_and_time() == null) {
            primary_sentence = primary_file.getNextSentence();
            sentence_handler.parse(primary_stream, primary_sentence);
        }
        while (secondary_stream.getDate_and_time() == null) {
            secondary_sentence = secondary_file.getNextSentence();
            sentence_handler.parse(secondary_stream, secondary_sentence);
        }
    }

    private void determineBestOutput(Stream primary_stream, Stream secondary_stream) {

        /**
         * if there are 3 or more satellites with an SNR value of
         * 35 or more in Stream 1, use stream 1 for output.
         */
        if (primary_stream.getSignals35AndAbove() >= 3) {
            /**
             * Call to output the stream as XML for the GPX.
             */
            getStreamOutput(primary_stream);

            /**
             * Update for the last time that was recorded to GPX.
             */
            lastRecordedTime = (Calendar) primary_stream.getDate_and_time().clone();

            /**
             * If there are not 3 or more 35+ SNR satellites, are there
             * 3 or more with a range between 30 and 35?
             */
        } else if (primary_stream.getSignalsBetween30And35() >= 3) {
            /**
             * If there are, is Stream 2 still better? This is when Stream 2
             * has 3 or more 30-34 ranged satellites and equal to or more
             * 35+ satellites
             */
            if (secondary_stream.getSignalsBetween30And35() >= 3
                    && secondary_stream.getSignals35AndAbove() >= primary_stream.getSignals35AndAbove()) {
                applyOffsets(secondary_stream);
                getStreamOutput(secondary_stream);
                lastRecordedTime = (Calendar) primary_stream.getDate_and_time().clone();
            } else {
                /**
                 * If Stream two cannot beat Stream 1's best (regardless of if Stream
                 * 2 has less than 3 30-34 SNR satellites and some 35+s), just use
                 * Stream 1.
                 */
                getStreamOutput(primary_stream);
                lastRecordedTime = (Calendar) primary_stream.getDate_and_time().clone();
            }
            /**
             * If sStream one has less than 3 satellites with SNR 30-34 and
             * less than 3 satellites with SNR 35+, use Stream 2 with the offsets.
             */
        } else if ((primary_stream.getSignalsBetween30And35() < 3
                && primary_stream.getSignals35AndAbove() < 3)) {
            applyOffsets(secondary_stream);
            getStreamOutput(secondary_stream);
            lastRecordedTime = (Calendar) primary_stream.getDate_and_time().clone();
        }
    }

    /**
     * Look to see if there is continuous time so far or if a second has been missed out of the primary
     * stream.
     *
     * @param primary_stream   - Stream 1.
     * @param secondary_stream - Stream 2.
     */
    private void checkForMissedSeconds(Stream primary_stream, Stream secondary_stream) {
        /**
         * Make sure that we're not going to be checking against null and causing
         * an Exception.
         */
        if (lastRecordedTime == null) {
            lastRecordedTime = (Calendar) primary_stream.getDate_and_time().clone();
        }

        /**
         * If there is a time difference of more than 2000 milliseconds between timestamps, we can
         * be sure that a second has been missed somewhere.
         */
        if ((lastRecordedTime.getTimeInMillis() - primary_stream.getDate_and_time().getTimeInMillis()) < -1999) {
            /**
             * Check that stream 1 is actually being supported by stream 2. As this may be where the 'missed second' lies.
             */
            if (primary_stream.getDate_and_time().compareTo(secondary_stream.getDate_and_time()) > 0) {
                missedSecond = true;
            }
        }
    }

    /**
     * Update the Latitude and Longitude offsets. Doing this
     * allows us to estimate where the primary stream's lat and lng
     * would be, even if the signal isn't good enough for us to
     * use the actual given Latitude and Longitude.
     */
    private void updateOffsets(Stream primary_stream, Stream secondary_stream) {
        double lat_off = primary_stream.getLatitude();
        lat_off = lat_off - secondary_stream.getLatitude();
        latitude_offset = lat_off;

        double lng_off = primary_stream.getLongitude();
        lng_off = lng_off - secondary_stream.getLongitude();
        longitude_offset = lng_off;
    }

    /**
     * Take a stream and apply the offsets taken from the last two stable
     * versions of the primary and secondary stream to the Latitude and
     * Longitude of it.
     *
     * @param streamer - The Stream to be aligned.
     */
    private void applyOffsets(Stream streamer) {
        streamer.setLatitudeVal(streamer.getLatitude() + latitude_offset);
        streamer.setLongitudeVal(streamer.getLongitude() + longitude_offset);
    }
}
