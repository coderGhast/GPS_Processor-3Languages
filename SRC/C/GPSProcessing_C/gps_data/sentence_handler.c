/* 
 * File:   sentence_handler.c
 * Author: jee22
 *
 * Takes in sentences and determines what
 * they are and what to do with them.
 */

#include <time.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>
#include "stream_components.h"

/**
 * Takes a GPRMC sentence and takes the needed data.
 * @param streamer The stream for the sentence.
 * @param sentence The sentence itself.
 */
void handle_gprmc(stream * streamer, char * sentence) {
    char * time_string = strsep(&sentence, ","); /* Get time */
    strsep(&sentence, ","); /* Ignore */
    char * latitude = strsep(&sentence, ","); /* Latitude */
    char * lat_facing = strsep(&sentence, ","); /* Facing (N/S) */
    char * longitude = strsep(&sentence, ","); /* Longitude */
    char * lng_facing = strsep(&sentence, ","); /* Facing (W/E) */
    strsep(&sentence, ","); /* Ignore */
    strsep(&sentence, ","); /* Ignore */
    char * date_string = strsep(&sentence, ","); /* Date */

    set_latitude(streamer, latitude, lat_facing);
    set_longitude(streamer, longitude, lng_facing);
    streamer->date_and_time = set_date_and_time(time_string, date_string);
}

/**
 * Takes a GPGGA sentence and gets the needed data from it.
 * @param streamer The stream where the sentence came from.
 * @param sentence The sentence itself.
 */
void handle_gpgga(stream * streamer, char * sentence) {
    char * time_string = strsep(&sentence, ","); /* Get time */
    char * latitude = strsep(&sentence, ","); /* Latitude */
    char * lat_facing = strsep(&sentence, ","); /* Facing (N/S) */
    char * longitude = strsep(&sentence, ","); /* Longitude */
    char * lng_facing = strsep(&sentence, ","); /* Facing (W/E) */
    strsep(&sentence, ","); /* Ignore */
    strsep(&sentence, ","); /* Ignore */
    strsep(&sentence, ","); /* Ignore */
    char * elevation = strsep(&sentence, ","); /* Elevation */

    set_latitude(streamer, latitude, lat_facing);
    set_longitude(streamer, longitude, lng_facing);
    set_elevation(streamer, elevation);
    update_time(time_string, streamer->date_and_time);
}

/**
 * Dealing with a GPGSV, and getting the data needed.
 * @param streamer The stream the sentence came from.
 * @param sentence The sentence itself.
 */
void handle_gpgsv(stream * streamer, char * sentence) {
    strsep(&sentence, ","); /* Ignore - Total number of GSVs for this set. */
    int sentence_number = atoi(strsep(&sentence, ","));
    strsep(&sentence, ","); /* Ignore - Total number of satellites */

    /* If the sentence is the first, start the stream recordings
     for the satellites from 0 again. */
    if (sentence_number == 1) {
        streamer->snrs_above_30_below_35 = 0;
        streamer->snrs_above_35 = 0;
    }

    int last_line = 0;
    /* While the last line hasn't been reached yet, keep going over the
     sentence. */
    while (last_line != 1) {
        strsep(&sentence, ","); /* Ignore - Satellite PRN */
        strsep(&sentence, ","); /* Ignore - Elevation */
        strsep(&sentence, ","); /* Ignore - Azimuth */
        char * snr = strsep(&sentence, ",");
        
        /* Check that the length of the SNR section is the same
         when checked to remove an asterisk (denoting that it's
         the last line of the sentence with the checksum */
        size_t string_length = strlen(snr);
        size_t span_without_asterisk = strcspn(snr, "*");

        if (span_without_asterisk != string_length) {
            /* Get the value before the asterisk and check
             to show that this is the last section of the line. */
            snr = strsep(&snr, "*");
            last_line = 1;
        }

        /* If the snr value is higher than 0, increment the
         number of satellites through a call to cald_snr_amounts()*/
        if (strcmp(snr, "") != 0) {
            calc_snr_amounts(streamer, snr);
        }
    }
}

/**
 * Take a sentence and find out which type of sentence it
 * is and how it should be handled.
 * @param streamer The stream the sentence came from.
 * @param sentence The sentence itself.
 */
void parse_sentence(stream * streamer, char * sentence) {
    char * sentence_signifier;
    sentence_signifier = strsep(&sentence, ",");

    if (strcmp(sentence_signifier, "$GPRMC") == 0) {
        handle_gprmc(streamer, sentence);
    } else
        if (strcmp(sentence_signifier, "$GPGGA") == 0) {
        handle_gpgga(streamer, sentence);
    } else
        if (strcmp(sentence_signifier, "$GPGSV") == 0) {
        handle_gpgsv(streamer, sentence);
    }
}