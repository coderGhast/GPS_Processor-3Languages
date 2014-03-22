/* 
 * File:   Stream.cpp
 * Author: jee22
 * 
 * All data and methods related to
 * handling the aspects of a gps stream that
 * we are interested in.
 * 
 * Created on 21 March 2014, 02:29
 */

#include <time.h>
#include <iostream>
#include <stdlib.h>
#include <sstream>
#include <string>
#include "Stream.h"

/**
 * Make sure we're starting on 0 for our
 * date and time.
 */
Stream::Stream() {
    date_and_time = 0;
}

Stream::Stream(const Stream& orig) {
}

Stream::~Stream() {
}

/**
 * Get the latitude
 * @return Latitude
 */
double Stream::getLatitude() {
    return latitude;
}

/**
 * Set the Latitude from a string, converting it from
 * the NMEA type into a decimal format used in GPX.
 * @param string_lat The latitude as a string
 * @param lat_facing The facing of the latitude.
 */
void Stream::setLatitude(string string_lat, string lat_facing) {
    /* Set aside strings for the parts of the latitude */
    string degrees;
    string time;

    /* Get the start of the time section through finding the decimal.*/
    int start_of_time = string_lat.find('.') - 2;

    /* Separate the latitude string into the degrees and time parts. */
    degrees = string_lat.substr(0, start_of_time);
    time = string_lat.substr(start_of_time, string_lat.size() - start_of_time);

    /* Convert the strings into numbers. */
    int degrees_int = atoi(degrees.c_str());
    double time_dbl = atof(time.c_str());

    /* Convert from NMEA location to decimal. */
    time_dbl = degrees_int + (time_dbl / 60);

    /* If the facing is South, make it so Mr Sulu in the decimal.*/
    if (lat_facing.compare("S") == 0) {
        time_dbl = time_dbl * -1;
    }

    /* Set it! */
    latitude = time_dbl;
}

/**
 * Set the Latitude with an actual numerical value.
 * @param val The latitude.
 */
void Stream::setLatitudeVal(double val) {
    latitude = val;
}

/**
 * Get the Longitude
 * @return Longitude
 */
double Stream::getLongitude() {
    return longitude;
}

/**
 * Set the Longitude from a string, converting it from
 * the NMEA type into a decimal format used in GPX.
 * @param string_lng The longitude as a string
 * @param lng_facing The facing of the longitude.
 */
void Stream::setLongitude(string string_lng, string lng_facing) {
    /* Set aside strings for the parts of the latitude */
    string degrees;
    string time;

    /* Get the start of the time section through finding the decimal.*/
    int start_of_time = string_lng.find('.') - 2;

    /* Separate the longitude string into the degrees and time parts. */
    degrees = string_lng.substr(0, start_of_time);
    time = string_lng.substr(start_of_time, string_lng.size() - start_of_time);

    /* Convert the strings into numbers. */
    int degrees_int = atoi(degrees.c_str());
    double time_dbl = atof(time.c_str());

    /* Convert from NMEA location to decimal. */
    time_dbl = degrees_int + (time_dbl / 60);

    /* If the facing is West, make it so Mr Sulu in the decimal.*/
    if (lng_facing.compare("W") == 0) {
        time_dbl = time_dbl * -1;
    }

    /* Set it! */
    longitude = time_dbl;
}

/**
 * Set the Longitude with an actual numerical value.
 * @param val The Longitude.
 */
void Stream::setLongitudeVal(double val) {
    longitude = val;
}

/**
 * Get the elevation at the current point in time.
 * @return Elevation
 */
double Stream::getElevation() {
    return elevation;
}

/**
 * Set the elevation from a string, converting it into
 * a double
 * @param string_elevation The elevation represented by a string.
 */
void Stream::setElevation(string string_elevation) {
    elevation = atof(string_elevation.c_str());
}

/**
 * Get the date and time in a time_t struct.
 * @return The date and time as a time_t struct.
 */
time_t Stream::getDateAndTime() {
    return date_and_time;
}

/**
 * Set a new date and time from a string representing the date
 * and a string representing the time.
 * @param date_string Date
 * @param time_string Time
 */
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

/**
 * Update the time in this class with a new string representing time, 
 * while leaving the date the same.
 * @param time_string Time
 */
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

/**
 * Calculate what to do with a single SNR value of a satellite
 * when passed to this class. Whether it is or above 35, or
 * between 30 and 35.
 * @param snr Signal to Noise Ratio of a satellite signal.
 */
void Stream::calcSNRAmounts(string snr) {
    int snr_num = atoi(snr.c_str());
    if (snr_num >= 35) {
        snrs_above_35++;
    } else if (snr_num >= 30 && snr_num < 35) {
        snrs_above_30_below_35++;
    }
}

/**
 * Get the amount of satellites with an SNR value at or above 35 for
 * this stream as per the last read in GPGSVs.
 * @return The number.
 */
int Stream::get35AndAboveSNR() {
    return snrs_above_35;
}

/**
 * Set the number of SNR values for 35 and above.
 * @param snr_values The values
 */
void Stream::set35AndAboveSNR(int snr_values) {
    snrs_above_35 = snr_values;
}

/**
 * Get the amount of satellites with an SNR value between 30 and 35 for
 * this stream as per the last read in GPGSVs.
 * @return The number.
 */
int Stream::getBetween30And35SNR() {
    return snrs_above_30_below_35;
}

/**
 * Set the number of SNR values for between 30 and 35.
 * @param snr_values The values
 */
void Stream::setBetween30And35SNR(int snr_values) {
    snrs_above_30_below_35 = snr_values;
}