/* 
 * File:   SentenceHandler.h
 * Author: jee22
 *
 * Created on 21 March 2014, 02:28
 */
#include <iostream>
#include <string>
#include "Stream.h"

#ifndef SENTENCEHANDLER_H
#define	SENTENCEHANDLER_H

using namespace std;

class SentenceHandler {
public:
    SentenceHandler();
    SentenceHandler(const SentenceHandler& orig);
    virtual ~SentenceHandler();
    
    void handle_gprmc(Stream * streamer, stringstream * sentence);
    void handle_gpgga(Stream * streamer, stringstream * sentence);
    void handle_gpgsv(Stream * streamer, stringstream * sentence);
    void parseSentence(Stream * streamer, string sentence);
private:

};

#endif	/* SENTENCEHANDLER_H */

