/* 
 * File:   xml_creator.c
 * Author: jee22
 * 
 * Handles part of the XML tagging
 * and sending it to be written out.
 * 
 */

#include <string.h>
#include <stdlib.h>
#include <stddef.h>
#include <time.h>
#include <stdio.h>
#include "stream_components.h"
#include "offset_handler.h"

/**
 * The start of the GPX XML file.
 */
void start_xml() {
    char * xml_tag;
    xml_tag = "<gpx version=\"1.0\"\n"
            "creator=\"ExpertGPS 1.1 - http://www.topografix.com\"\n"
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            "xmlns=\"http://www.topografix.com/GPX/1/0\"\n"
            "xsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 "
            "http://www.topografix.com/GPX/1/0/gpx.xsd\">\n";

    write_to_file(xml_tag);
}

/**
 * The end of the GPX XML file.
 */
void end_xml() {
    char * xml_tag;
    xml_tag = "</gpx>";

    write_to_file(xml_tag);
}

/**
 * Take a time and put it into a format for the GPX XML
 * file.
 * @param date_and_time The time to be formatted.
 */
void format_date_and_time(time_t date_and_time) {
    time_t to_format = date_and_time;
    char buffer[40];
    /* The time is formated using strftime, based on how it should be
     for the GPX XML file.*/
    strftime(buffer, 40, "<time>%Y-%m-%dT%H:%M:%SZ</time>\n", localtime(&to_format));
    char * xml_tag = malloc(40);
    xml_tag = buffer;

    write_to_file(xml_tag);
}

/**
 * A call to output the different sections of a Way point for
 * the particular stream and it's current point.
 * @param streamer The stream to be output.
 */
void output_stream(stream * streamer) {
    write_lat_lng(streamer->latitude, streamer->longitude);
    write_elevation(streamer->elevation);
    format_date_and_time(streamer->date_and_time);
    write_end_wpt();
}