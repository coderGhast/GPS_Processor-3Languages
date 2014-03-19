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
     * The last recorded time of an output of data
     */
    private Calendar lastRecordedTime;

    /**
     * Holds whether a second was missed from the
     * timestamps or not
     */
    private boolean missedSecond;

    /**
     * The primary stream that this application
     * will focus on making a route with.
     */
    private StreamReader primary_stream;

    /**
     * The supporting stream for the primary stream
     * that the application will fall back on
     * if/when the primary stream fails.
     */
    private StreamReader secondary_stream;

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
    private double latitude_offset;
    /**
     * As above, but with longitude
     */
    private double longitude_offset;

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
        /**
         * Start building the GPX file.
         */
        fileMaker.addToXML(xmlCreator.startXML());
        /**
         * Make a new stream reader to read the primary
         * GPS data file.
         */
        primary_stream = new StreamReader("gps_data/gps_1.dat");
        /**
         * Make a new stream reader to read the secondary
         * GPS data file.
         */
        secondary_stream = new StreamReader("gps_data/gps_2.dat");

        /**
         * Begin reading and syncing the Streams.
         */
        syncStreams();


        /**
         * At the end of the streams, close the readers down.
         */
        primary_stream.closeStream();
        secondary_stream.closeStream();
        /**
         * Finish off building the XML.
         */
        fileMaker.addToXML(xmlCreator.endXML());
        fileMaker.writeOutGPX();
    }

    /**
     * Read in from the Streams and keep them synced throughout the
     * application's lifetime.
     */
    private void syncStreams() {
        /**
         * Create new sentence handlers to deal with the individual
         * streams.
         */
        SentenceHandler primary_sentence_handler = new SentenceHandler();
        SentenceHandler secondary_sentence_handler = new SentenceHandler();

        /**
         * Read in and deal with sentences until we have timestamps for each stream.
         */
        getInitialTimestamps(primary_sentence_handler, secondary_sentence_handler);

        /**
         * While there are still waypoints to be made in Stream 1.
         */
        while (primary_sentence != null) {

            /**
             * See if there are any seconds missing from the primary stream.
             */
            checkForMissedSeconds(primary_sentence_handler, secondary_sentence_handler);
            /**
             * If there are missed seconds, do something about it.
             */
            if (missedSecond) {
                /**
                 * Check that the current time isn't the same as the last recorded time
                 * in stream 2 so we don't output duplicate timestamps.
                 */
                if (secondary_sentence_handler.getDate_and_time().compareTo(lastRecordedTime) != 0) {
                    /**
                     * Output stream 2 in place of stream 1 to the GPX.
                     */
                    getStreamOutput(applyOffsets(secondary_sentence_handler));
                    /**
                     * Set missed seconds to being false to say that all seconds are accounted for.
                     */
                    missedSecond = false;
                    /**
                     * Update our last recorded time with that from the replacement time from
                     * stream 2.
                     */
                    lastRecordedTime = (Calendar) secondary_sentence_handler.getDate_and_time().clone();
                }
            }

            /**
             * If the primary stream's time is further behind than the second stream's time,
             * read it in until it 'catches up' with the secondary.
             */
            if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) < 0) {
                primary_sentence = primary_stream.getNextSentence();
                if (primary_sentence != null) {
                    /**
                     * Work with the current sentence and get the desired information.
                     */
                    primary_sentence_handler.parse(primary_sentence);
                }

            } else if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) > 0) {
                /**
                 * Else if the secondary stream's time is further behind the primary stream,
                 * read that in until it 'catches up' with the primary.
                 */
                secondary_sentence = secondary_stream.getNextSentence();
                if (secondary_sentence != null) {
                    /**
                     * Work with the current sentence and get the desired information.
                     */
                    secondary_sentence_handler.parse(secondary_sentence);
                }
            } else {

                /**
                 * ELSE, the timestamps must match, so read both in to compare.
                 */


                //==================== PRIMARY STREAM ===================

                /**
                 * Check that the stream is not empty.
                 */
                if (primary_sentence != null) {
                    if (primary_sentence_handler.getDate_and_time().compareTo(lastRecordedTime) != 0) {
                        /**
                         * Check that there is no record of missing seconds from the stream.
                         */
                        if (!missedSecond) {

                            /**
                             * if there are 3 or more satellites with an SNR value of
                             * 35 or more in Stream 1, use stream 1 for output.
                             */
                            if (primary_sentence_handler.getSignalsAbove35() >= 3) {
                                /**
                                 * Call to output the stream as XML for the GPX.
                                 */
                                getStreamOutput(primary_sentence_handler);

                                /**
                                 * Update for the last time that was recorded to GPX.
                                 */
                                lastRecordedTime = (Calendar) primary_sentence_handler.getDate_and_time().clone();

                                /**
                                 * If there are not 3 or more 35+ SNR satellites, are there
                                 * 3 or more with a range between 30 and 35?
                                 */
                            } else if (primary_sentence_handler.getSignalsBetween30And35() >= 3) {
                                /**
                                 * If there are, is Stream 2 still better? This is when Stream 2
                                 * has 3 or more 30-34 ranged satellites and equal to or more
                                 * 35+ satellites
                                 */
                                if (secondary_sentence_handler.getSignalsBetween30And35() >= 3
                                        && secondary_sentence_handler.getSignalsAbove35() >= primary_sentence_handler.getSignalsAbove35()) {
                                    getStreamOutput(applyOffsets(secondary_sentence_handler));
                                    lastRecordedTime = (Calendar) primary_sentence_handler.getDate_and_time().clone();
                                } else {
                                    /**
                                     * If Stream two cannot beat Stream 1's best (regardless of if Stream
                                     * 2 has less than 3 30-34 SNR satellites and some 35+s), just use
                                     * Stream 1.
                                     */
                                    getStreamOutput(primary_sentence_handler);
                                    lastRecordedTime = (Calendar) primary_sentence_handler.getDate_and_time().clone();
                                }
                                /**
                                 * If sStream one has less than 3 satellites with SNR 30-34 and
                                 * less than 3 satellites with SNR 35+, use Stream 2 with the offsets.
                                 */
                            } else if ((primary_sentence_handler.getSignalsBetween30And35() < 3
                                    && primary_sentence_handler.getSignalsAbove35() < 3)) {
                                getStreamOutput(applyOffsets(secondary_sentence_handler));
                                lastRecordedTime = (Calendar) primary_sentence_handler.getDate_and_time().clone();
                            }
                        }
                    }
                    /**
                     * Work with the current sentence and get the desired information.
                     */
                    primary_sentence_handler.parse(primary_sentence);
                }

                /**
                 * Read in the next sentence, ready for the next run through.
                 * Do not parse it yet or we will lose the required information
                 * needed right now.
                 */
                primary_sentence = primary_stream.getNextSentence();


                //================= SECONDARY STREAM ====================

                /**
                 * Check that the stream is not empty.
                 */
                if (secondary_sentence != null) {
                    /**
                     * Work with the current sentence and get the desired information.
                     */
                    secondary_sentence_handler.parse(secondary_sentence);
                }
                /**
                 * Read in the next sentence, ready for the next run through.
                 * Do not parse it yet or we will lose the required information
                 * needed right now.
                 */
                secondary_sentence = secondary_stream.getNextSentence();


                /**
                 * Update the Latitude and Longitude offsets between the primary
                 * and secondary streams.
                 */
                updateOffsets(primary_sentence_handler, secondary_sentence_handler);
            }

        }
    }

    /**
     * Get the core data from a stream and output it for XML.
     *
     * @param sentence_handler - The stream to be XMLified.
     */
    private void getStreamOutput(SentenceHandler sentence_handler) {
        fileMaker.addToXML(xmlCreator.formatLatitudeAndLongitude(sentence_handler.getLatitude(), sentence_handler.getLongitude()));
        fileMaker.addToXML(xmlCreator.formatElevation(sentence_handler.getElevation()));
        fileMaker.addToXML(xmlCreator.formatDateAndTime(sentence_handler.getDate_and_time()));
        fileMaker.addToXML(xmlCreator.endWaypoint());
    }

    /**
     * When first reading in the streams, read them in until
     * we get an initial timestamp for each stream.
     *
     * @param primary_sentence_handler   - S1.
     * @param secondary_sentence_handler - S2.
     */
    private void getInitialTimestamps(SentenceHandler primary_sentence_handler, SentenceHandler secondary_sentence_handler) {
        /**
         * While there is not yet a timestamp, keep reading in a line and parsing it
         * as a sentence.
         */
        while (primary_sentence_handler.getDate_and_time() == null) {
            primary_sentence = primary_stream.getNextSentence();
            primary_sentence_handler.parse(primary_sentence);
        }
        while (secondary_sentence_handler.getDate_and_time() == null) {
            secondary_sentence = secondary_stream.getNextSentence();
            secondary_sentence_handler.parse(secondary_sentence);
        }
    }

    /**
     * Look to see if there is continous time so far or if a second has been missed out of the primary
     * stream.
     *
     * @param primary_sentence_handler   - Stream 1.
     * @param secondary_sentence_handler - Stream 2.
     */
    private void checkForMissedSeconds(SentenceHandler primary_sentence_handler, SentenceHandler secondary_sentence_handler) {
        /**
         * Make sure that we're not going to be checking against null and causing
         * an Exception.
         */
        if (lastRecordedTime == null) {
            lastRecordedTime = (Calendar) primary_sentence_handler.getDate_and_time().clone();
        }

        /**
         * If there is a time difference of more than 2000 milliseconds between timestamps, we can
         * be sure that a second has been missed somewhere.
         */
        if ((lastRecordedTime.getTimeInMillis() - primary_sentence_handler.getDate_and_time().getTimeInMillis()) < -1999) {
            /**
             * Check that stream 1 is actually being supported by stream 2.
             */
            if (primary_sentence_handler.getDate_and_time().compareTo(secondary_sentence_handler.getDate_and_time()) > 0) {
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
    private void updateOffsets(SentenceHandler primary_sentence_handler, SentenceHandler secondary_sentence_handler) {

            if (primary_sentence_handler.getLatitude() != null
                    && primary_sentence_handler.getLongitude() != null
                    && secondary_sentence_handler.getLatitude() != null
                    && secondary_sentence_handler.getLongitude() != null) {
                double doubleLat = Double.parseDouble(primary_sentence_handler.getLatitude());
                doubleLat = doubleLat - Double.parseDouble(secondary_sentence_handler.getLatitude());
                latitude_offset = doubleLat;

                double doubleLng = Double.parseDouble(primary_sentence_handler.getLongitude());
                doubleLng = doubleLng - Double.parseDouble(secondary_sentence_handler.getLongitude());
                longitude_offset = doubleLng;

            }

    }

    /**
     * Take a stream and apply the offsets taken from the last two stable
     * versions of the primary and secondary stream to the Latitude and
     * Longitude of it.
     *
     * @param secondary_sentence_handler - The Stream to be aligned.
     * @return - The aligned SentenceHandler stream.
     */
    private SentenceHandler applyOffsets(SentenceHandler secondary_sentence_handler) {
        secondary_sentence_handler.setLatitude(latitudeOffsetCorrection(secondary_sentence_handler));
        secondary_sentence_handler.setLongitude(longitudeOffsetCorrection(secondary_sentence_handler));
        return secondary_sentence_handler;
    }

    /**
     * Correct a stream's Latitude with the offset of the other.
     *
     * @param secondary_sentence_handler - Stream to be aligned.
     * @return The updated Latitude.
     */
    private String latitudeOffsetCorrection(SentenceHandler secondary_sentence_handler) {
        double doubleLat = Double.parseDouble(secondary_sentence_handler.getLatitude());
        doubleLat = doubleLat + latitude_offset;
        return String.valueOf(doubleLat);
    }

    /**
     * Correct a stream's Longitude with the offset of the other.
     *
     * @param secondary_sentence_handler - Stream to be aligned.
     * @return The updated Longitude.
     */
    private String longitudeOffsetCorrection(SentenceHandler secondary_sentence_handler) {
        double doubleLng = Double.parseDouble(secondary_sentence_handler.getLongitude());
        doubleLng = doubleLng + longitude_offset;
        return String.valueOf(doubleLng);
    }
}
