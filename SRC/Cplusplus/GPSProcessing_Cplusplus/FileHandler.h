/* 
 * File:   FileHandler.h
 * Author: jee22
 *
 * Created on 21 March 2014, 02:29
 */

#ifndef FILEHANDLER_H
#define	FILEHANDLER_H

#include <iostream>
#include <fstream>
#include <string>

using namespace std;

class FileHandler {
public:
    FileHandler(string file_name, char operation);
    FileHandler(const FileHandler& orig);
    virtual ~FileHandler();
    
    void openReadFile(string file_name);
    void openInitialWrite();
    void openWriteFile();
    string readFile();
    void writeFile(string xml);
    void closeReadFile();
    void closeWriteFile();
private:
    ifstream infile;
    ofstream outfile;
    string out_file_name;
};

#endif	/* FILEHANDLER_H */