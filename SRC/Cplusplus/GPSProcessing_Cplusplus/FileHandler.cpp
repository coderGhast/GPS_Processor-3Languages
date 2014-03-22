/* 
 * File:   FileHandler.cpp
 * Author: jee22
 * 
 * Reads in and Writes out to files
 * for the application.
 * 
 * Created on 21 March 2014, 02:29
 */

#include <iostream>
#include <fstream>
#include <string>
#include "FileHandler.h"

using namespace std;

/**
 * Constructor takes in a file name
 * and the particular operation to
 * be performed with the file.
 * @param file_name File name
 * @param operation What to do with it.
 */
FileHandler::FileHandler(string file_name, char operation) {
    /* If the file is to be read in, open it now. */
    if (operation == 'r') {
        openReadFile(file_name);
    } else {
        /* Else, just store the file name for later use. */
        out_file_name = file_name;
    }
}

FileHandler::FileHandler(const FileHandler& orig) {
}

FileHandler::~FileHandler() {
}

/* Open a file to read. */
void FileHandler::openReadFile(string file_name) {
    infile.open(file_name.c_str());
}

/* Open a file to write out to for the first time. */
void FileHandler::openInitialWrite() {
    outfile.open(out_file_name.c_str());
}

/* Open a file to write out to through appending to the end of the file. */
void FileHandler::openWriteFile() {
    outfile.open(out_file_name.c_str(), ios::app);
}

/* Write out a string to the file. */
void FileHandler::writeFile(string xml) {
    cout<<"Outputting to file"<<endl;
    outfile << xml;
}

/* Read in a line from the file opened in the constructor. */
string FileHandler::readFile() {
    string sentence;

    /* If we haven't yet reached the end of the file. */
    if (!infile.eof()) {
        /* Read in a line and save it in the sentence. */
        getline(infile, sentence);
    } else {
        /* If there are no more lines left to read, put end in the string
         to be returned and dealt with in DataCoordinator. */
        sentence = "end";
    }
    return sentence;
}

/* Close the file that has been read from. */
void FileHandler::closeReadFile() {
    infile.close();
}

/* Close the file being written to. */
void FileHandler::closeWriteFile() {
    outfile.close();
}

