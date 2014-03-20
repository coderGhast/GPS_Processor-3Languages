/* 
 * File:   stream_components.h
 * Author: jee22
 *
 * Created on 20 March 2014, 01:29
 */

#ifndef STREAM_COMPONENTS_H
#define	STREAM_COMPONENTS_H

#ifdef	__cplusplus
extern "C" {
#endif




#ifdef	__cplusplus
}
#endif

#endif	/* STREAM_COMPONENTS_H */

#include <time.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>
#include "date_manipulation.h"

typedef struct stream_struct {
    time_t date_and_time;
    double latitude;
    double longitude;
    double elevation;
    int expected_sentence_number;
    int snrs_above_30_below_35;
    int snrs_above_35;
} stream;

void set_latitude(stream * streamer, char * string_lat, char * lat_facing);
void set_longitude(stream * streamer, char * string_lat, char * lat_facing);
void set_elevation(stream * streamer, char * string_elevation);
void calc_snr_amounts(stream * streamer, char * snr);