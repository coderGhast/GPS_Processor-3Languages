#include <stdio.h>
#include <stdlib.h>
#include <time.h>

time_t new_date(int day, int month, int year, int hours, 
        int minutes, int seconds){    
    struct tm str_time;
    time_t new_date;

    str_time.tm_year = year-1900;
    str_time.tm_mon = month -1;
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

time_t set_date_and_time(char * time_string, char * date_string){
    char time_char_1;
    char time_char_2;
    char time_holder[2];
    char * ptr;
    
    time_char_1 = time_string[0];
    time_char_2 = time_string[1];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int hour = strtol(time_holder, &ptr,10);
    
    time_char_1 = time_string[2];
    time_char_2 = time_string[3];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int min = strtol(time_holder, &ptr,10);
    
    time_char_1 = time_string[4];
    time_char_2 = time_string[5];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int sec = strtol(time_holder, &ptr,10);
    
    time_char_1 = date_string[0];
    time_char_2 = date_string[1];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int day = strtol(time_holder, &ptr,10);
    
    time_char_1 = date_string[2];
    time_char_2 = date_string[3];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int month = strtol(time_holder, &ptr,10);
    
    time_char_1 = date_string[4];
    time_char_2 = date_string[5];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    
    int year = strtol(time_holder, &ptr,10) + 2000;
    
    return new_date(day, month, year, hour, min, sec);
}

time_t update_time(char * time_string, time_t time_to_update){
    char time_char_1;
    char time_char_2;
    char time_holder[2];
    char * ptr;
    
    time_char_1 = time_string[0];
    time_char_2 = time_string[1];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int hour = strtol(time_holder, &ptr,10);
    
    time_char_1 = time_string[2];
    time_char_2 = time_string[3];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int min = strtol(time_holder, &ptr,10);
    
    time_char_1 = time_string[4];
    time_char_2 = time_string[5];
    time_holder[2];
    time_holder[0] = time_char_1;
    time_holder[1] = time_char_2;
    
    int sec = strtol(time_holder, &ptr,10);
    
    struct tm *current_time = gmtime(&time_to_update);
    
    current_time->tm_hour = hour;
    current_time->tm_min = min;
    current_time->tm_sec = sec;
    current_time->tm_isdst = 0;

    time_to_update = mktime(current_time);
    return time_to_update;
}