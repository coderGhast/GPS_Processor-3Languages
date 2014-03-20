/* 
 * File:   stream_components.c
 * Author: jee22
 * 
 * Deals with the different sections of
 * a stream, and in the header file contains
 * the data related to a stream within a 
 * struct.
 */

#include "stream_components.h"

/**
 * Set the Latitude in a struct stream from a string
 * of the latitude and facing.
 * @param streamer The stream.
 * @param string_lat The Latitude as a string.
 * @param lat_facing The facing of the latitude.
 */
void set_latitude(stream * streamer, char * string_lat, char * lat_facing) {
    /* Convert the string into a double */
    streamer->latitude = atof(string_lat);
    /* Change the Latitude for the correct format for GPX. */
    streamer->latitude = streamer->latitude / 100;
    /* If the facing is South, turn the Latitude negative. */
    if (strcmp(lat_facing, "S") == 0) {
        streamer->latitude = streamer->latitude * -1;
    }
}

/**
 * Set the Longitude in a struct stream from a string
 * of the longitude and facing.
 * @param streamer The stream.
 * @param string_lat The Longitude as a string.
 * @param lat_facing The facing of the Longitude.
 */
void set_longitude(stream * streamer, char * string_lat, char * lat_facing) {
    /* Convert the string into a double */
    streamer->longitude = atof(string_lat);
    /* Change the Longitude for the correct format for GPX. */
    streamer->longitude = streamer->longitude / 100;
    /* If the facing is West, turn the Longitude negative. */
    if (strcmp(lat_facing, "W") == 0) {
        streamer->longitude = streamer->longitude * -1;
    }
}

/**
 * Set the elevation from a sentence.
 * @param streamer The stream
 * @param string_elevation The elevation as a String
 */
void set_elevation(stream * streamer, char * string_elevation) {
    /* Convert it and set it. */
    streamer->elevation = atof(string_elevation);
}

/**
 * From a stream and a string that contains a single satellites
 * snr value, convert it to a integer and see whether it is
 * between 30 and 35 or above 30. Depending what it is, increment
 * a particular value in the struct. This is used for finding
 * the quality of a stream's timestamp in data_manipulator.
 * @param streamer The Stream
 * @param snr The value of a satellites SNR in a string.
 */
void calc_snr_amounts(stream * streamer, char * snr) {
    int snr_num = atoi(snr);
    if (snr_num >= 35) {
        streamer->snrs_above_35++;
    } else if (snr_num >= 30 && snr_num < 35) {
        streamer->snrs_above_30_below_35++;
    }
}

