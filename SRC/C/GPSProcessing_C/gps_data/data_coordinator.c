/* 
 * File:   data_coordinator.c
 * Author: jee22
 * 
 * The main controller of the application.
 * From here, the application makes calls
 * to get the sentence information from the
 * .dat gps files.
 * 
 * This data is then parsed to be used for
 * outputting and comparisons against the
 * other stream to decide which is the best
 * to output.
 *
 */

#include <stdio.h>
#include "file_handler.h"
#include "sentence_handler.h"
#include "date_manipulation.h"
#include "offset_handler.h"
#include "xml_creator.h"

static char *primary_sentence = "Begin";
static char *secondary_sentence = "Begin";
static time_t last_recorded_time;
static int missed_second = 0;
static double lat_offset;
static double lng_offset;

/**
 * Determining the best output stream by
 * comparing the Signal-to-Noise Ratios of the
 * satellites provided by the sentences.
 * @param stream_1 Primary stream
 * @param stream_2 Secondary stream
 */
void determine_best_output_stream(stream * stream_1, stream * stream_2) {
    /* If Primary stream has more than 3 satellites with an SNR of 35
     or more, then no issues, go print. */
    if (stream_1->snrs_above_35 >= 3) {
        /* Output stream 1 to XML*/
        output_stream(stream_1);
        last_recorded_time = stream_1->date_and_time;
        /* If there are 3 or more satellites with an SNR
         between 30 and 35, do more checks. */
    } else if (stream_1->snrs_above_30_below_35 >= 3) {
        /* If stream 2 has a better amount of satellites
         than stream 1, use that instead. */
        if (stream_2->snrs_above_30_below_35 >= 3
                && stream_2->snrs_above_35 >= stream_1->snrs_above_35) {
            /* Output stream 2 to XML WITH OFFSETS */
            apply_offsets(stream_2, lat_offset, lng_offset);
            output_stream(stream_2);
            last_recorded_time = stream_1->date_and_time;
        } else {
            /* Output stream 1 to XML */
            output_stream(stream_1);
            last_recorded_time = stream_1->date_and_time;
        }
        /* If the quality of the primary streams
         satellites are very bad, just use the
         secondary stream. */
    } else if (stream_1->snrs_above_30_below_35 < 3
            && stream_1->snrs_above_35 < 3) {
        /* Output stream 2 to XML WITH OFFSETS */
        apply_offsets(stream_2, lat_offset, lng_offset);
        output_stream(stream_2);
        last_recorded_time = stream_1->date_and_time;
    }
}

/**
 * Read in each stream until they each have a timestamp to
 * be able to be compared.
 * @param primary_stream Stream 1
 * @param stream_1 Primary Stream
 * @param secondary_stream Stream 2
 * @param stream_2 Secondary Stream
 */
void get_initial_timestamps(FILE*primary_stream, stream * stream_1,
        FILE*secondary_stream, stream * stream_2) {
    /* Flat time to compare against other times that should
     be more than the flat default time. */
    time_t zero;

    /* While primary stream doesn't have a time more than the
     default, keep reading and parsing.*/
    while (difftime(zero, stream_1->date_and_time) == 0) {
        parse_sentence(stream_1, primary_sentence);
        primary_sentence = read_in_sentence(primary_stream);
    }
    /* Same as above, but with the secondary stream. */
    while (difftime(zero, stream_2->date_and_time) == 0) {
        parse_sentence(stream_2, secondary_sentence);
        secondary_sentence = read_in_sentence(secondary_stream);
    }
}

/**
 * Check the streams for seconds missed between the last
 * recorded time and the stream 1.
 * @param stream_1 Primary stream
 * @param stream_2 Secondary stream
 */
void check_for_missed_seconds(stream * stream_1, stream * stream_2) {
    /* Flat time to compare against */
    time_t zero;
    /* Check that the last recorded time has been given
     a time to begin with. If not, give it the most recent. */
    if (difftime(zero, last_recorded_time) == 0) {
        last_recorded_time = stream_1->date_and_time;
    }
    /* If the last read in time from stream 1 is further than
     1 second ahread of the last recorded time, we've missed
     a second*/
    if (difftime(last_recorded_time, stream_1->date_and_time) < -1) {
        /* Check that the primary stream is being supported by
         the secondary stream. */
        if (difftime(stream_1->date_and_time, stream_2->date_and_time) > 0) {
            /* Set missed time as 'true'. */
            missed_second = 1;
        }
    }

}

