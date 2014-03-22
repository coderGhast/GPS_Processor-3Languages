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
 * of the latitude and facing, converting the NMEA into decimal.
 * @param streamer The stream.
 * @param string_lat The Latitude as a string.
 * @param lat_facing The facing of the latitude.
 */
void set_latitude(stream * streamer, char * string_lat, char * lat_facing) {
    char *decimal;

    /* Find the decimal point in the string*/
    decimal = strchr(string_lat, '.');
    /* Get the index of the start of the 'time' through -2 of the decimal*/
    int start_of_time = ((int)(decimal - string_lat)) - 2;
    
    /* Put aside some c style strings for the degrees and time. */
    char * degrees = calloc(1, sizeof string_lat);
    char * time = calloc(1, sizeof string_lat);
    
    /* Take apart the latitude string to get the components for
     the degrees and time */
    strncpy(degrees, string_lat, start_of_time);
    strncpy(time, string_lat + start_of_time, sizeof string_lat);
    
    /* Convert the strings into numbers. */
    int degrees_int = atoi(degrees);
    double time_dbl = atof(time);
    
    /* Convert the NMEA coordinates into decimal.*/
    time_dbl = time_dbl / 60;
    time_dbl = time_dbl + degrees_int;
    
    /* If the facing is South, turn the Latitude negative. */
    if (strcmp(lat_facing, "S") == 0) {
        time_dbl = time_dbl * -1;
    }
    
    /* Set it! */
    streamer->latitude = time_dbl;
   
    /* Free up the calloc'd memory. */
    free(degrees);
    free(time);
}

/**
 * Set the Longitude in a struct stream from a string
 * of the longitude and facing.
 * @param streamer The stream.
 * @param string_lng The Longitude as a string.
 * @param lng_facing The facing of the Longitude.
 */
void set_longitude(stream * streamer, char * string_lng, char * lng_facing) {
    char *decimal;

    /* Find the decimal point in the string*/
    decimal = strchr(string_lng, '.');
    int start_of_time = ((int)(decimal - string_lng)) - 2;
    
    /* Put aside some c style strings for the degrees and time. */
    char * degrees = calloc(1, sizeof string_lng);
    char * time = calloc(1, sizeof string_lng);
    
    /* Take apart the latitude string to get the components for
     the degrees and time */
    strncpy(degrees, string_lng, start_of_time);
    strncpy(time, string_lng + start_of_time, sizeof string_lng);
    
    /* Convert the strings into numbers. */
    int degrees_int = atoi(degrees);
    double time_dbl = atof(time);
    
    /* Convert the NMEA coordinates into decimal.*/
    time_dbl = time_dbl / 60;
    time_dbl = time_dbl + degrees_int;
    
    /* If the facing is West, turn the Latitude negative. */
    if (strcmp(lng_facing, "W") == 0) {
        time_dbl = time_dbl * -1;
    }
    
    /* Set it! */
    streamer->longitude = time_dbl;
    
    /* Free up the calloc'd memory. */
    free(degrees);
    free(time);
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

