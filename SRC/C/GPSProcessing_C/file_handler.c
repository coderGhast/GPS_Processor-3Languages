/* 
 * File:   file_handler.c
 * Author: jee22
 *
 * Deals with reading in and outputting
 * to files for the application.
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <time.h>

/* The file pointer for outputting the data. */
static FILE * output_gpx;

/**
 * Initially opening the file as 'write' to clear
 * the file of any previous data.
 */
void initial_open() {
    output_gpx = fopen("output_c.gpx", "w");
}

/**
 * Open the already edited file.
 */
void open_file() {
    output_gpx = fopen("output_c.gpx", "a");
}

/**
 * Close the file down again.
 */
void close_file(FILE * file_pointer) {
    fclose(file_pointer);
}

/**
 * Write out the latitude and longitude waypoint
 * to the GPX file.
 * @param lat Latitude
 * @param lng Longitude
 */
void write_lat_lng(double lat, double lng) {
    open_file();
    char * wpt_start = "<wpt lat=\"";
    char * wpt_mid = "\" lon=\"";
    char * wpt_end = "\">\n";
    fprintf(output_gpx, "%s%f%s%f%s", wpt_start, lat, wpt_mid, lng, wpt_end);
    close_file(output_gpx);
}

/**
 * Write the end of the waypoint xml tag.
 */
void write_end_wpt() {
    open_file();
    char * end_wpt = "</wpt>\n";
    fprintf(output_gpx, "%s", end_wpt);
    close_file(output_gpx);
}

/**
 * Write the elevation xml tags to the gpx file.
 * @param elevation The elevation from the sentences.
 */
void write_elevation(double elevation) {
    open_file();
    char * start_ele = "<ele>";
    char * end_ele = "</ele>\n";
    fprintf(output_gpx, "%s%.2f%s", start_ele, elevation, end_ele);
    close_file(output_gpx);
}

/**
 * Write out a string to the file.
 * @param xml_string The string to be written out.
 */
void write_to_file(char * xml_string) {
    open_file();
    fprintf(output_gpx, "%s", xml_string);
    close_file(output_gpx);
}

/**
 * Opens the file for reading in, e.g. gps_1.dat
 * @param file_name The file name
 * @return  The file pointer.
 */
FILE* open_the_file(char * file_name) {
    FILE * file_pointer = fopen(file_name, "r");
    return file_pointer;
}

/**
 * Read in a single sentence from the file
 * and return it in order to be parsed by
 * the application.
 * @param file_pointer The file to be read.
 * @return The sentence returned.
 */
char * read_in_sentence(FILE * file_pointer) {
    char *line = malloc(1000);
    if (file_pointer == NULL) {
        printf("Fail");
    }

    if (file_pointer != NULL) {
        if (fscanf(file_pointer, "%s", line) != EOF) {

        } else {
            line = "end";
        }
    }
    return line;
}