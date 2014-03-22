/* 
 * File:   SentenceHandler.cpp
 * Author: jee22
 *
 * Takes in sentences and determines what
 * they are and what to do with them.
 */

#include <string>
#include <iostream>
#include <sstream>
#include <time.h>
#include "SentenceHandler.h"
#include "Stream.h"

using namespace std;

SentenceHandler::SentenceHandler() {
}

SentenceHandler::SentenceHandler(const SentenceHandler& orig) {
}

SentenceHandler::~SentenceHandler() {
}

/**
 * Takes a GPRMC sentence and takes the needed data.
 * @param streamer The stream for the sentence.
 * @param sentence The sentence itself.
 */
void SentenceHandler::handle_gprmc(Stream * streamer, stringstream * sentence){
    string dont_care;
    string time_string;
    string date_string;
    string location; /* For Latitude, Longitude and Elevation.*/
    string facing; /* N,E,S,W */
    
    getline(*sentence, time_string, ','); /* Get time */
    
    getline(*sentence, dont_care, ','); /* Ignore */
    
    getline(*sentence, location, ','); /* Get Latitude */
    getline(*sentence, facing, ','); /* Get facing */
    streamer->setLatitude(location, facing);
    
    getline(*sentence, location, ','); /* Get Longitude */
    getline(*sentence, facing, ','); /* Get facing */
    streamer->setLongitude(location, facing);

    getline(*sentence, dont_care, ','); /* Ignore */
    getline(*sentence, dont_care, ','); /* Ignore */
    
    getline(*sentence, date_string, ','); /* Get date */
    streamer->setDateAndTime(date_string, time_string);
}

/**
 * Takes a GPGGA sentence and gets the needed data from it.
 * @param streamer The stream where the sentence came from.
 * @param sentence The sentence itself.
 */
void SentenceHandler::handle_gpgga(Stream * streamer, stringstream * sentence){
    string dont_care;
    string time_string;
    string location;
    string facing;
    
    getline(*sentence, time_string, ','); /* Get time */
    streamer->updateTime(time_string);
    
    getline(*sentence, location, ','); /* Get Latitude */
    getline(*sentence, facing, ','); /* Get facing (N/S) */
    streamer->setLatitude(location, facing);
    
    getline(*sentence, location, ','); /* Get Longitude */
    getline(*sentence, facing, ','); /* Get facing (E/W) */
    streamer->setLongitude(location, facing);
    
    getline(*sentence, dont_care, ','); /* Ignore */
    getline(*sentence, dont_care, ','); /* Ignore */
    getline(*sentence, dont_care, ','); /* Ignore */
    
    getline(*sentence, location, ','); /* Elevation */
    streamer->setElevation(location);

    streamer->updateTime(time_string);    
}

/**
 * Dealing with a GPGSV, and getting the data needed.
 * @param streamer The stream the sentence came from.
 * @param sentence The sentence itself.
 */
void SentenceHandler::handle_gpgsv(Stream * streamer, stringstream * sentence){
    string dont_care;
    getline(*sentence, dont_care, ','); /* Ignore - Total number of GSVs for this set. */
    getline(*sentence, dont_care, ','); /* Sentence Number */
    int sentence_number;
    stringstream ss(dont_care);
    ss >> sentence_number;
    getline(*sentence, dont_care, ','); /* Ignore - Total number of satellites */

    /* If the sentence is the first, start the stream recordings
     for the satellites from 0 again. */
    if (sentence_number == 1) {
        streamer->set35AndAboveSNR(0);
        streamer->setBetween30And35SNR(0);
    }

    int last_line = 0;
    /* While the last line hasn't been reached yet, keep going over the
     sentence. */
    while (last_line != 1) {
        getline(*sentence, dont_care, ','); /* Ignore - Satellite PRN */
        getline(*sentence, dont_care, ','); /* Ignore - Elevation */
        getline(*sentence, dont_care, ','); /* Ignore - Azimuth */
        
        string snr;
        getline(*sentence, snr, ','); /* SNR value */
        
        int asterisk = snr.find('*');
        
        if (asterisk != string::npos) {
            /* Get the value before the asterisk and check
             to show that this is the last section of the line. */
            snr = snr.substr(0, asterisk);
            last_line = 1;
        }

        /* If the snr value is higher than 0, increment the
         number of satellites through a call to calc_snr_amounts()*/
        if (snr.compare("") != 0) {
            streamer->calcSNRAmounts(snr);
        }
    }
}

void SentenceHandler::parseSentence(Stream * streamer, string sentence){
    stringstream linestream(sentence);
    string sentence_signifier;
    getline(linestream, sentence_signifier, ',');
    if(sentence_signifier.compare("$GPRMC") == 0){
        handle_gprmc(streamer, &linestream);
    } else if(sentence_signifier.compare("$GPGGA") == 0){
        handle_gpgga(streamer, &linestream);
    } else if(sentence_signifier.compare("$GPGSV") == 0){
        handle_gpgsv(streamer, &linestream);
    }
}
