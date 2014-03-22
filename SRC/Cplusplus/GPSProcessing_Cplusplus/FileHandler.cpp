/* 
 * File:   FileHandler.cpp
 * Author: jee22
 * 
 * Created on 21 March 2014, 02:29
 */

#include <iostream>
#include <fstream>
#include <string>
#include "FileHandler.h"

using namespace std;

FileHandler::FileHandler(string file_name, char operation) {
    if(operation == 'r'){
        openReadFile(file_name);
    } else {
        out_file_name = file_name;
    }
}

FileHandler::FileHandler(const FileHandler& orig) {
}

FileHandler::~FileHandler() {
}

void FileHandler::openReadFile(string file_name){
    infile.open(file_name.c_str());
}

void FileHandler::openInitialWrite(){
    outfile.open(out_file_name.c_str());
}

void FileHandler::openWriteFile(){
    outfile.open(out_file_name.c_str(), ios::app);
}

void FileHandler::writeFile(string xml){
    outfile<<xml;
}

string FileHandler::readFile() {
        string sentence;
	
        if(!infile.eof())
        {
	        getline(infile,sentence); // Saves the line in sentence
        } else {
            sentence = "end";
        }
        return sentence;
}

void FileHandler::closeReadFile(){
    infile.close();
}

void FileHandler::closeWriteFile(){
    outfile.close();
}

