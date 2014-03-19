/* 
 * File:   data_coordinator.c
 * Author: jee22
 *
 * Created on 19 March 2014
 */

#include <stdio.h>
#include "file_handler.h"
#include "sentence_handler.h"

void run_application(){
    char *primary_sentence = "Begin";
    char *secondary_sentence = "Begin";
    
    FILE * primary_stream = open_the_file("gps_data/gps_1.dat");
    FILE * secondary_stream = open_the_file("gps_data/gps_2.dat");
    
    int i = 0;
    while(strcmp(primary_sentence, "end") != 0){        
        parse_sentence(primary_sentence);
        primary_sentence = read_in_sentence(primary_stream);
        secondary_sentence = read_in_sentence(secondary_stream);

    }
    close_reader(primary_stream);
}
