/* 
 * File:   sentence_handler.h
 * Author: jee22
 *
 * Created on 19 March 2014, 17:12
 */

#ifndef SENTENCE_HANDLER_H
#define	SENTENCE_HANDLER_H

#ifdef	__cplusplus
extern "C" {
#endif




#ifdef	__cplusplus
}
#endif

#endif	/* SENTENCE_HANDLER_H */

#include <time.h>

void setLatitude(char * string_lat, char * lat_facing);
void setLongitude(char * string_lat, char * lat_facing);
void handle_gprmc(char * sentence);
void handle_gpgga(char * sentence);
void handle_gpgsv(char * sentence);
void parse_sentence(char * sentence);
