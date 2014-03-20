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
#include "stream_components.h"


void handle_gprmc(stream * streamer, char * sentence);
void handle_gpgga(stream * streamer, char * sentence);
void handle_gpgsv(stream * streamer, char * sentence);
void parse_sentence(stream * streamer, char * sentence);
(stream * streamer, char * sentence);
void handle_gpgsv(stream * streamer, char * sentence);
void parse_sentence(stream * streamer, char * sentence);
