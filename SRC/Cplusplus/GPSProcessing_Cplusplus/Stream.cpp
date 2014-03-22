/* 
 * File:   Stream.cpp
 * Author: jee22
 * 
 * Created on 21 March 2014, 02:29
 */

#include <time.h>
#include <iostream>
#include <stdlib.h>
#include <sstream>
#include <string>
#include "Stream.h"

Stream::Stream() {
    date_and_time = 0;
}

Stream::Stream(const Stream& orig) {
}

Stream::~Stream() {
}

double Stream::getLatitude() {
    return latitude;
}

void Stream::setLatitude(string string_lat, string lat_facing) {
    latitude = atof(string_lat.c_str());
    latitude = latitude / 100;
    if (lat_facing.compare("S") == 0) {
        latitude = latitude * -1;
    }
}

void Stream::setLatitudeVal(double val){
    latitude = val;
}

double Stream::getLongitude() {
    return longitude;
}

void Stream::setLongitude(string string_lng, string lng_facing) {
    longitude = atof(string_lng.c_str());
    longitude = longitude / 100;
    if (lng_facing.compare("W") == 0) {
        longitude = longitude * -1;
    }
}

void Stream::setLongitudeVal(double val){
    longitude = val;
}

double Stream::getElevation() {
    return elevation;
}

void Stream::setElevation(string string_elevation) {
    elevation = atof(string_elevation.c_str());
}

time_t Stream::getDateAndTime() {
    return date_and_time;
}

void Stream::setDateAndTime(string date_string, string time_string) {
    struct tm new_time;
    int temp = 0;

    /* Convert and set the date from the ddmmyy string. */
    stringstream convert_day(date_string.substr(0, 2));
    convert_day >> new_time.tm_mday;
    stringstream convert_mon(date_string.substr(2, 2));
    temp = 0;
    convert_mon >> temp;
    new_time.tm_mon = temp - 1;
    stringstream convert_year(date_string.substr(4, 2));
    temp = 0;
    convert_year >> temp;
    temp = temp + 2000;
    new_time.tm_year = temp - 1900;

    /* Convert and set the time from the hhmmss string. */
    stringstream convert_hour(time_string.substr(0, 2));
    convert_hour >> new_time.tm_hour;
    stringstream convert_min(time_string.substr(2, 2));
    temp = 0;
    convert_min >> temp;
    new_time.tm_min = temp;
    stringstream convert_sec(time_string.substr(4, 2));
    temp = 0;
    convert_sec >> temp;
    new_time.tm_sec = temp;

    new_time.tm_isdst = 0;

    date_and_time = mktime(&new_time);
}

void Stream::updateTime(string time_string) {
    struct tm * new_time = gmtime(&date_and_time);
    int temp;

    /* Convert and set the time from the hhmmss string. */
    stringstream convert_hour(time_string.substr(0, 2));
    convert_hour >> new_time->tm_hour;
    stringstream convert_min(time_string.substr(2, 2));
    temp = 0;
    convert_min >> temp;
    new_time->tm_min = temp;
    stringstream convert_sec(time_string.substr(4, 2));
    temp = 0;
    convert_sec >> temp;
    new_time->tm_sec = temp;

    new_time->tm_isdst = 0;

    date_and_time = mktime(new_time);
}

void Stream::calcSNRAmounts(string snr) {
    int snr_num = atoi(snr.c_str());
    if (snr_num >= 35) {
        snrs_above_35++;
    } else if (snr_num >= 30 && snr_num < 35) {
        snrs_above_30_below_35++;
    }
}

int Stream::get35AndAboveSNR() {
    return snrs_above_35;
}

void Stream::set35AndAboveSNR(int snr_values) {
    snrs_above_35 = snr_values;
}

int Stream::getBetween30And35SNR() {
    return snrs_above_30_below_35;
}

void Stream::setBetween30And35SNR(int snr_values) {
    snrs_above_30_below_35 = snr_values;
}