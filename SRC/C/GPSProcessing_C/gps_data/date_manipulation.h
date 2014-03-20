/* 
 * File:   date_manipulation.h
 * Author: jee22
 *
 * Created on 19 March 2014, 19:26
 */

#ifndef DATE_MANIPULATION_H
#define	DATE_MANIPULATION_H

#ifdef	__cplusplus
extern "C" {
#endif



#ifdef	__cplusplus
}
#endif

#endif	/* DATE_MANIPULATION_H */

time_t new_date(int day, int month, int year, int hours, 
        int minutes, int seconds);
void print_date(time_t time);
time_t set_date_and_time(char * time_string, char * date_string);
time_t update_time(char * time_string, time_t time_to_update);