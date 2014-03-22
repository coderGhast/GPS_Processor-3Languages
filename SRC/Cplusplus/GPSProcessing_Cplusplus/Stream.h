/* 
 * File:   Stream.h
 * Author: jee22
 *
 * Created on 21 March 2014, 02:29
 */

#ifndef STREAM_H
#define	STREAM_H

#include <time.h>
#include <string>
#include <iostream>

using namespace std;

class Stream {
public:
    Stream();
    Stream(const Stream& orig);
    virtual ~Stream();

    double getLatitude();
    void setLatitude(string string_lat, string lat_facing);
    void setLatitudeVal(double val);
    double getLongitude();
    void setLongitude(string string_lng, string lng_facing);
    void setLongitudeVal(double val);
    double getElevation();
    void setElevation(string string_elevation);
    time_t getDateAndTime();
    void setDateAndTime(string date_string, string time_string);
    void updateTime(string time_string);
    void calcSNRAmounts(string snr);
    int get35AndAboveSNR();
    void set35AndAboveSNR(int snr_values);
    int getBetween30And35SNR();
    void setBetween30And35SNR(int snr_values);
private:
    time_t date_and_time;
    double latitude;
    double longitude;
    double elevation;
    int snrs_above_30_below_35;
    int snrs_above_35;
};

#endif	/* STREAM_H */

