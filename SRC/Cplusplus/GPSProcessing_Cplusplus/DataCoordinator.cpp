/* 
 * File:   DataCoordinator.cpp
 * Author: jee22
 * 
 * Handles the flow of data throughout the application
 * and acts as a central section for getting the data
 * from the files using FileHandler, and passing/parsing
 * them through DataCoordinator, to be set where they
 * need to be set in Stream.
 * 
 * Created on 21 March 2014, 02:23
 */

#include <time.h>
#include <iostream>
#include <string>
#include "DataCoordinator.h"
#include "SentenceHandler.h"
#include "Stream.h"
#include "FileHandler.h"

/**
 * Constructor sets up the necessary classes and assigns
 * some initial values to variables that need them.
 */
DataCoordinator::DataCoordinator() {
    sentence_handler = new SentenceHandler();
    /* The strings for the sentences cannot start
     NULL or without any content. This is due to the
     'while' loop used in the syncStreams(). If they
     were blank, then the while loop would not run.*/
    primary_sentence = "Begin";
    secondary_sentence = "Begin";
    /* Make sure that the application knows that no
     second has been missed at the start. */
    missed_second = 0;
}

DataCoordinator::DataCoordinator(const DataCoordinator& orig) {
}

/**
 * On deletion of this class, delete those other classes within it
 * that are taking up memory.
 */
DataCoordinator::~DataCoordinator() {
    delete(sentence_handler);
    delete(primary_file);
    delete(secondary_file);
}

/**
 * Determining the best output stream by
 * comparing the Signal-to-Noise Ratios of the
 * satellites provided by the sentences.
 * @param stream_1 Primary stream
 * @param stream_2 Secondary stream
 */
void DataCoordinator::determineBestOutputStream(Stream * stream_1, Stream * stream_2) {
    /* If Primary stream has more than 3 satellites with an SNR of 35
     or more, then no issues, go print. */
    if (stream_1->get35AndAboveSNR() >= 3) {
        /* Output stream 1 to XML*/
        xml_creator->outputStream(stream_1);

        last_recorded_time = stream_1->getDateAndTime();
        /* If there are 3 or more satellites with an SNR
         between 30 and 35, do more checks. */
    } else if (stream_1->getBetween30And35SNR() >= 3) {
        /* If stream 2 has a better amount of satellites
         than stream 1, use that instead. */
        if (stream_2->getBetween30And35SNR() >= 3
                && stream_2->get35AndAboveSNR() >= stream_1->get35AndAboveSNR()) {
            /* Output stream 2 to XML WITH OFFSETS */

            applyOffsets(stream_2);
            xml_creator->outputStream(stream_2);
            last_recorded_time = stream_1->getDateAndTime();
        } else {
            /* Output stream 1 to XML */
            xml_creator->outputStream(stream_1);

            last_recorded_time = stream_1->getDateAndTime();
        }
        /* If the quality of the primary streams
         satellites are very bad, just use the
         secondary stream. */
    } else if (stream_1->getBetween30And35SNR() < 3
            && stream_1->get35AndAboveSNR() < 3) {
        /* Output stream 2 to XML WITH OFFSETS */
        applyOffsets(stream_2);
        xml_creator->outputStream(stream_2);

        last_recorded_time = stream_1->getDateAndTime();
    }
}

/**
 * Get the initial timestamps for the streams, so we can properly
 * sync the streams together.
 * @param stream_1 Primary stream
 * @param stream_2 Secondary stream
 */
void DataCoordinator::getInitialTimestamps(Stream * stream_1, Stream * stream_2) {
    /* Flat time to compare against other times that should
     be more than the flat default time. */
    time_t zero = 0;

    /* While primary stream doesn't have a time more than the
     default, keep reading and parsing.*/
    while (difftime(stream_1->getDateAndTime(), zero) == 0) {
        primary_sentence = primary_file->readFile();
        sentence_handler->parseSentence(stream_1, primary_sentence);
    }
    /* Same as above, but with the secondary stream. */
    while (difftime(stream_2->getDateAndTime(), zero) == 0) {
        secondary_sentence = secondary_file->readFile();
        sentence_handler->parseSentence(stream_2, secondary_sentence);
    }
}

/**
 * Check the streams for seconds missed between the last
 * recorded time and the stream 1.
 * @param stream_1 Primary stream
 * @param stream_2 Secondary stream
 */
void DataCoordinator::checkForMissedSeconds(Stream * stream_1, Stream * stream_2) {
    /* Flat time to compare against */
    time_t zero = 0;

    /* Check that the last recorded time has been given
     a time to begin with. If not, give it the most recent. */
    if (difftime(last_recorded_time, zero) == 0) {
        last_recorded_time = stream_1->getDateAndTime();
    }
    /* If the last read in time from stream 1 is further than
     1 second ahead of the last recorded time, we've missed
     a second*/
    if (difftime(last_recorded_time, stream_1->getDateAndTime()) < -1) {
        /* Check that the primary stream is being supported by
         the secondary stream. */
        if (difftime(stream_1->getDateAndTime(), stream_2->getDateAndTime()) > 0) {
            /* Set missed time as 'true'. */
            missed_second = 1;
        }
    }

}

/**
 * The core of the application, syncing the two streams
 * together so that their times match, and what to do
 * when their timestamps do match.
 */
