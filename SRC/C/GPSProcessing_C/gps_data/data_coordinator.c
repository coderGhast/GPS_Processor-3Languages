/* 
 * File:   data_coordinator.c
 * Author: jee22
 *
 * Created on 19 March 2014
 */

#include <stdio.h>
#include "file_handler.h"
#include "sentence_handler.h"
#include "date_manipulation.h"
#include "offset_handler.h"

static char *primary_sentence = "Begin";
static char *secondary_sentence = "Begin";
static time_t last_recorded_time;
static int missed_second = 0;

void get_initial_timestamps(FILE*primary_stream, stream * stream_1,
        FILE*secondary_stream, stream * stream_2) {
    time_t zero;
    while (difftime(zero, stream_1->date_and_time) == 0) {
        parse_sentence(stream_1, primary_sentence);
        primary_sentence = read_in_sentence(primary_stream);
    }

    while (difftime(zero, stream_2->date_and_time) == 0) {
        parse_sentence(stream_2, secondary_sentence);
        secondary_sentence = read_in_sentence(secondary_stream);
    }
}

void check_for_missed_seconds(stream * stream_1, stream * stream_2) {
    time_t zero;
    if (difftime(zero, last_recorded_time) == 0) {
        last_recorded_time = stream_1->date_and_time;
    }
    if (difftime(last_recorded_time, stream_1->date_and_time) < -1) {
        if (difftime(stream_1->date_and_time, stream_2->date_and_time) > 0) {
            printf("Missed time: %f\n", difftime(last_recorded_time, stream_1->date_and_time));
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
    
    double lat_offset = 0;
    double lng_offset = 0;

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
                //output stream 2 to XML
                missed_second = 0;
                last_recorded_time = stream_2->date_and_time;
            }
        }

        /*
         * 'Catch up' primary stream to secondary stream if it's timestamp
         * is further behind.
         */
        if (difftime(stream_1->date_and_time, stream_2->date_and_time) < 0) {
            primary_sentence = read_in_sentence(primary_stream);
            parse_sentence(stream_1, primary_sentence);
        }            /*
         * 'Catch up' secondary stream to primary stream if it's timestamp
         * is further behind.
         */
        else if (difftime(stream_1->date_and_time, stream_2->date_and_time) > 0) {
            secondary_sentence = read_in_sentence(secondary_stream);
            parse_sentence(stream_2, secondary_sentence);
        } else {
            /*
             * Timestamps must match, so run each stream
             * equally.
             */

            //=========== PRIMARY STREAM ===============
            /*
             * Check that we won't be outputting duplicate timestamp data.
             */
            if (difftime(last_recorded_time, stream_1->date_and_time) != 0) {
                /*
                 * Check we haven't missed a second in the timestamps.
                 */
                if (!missed_second) {
                    last_recorded_time = stream_1->date_and_time;
                    primary_sentence = read_in_sentence(primary_stream);
                    parse_sentence(stream_1, primary_sentence);
                }
            }

            //=========== SECONDARY STREAM ==============

            secondary_sentence = read_in_sentence(secondary_stream);
            parse_sentence(stream_2, secondary_sentence);
        }

        if(stream_1->latitude != 0 && stream_2->latitude != 0){
                lat_offset = update_latitude_offset(stream_1, stream_2);
                lng_offset = update_longitude_offset(stream_1, stream_2);
        }
    }
    
    free(stream_1);
    free(stream_2);
}

void run_application() {

    FILE * primary_stream = open_the_file("gps_data/gps_1.dat");
    FILE * secondary_stream = open_the_file("gps_data/gps_2.dat");

    sync_streams(primary_stream, secondary_stream);

    close_reader(primary_stream);
    close_reader(secondary_stream);
}