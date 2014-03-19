#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/*
 * Constructs a new dayinfo struct to hold the information
 * about the given date and time using the functions provided
 * by the standard library 'time'.
 */
time_t new_date(int day, int month, int year, int hours, 
        int minutes, int seconds){    
    struct tm str_time;
    time_t new_date;

    str_time.tm_year = year-1900;
    str_time.tm_mon = month - 1;
    str_time.tm_mday = day;
    str_time.tm_hour = hours;
    str_time.tm_min = minutes;
    str_time.tm_sec = seconds;
    str_time.tm_isdst = 0;

    new_date = mktime(&str_time);
    
    return new_date;
}

void print_date(time_t time){
    fprintf(stdout, "%s", ctime(&time));
}

/*
 * Calculates the time passed between the Ship file record and Mayday report.
 */
double time_passed(time_t time_1, time_t time_2){
    double time_passed = 0;
    time_passed = difftime(time_1, time_2);
 
    return time_passed;
}

time_t set_date_and_time(char * time_string, char * date_string){
    char time_char_1;
    char time_char_2;
    char time_holder[2];
    
    time_char_1 = time_string[0];
    time_char_2 = time_string[1];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int hour = atoi(time_holder);
    
    time_char_1 = time_string[2];
    time_char_2 = time_string[3];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int min = atoi(time_holder);
    
    time_char_1 = time_string[4];
    time_char_2 = time_string[5];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int sec = atoi(time_holder);
    
    time_char_1 = date_string[0];
    time_char_2 = date_string[1];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int day = atoi(time_holder);
    
    time_char_1 = date_string[2];
    time_char_2 = date_string[3];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int month = atoi(time_holder);
    
    time_char_1 = date_string[4];
    time_char_2 = date_string[5];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int year = atoi(time_holder);
    
    return new_date(day, month, year, hour, min, sec);
}

time_t update_time(char * time_string, time_t time_to_update){
    char time_char_1;
    char time_char_2;
    char time_holder[2];
    
    time_char_1 = time_string[0];
    time_char_2 = time_string[1];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int hour = atoi(time_holder);
    
    time_char_1 = time_string[2];
    time_char_2 = time_string[3];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int min = atoi(time_holder);
    
    time_char_1 = time_string[4];
    time_char_2 = time_string[5];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int sec = atoi(time_holder);
    
    return time_to_update;
}