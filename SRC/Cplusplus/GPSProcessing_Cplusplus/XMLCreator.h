/* 
 * File:   XMLCreator.h
 * Author: jee22
 *
 * Created on 21 March 2014, 02:30
 */

#ifndef XMLCREATOR_H
#define	XMLCREATOR_H

#include <iostream>
#include <string>
#include <time.h>
#include "Stream.h"
#include "FileHandler.h"

using namespace std;

class XMLCreator {
public:
    XMLCreator();
    XMLCreator(const XMLCreator& orig);
    virtual ~XMLCreator();
    
    void startXML();
    void endXML();
    string tagDateAndTime(time_t date_and_time);
    string tagWaypoint(double latitude, double longitude);
    string tagElevation(double elevation);
    string tagEndWaypoint();
    
    void outputStream(Stream * streamer);
private:
    FileHandler * file_handler;
};

#endif	/* XMLCREATOR_H */