void DataCoordinator::syncStreams() {
    cout<<"Syncing streams"<<endl;
    /* Instantiate the sentence handler in order to decide what
     to do with each sentence in the files. */
    sentence_handler = new SentenceHandler();

    /* Instantiate two new Streams to hold the data about each
     individual stream. */
    Stream * primary_stream = new Stream();
    Stream * secondary_stream = new Stream();

    /* Get a timestamp for each stream, parsing the data as it is
     read in, read for the first output to the file. */
    getInitialTimestamps(primary_stream, secondary_stream);

    /* While there are still sentences to be read in and parsed. */
    while (primary_sentence.compare("") != 0) {
        /*
         * Check that there hasn't been a second missed in the timestamps.
         * If there has been, use the secondary stream as a replacement
         * in the GPX output.
         */
        checkForMissedSeconds(primary_stream, secondary_stream);
        if (missed_second > 0) {
            if (difftime(secondary_stream->getDateAndTime(), last_recorded_time) != 0) {
                /* Output stream 2 to XML WITH OFFSETS */
                applyOffsets(secondary_stream);
                xml_creator->outputStream(secondary_stream);

                /* After putting out the second stream to make up
                 for the missing second, set missing second to
                 show that no second has been missed. */
                missed_second = 0;
                last_recorded_time = secondary_stream->getDateAndTime();
            }
        }

        /*
         * 'Catch up' primary stream to secondary stream if it's timestamp
         * is further behind.
         */
        if (difftime(primary_stream->getDateAndTime(),
                secondary_stream->getDateAndTime()) < 0) {
            /* Reads in the next sentence from the primary stream
             and parse it through the application. */
            primary_sentence = primary_file->readFile();
            sentence_handler->parseSentence(primary_stream, primary_sentence);
        }/*
         * 'Catch up' secondary stream to primary stream if it's timestamp
         * is further behind.
         */
        else if (difftime(primary_stream->getDateAndTime(),
                secondary_stream->getDateAndTime()) > 0) {
            /* Reads in the next sentence from the secondary stream
             and parse it through the application. */
            secondary_sentence = secondary_file->readFile();
            sentence_handler->parseSentence(secondary_stream, secondary_sentence);
        } else {
            /*
             * Timestamps must match, so run each stream
             * equally.
             */

            /*=========== PRIMARY STREAM =============== */
            /*
             * Check that we won't be outputting duplicate timestamp data.
             */
            if (difftime(last_recorded_time,
                    primary_stream->getDateAndTime()) != 0) {
                /*
                 * Check we haven't missed a second in the timestamps.
                 */
                if (!missed_second) {
                    /* Call to see which stream has the best satellite
                     Signal-to-Noise Ratios.*/
                    determineBestOutputStream(primary_stream, secondary_stream);
                    /* Read in the next primary sentence and parse it
                     through the application. */
                    primary_sentence = primary_file->readFile();
                    sentence_handler->parseSentence(primary_stream, primary_sentence);
                }
            }

            /*=========== SECONDARY STREAM ==============*/
            /* Reads in and parses the next sentence of the 
             secondary stream. */
            secondary_sentence = secondary_file->readFile();
            sentence_handler->parseSentence(secondary_stream, secondary_sentence);
        }

        /* If the both streams have a latitude and longitude value
         saved, calculate the offsets between them. */
        if (primary_stream->getLatitude() != 0
                && secondary_stream->getLatitude() != 0) {
            lat_offset = updateLatOffset(primary_stream, secondary_stream);
            lng_offset = updateLngOffset(primary_stream, secondary_stream);
        }
    }

    /* Free the memory through deleting the classes made that
     are no longer needed. */
    delete(primary_stream);
    delete(secondary_stream);
}

/**
 * Update the Latitude offset. 
 * @param stream_1 Primary stream
 * @param stream_2 Secondary stream
 * @return The new offset
 */
double DataCoordinator::updateLatOffset(Stream * stream_1, Stream * stream_2) {
    double double_lat;
    double_lat = stream_1->getLatitude();
    /* Get the difference between the primary and secondary stream's latitudes
     in order to be used as an offset. */
    double_lat = double_lat - stream_2->getLatitude();
    return double_lat;
}

/**
 * Update the Longitude offset.
 * @param stream_1 Primary stream
 * @param stream_2 Secondary stream
 * @return The new offset
 */
double DataCoordinator::updateLngOffset(Stream * stream_1, Stream * stream_2) {
    double double_lng;
    double_lng = stream_1->getLongitude();
    /* Get the difference between the primary and secondary stream's longitudes
     in order to be used as an offset. */
    double_lng = double_lng - stream_2->getLongitude();
    return double_lng;
}

/**
 * Apply the stored offsets to a stream.
 * @param streamer The stream to have it's lat and lng changed with the offsets
 */
void DataCoordinator::applyOffsets(Stream * streamer) {
    streamer->setLatitudeVal(streamer->getLatitude() + lat_offset);
    streamer->setLongitudeVal(streamer->getLongitude() + lng_offset);
}

/**
 * The main start and end of the application.
 */
void DataCoordinator::runApp() {
    cout<<"Begin application..."<<endl;
    /* Instantiate the classes for reading in the files, with
     the file names. */
    primary_file = new FileHandler("gps_data/gps_1.dat", 'r');
    secondary_file = new FileHandler("gps_data/gps_2.dat", 'r');

    /* Create a new XMLCreator object in order to write out the
     XML, and start by writing out the beginning of the GPX
     XML tags. */
    xml_creator = new XMLCreator();
    xml_creator->startXML();

    /* Begin the syncing of the streams. */
    syncStreams();

    /* End the GPX XML file. */
    xml_creator->endXML();

    /* Close down the files. */
    primary_file->closeReadFile();
    secondary_file->closeReadFile();
    cout<<"Application finished"<<endl;
}
