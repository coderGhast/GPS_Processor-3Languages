/* 
 * File:   DataCoordinator.h
 * Author: jee22
 *
 * Created on 21 March 2014, 02:23
 */

#ifndef DATACOORDINATOR_H
#define	DATACOORDINATOR_H

#include <time.h>
#include "XMLCreator.h"
#include "Stream.h"
#include "FileHandler.h"
#include "SentenceHandler.h"

class DataCoordinator {
public:
    DataCoordinator();
    DataCoordinator(const DataCoordinator& orig);
    virtual ~DataCoordinator();

    void runApp();
private:
    SentenceHandler * sentence_handler;
    string primary_sentence;
    string secondary_sentence;
    FileHandler * primary_file;
    FileHandler * secondary_file;
    time_t last_recorded_time;
    int missed_second;
    double lat_offset;
    double lng_offset;
    XMLCreator * xml_creator;

    void determineBestOutputStream(Stream * stream_1, Stream * stream_2);
    void getInitialTimestamps(Stream * stream_1, Stream * stream_2);
    void checkForMissedSeconds(Stream * stream_1, Stream * stream_2);
    void syncStreams();
    double updateLatOffset(Stream * stream_1, Stream * stream_2);
    double updateLngOffset(Stream * stream_1, Stream * stream_2);
    void applyOffsets(Stream * streamer);
};

#endif	/* DATACOORDINATOR_H */