/**
 * The main body of the application.
 * This function calls to get the data from the files
 * and then passes it on to sentence_handler where it
 * is parsed and stored in the stream structs.
 * 
 * The data stored in those structs is then used to
 * determine how the data should be output to the GPX.
 * The calls for outputting to the GPX are also made
 * here in this function.
 * 
 * @param primary_stream - Primary stream file.
 * @param secondary_stream - Secondary stream file.
 */
void sync_streams(FILE * primary_stream, FILE *secondary_stream) {
    /*
     * Make two new stream structs to hold the data about the
     * primary and secondary streams (stream_1 and stream_2,
     * respectively).
     */
    stream * stream_1 = malloc(sizeof (stream));
    stream * stream_2 = malloc(sizeof (stream));

    /*
     * Read in lines of the gps .dat files until we have timestamps.
     */
    get_initial_timestamps(primary_stream, stream_1, secondary_stream, stream_2);

    /*
     * While our primary stream has some lines left to go in it,
     * do some work!
     */
    while (strcmp(primary_sentence, "end") != 0) {
        /*
         * Check that there hasn't been a second missed in the timestamps.
         * If there has been, use the secondary stream as a replacement
         * in the GPX output.
         */
        check_for_missed_seconds(stream_1, stream_2);
        if (missed_second > 0) {
            if (difftime(stream_2->date_and_time, last_recorded_time) != 0) {
                /* Output stream 2 to XML WITH OFFSETS */
                apply_offsets(stream_2, lat_offset, lng_offset);
                output_stream(stream_2);
                /* After putting out the second stream to make up
                 for the missing second, set missing second to
                 show that no second has been missed. */
                missed_second = 0;
                last_recorded_time = stream_2->date_and_time;
            }
        }

        /*
         * 'Catch up' primary stream to secondary stream if it's timestamp
         * is further behind.
         */
        if (difftime(stream_1->date_and_time, stream_2->date_and_time) < 0) {
            /* Reads in the next sentence from the primary stream
             and parse it through the application. */
            primary_sentence = read_in_sentence(primary_stream);
            parse_sentence(stream_1, primary_sentence);
        }/*
         * 'Catch up' secondary stream to primary stream if it's timestamp
         * is further behind.
         */
        else if (difftime(stream_1->date_and_time,
                stream_2->date_and_time) > 0) {
            /* Reads in the next sentence from the secondary stream
             and parse it through the application. */
            secondary_sentence = read_in_sentence(secondary_stream);
            parse_sentence(stream_2, secondary_sentence);
        } else {
            /*
             * Timestamps must match, so run each stream
             * equally.
             */

            /*=========== PRIMARY STREAM =============== */
            /*
             * Check that we won't be outputting duplicate timestamp data.
             */
            if (difftime(last_recorded_time, stream_1->date_and_time) != 0) {
                /*
                 * Check we haven't missed a second in the timestamps.
                 */
                if (!missed_second) {
                    /* Call to see which stream has the best satellite
                     Signal-to-Noise Ratios.*/
                    determine_best_output_stream(stream_1, stream_2);
                    /* Read in the next primary sentence and parse it
                     through the application. */
                    primary_sentence = read_in_sentence(primary_stream);
                    parse_sentence(stream_1, primary_sentence);
                }
            }

            /*=========== SECONDARY STREAM ==============*/
            /* Reads in and parses the next sentence of the 
             secondary stream. */
            secondary_sentence = read_in_sentence(secondary_stream);
            parse_sentence(stream_2, secondary_sentence);
        }

        /* If the both streams have a latitude and longitude value
         saved, calculate the offsets between them. */
        if (stream_1->latitude != 0 && stream_2->latitude != 0) {
            lat_offset = update_latitude_offset(stream_1, stream_2);
            lng_offset = update_longitude_offset(stream_1, stream_2);
        }
    }

    /*
     * Free the data from the Heap with 'free' for both streams.
     */
    free(stream_1);
    free(stream_2);
}

/**
 * The start of the application, called from
 * the main function.
 */
void run_application() {
    /* Opens the file for output using 'w' for 'write'*/
    initial_open();
    /* Writes the start of the GPX XML file */
    start_xml();

    /* Opens two Files, one for each stream.*/
    FILE * primary_stream = open_the_file("gps_data/gps_1.dat");
    FILE * secondary_stream = open_the_file("gps_data/gps_2.dat");

    /* Calls to sync and run through each stream.*/
    sync_streams(primary_stream, secondary_stream);

    /* Closes the two stream files*/
    close_file(primary_stream);
    close_file(secondary_stream);

    /* Writes the end of the GPX XML file.*/
    end_xml();
}