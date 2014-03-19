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
#include "date_manipulation.h"

static time_t date_and_time;
static double latitude;
static double longitude;

void setLatitude(char * string_lat, char * lat_facing){
    latitude = atof(string_lat);
    latitude = latitude / 100;
    if(strcmp(lat_facing, "S") == 0){
        latitude = latitude * -1;
    }
}

void setLongitude(char * string_lat, char * lat_facing){
    longitude = atof(string_lat);
    longitude = longitude / 100;
    if(strcmp(lat_facing, "W") == 0){
        longitude = longitude * -1;
    }
}

void handle_gprmc(char * sentence){
    char * time_string = strsep(&sentence, ","); // Get time
    strsep(&sentence, ","); // Ignore
    char * latitude = strsep(&sentence, ","); // Latitude
    char * lat_facing = strsep(&sentence, ","); // Facing (N/S)
    char * longitude = strsep(&sentence, ","); // Longitude
    char * lng_facing = strsep(&sentence, ","); // Facing (W/E)
    strsep(&sentence, ","); // unsure
    strsep(&sentence, ","); // unsure
    char * date_string = strsep(&sentence, ","); // Date
    
    setLatitude(latitude, lat_facing);
    setLongitude(longitude, lng_facing);
    date_and_time = set_date_and_time(time_string, date_string);
}

void handle_gpgga(char * sentence){
    char * time_string = strsep(&sentence, ","); // Get time
    char * latitude = strsep(&sentence, ","); // Latitude
    char * lat_facing = strsep(&sentence, ","); // Facing (N/S)
    char * longitude = strsep(&sentence, ","); // Longitude
    char * lng_facing = strsep(&sentence, ","); // Facing (W/E)
    
    setLatitude(latitude, lat_facing);
    setLongitude(longitude, lng_facing);
    update_time(time_string);
}

void handle_gpgsv(char * sentence){
    
}

void parse_sentence(char * sentence){
    char * sentence_signifier;
    sentence_signifier = strsep(&sentence, ",");
    
    if(strcmp(sentence_signifier, "$GPRMC") == 0){
        handle_gprmc(sentence);
    } else
    if(strcmp(sentence_signifier, "$GPGGA") == 0){
        handle_gpgga(sentence);
    } else
    if(strcmp(sentence_signifier, "$GPGSV") == 0){
        handle_gpgsv(sentence);
    }
}