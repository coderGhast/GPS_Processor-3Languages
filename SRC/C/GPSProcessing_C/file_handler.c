/* 
 * File:   file_handler.c
 * Author: jee22
 *
 * Created on 19 March 2014
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <time.h>


FILE* open_the_file(char * file_name) {
    FILE * file_pointer = fopen(file_name, "r");
    return file_pointer;
}

char * read_in_sentence(FILE * file_pointer) {
    char *line = malloc(1000);
    if (file_pointer == NULL) {     
        printf("Fail");
    }

    if (file_pointer != NULL) {
        if(fscanf(file_pointer, "%s", line) != EOF){
             
        } else {
            line = "end";
        }
  }
    return line;
}

void close_reader(FILE * file_pointer){
    fclose(file_pointer);
}