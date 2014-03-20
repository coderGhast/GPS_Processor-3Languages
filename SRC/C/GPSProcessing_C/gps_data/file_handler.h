/* 
 * File:   file_handler.h
 * Author: jee22
 *
 */

#ifndef FILE_HANDLER_H
#define	FILE_HANDLER_H

#ifdef	__cplusplus
extern "C" {
#endif




#ifdef	__cplusplus
}
#endif

#endif	/* FILE_HANDLER_H */

void initial_open();
void open_file();
void close_file(FILE * file_pointer);
void write_lat_lng(double lat, double lng);
void write_end_wpt();
void write_elevation(double elevation);
FILE * open_the_file(char * file_name);
char * read_in_sentence();
