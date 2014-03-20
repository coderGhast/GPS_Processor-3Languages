/* 
 * File:   sentence_handler.c
 * Author: jee22
 *
 * Created on 19 March 2014
 */

#include <time.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>
#include "stream_components.h"

void setLatitude(stream * streamer, char * string_lat, char * lat_facing){
    streamer->latitude = atof(string_lat);
    streamer->latitude = streamer->latitude / 100;
    if(strcmp(lat_facing, "S") == 0){
        streamer->latitude = streamer->latitude * -1;
    }
}

void setLongitude(stream * streamer, char * string_lat, char * lat_facing){
    streamer->longitude = atof(string_lat);
    streamer->longitude = streamer->longitude / 100;
    if(strcmp(lat_facing, "W") == 0){
        streamer->longitude = streamer->longitude * -1;
    }
}

void handle_gprmc(stream * streamer, char * sentence){
    char * time_string = strsep(&sentence, ","); // Get time
    strsep(&sentence, ","); // Ignore
    char * latitude = strsep(&sentence, ","); // Latitude
    char * lat_facing = strsep(&sentence, ","); // Facing (N/S)
    char * longitude = strsep(&sentence, ","); // Longitude
    char * lng_facing = strsep(&sentence, ","); // Facing (W/E)
    strsep(&sentence, ","); // unsure
    strsep(&sentence, ","); // unsure
    char * date_string = strsep(&sentence, ","); // Date
    
    setLatitude(streamer, latitude, lat_facing);
    setLongitude(streamer, longitude, lng_facing);
    streamer->date_and_time = set_date_and_time(time_string, date_string);
}

void handle_gpgga(stream * streamer, char * sentence){
    char * time_string = strsep(&sentence, ","); // Get time
    char * latitude = strsep(&sentence, ","); // Latitude
    char * lat_facing = strsep(&sentence, ","); // Facing (N/S)
    char * longitude = strsep(&sentence, ","); // Longitude
    char * lng_facing = strsep(&sentence, ","); // Facing (W/E)
    
    setLatitude(streamer, latitude, lat_facing);
    setLongitude(streamer, longitude, lng_facing);
    update_time(time_string, streamer->date_and_time);
}

void handle_gpgsv(stream * streamer, char * sentence){
    
}

void parse_sentence(stream * streamer, char * sentence){
    char * sentence_signifier;
    sentence_signifier = strsep(&sentence, ",");
    
    if(strcmp(sentence_signifier, "$GPRMC") == 0){
        handle_gprmc(streamer, sentence);
    } else
    if(strcmp(sentence_signifier, "$GPGGA") == 0){
        handle_gpgga(streamer, sentence);
    } else
    if(strcmp(sentence_signifier, "$GPGSV") == 0){
        handle_gpgsv(streamer, sentence);
    }
}