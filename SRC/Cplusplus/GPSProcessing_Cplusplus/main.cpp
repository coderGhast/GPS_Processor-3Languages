/* 
 * File:   main.cpp
 * Author: jee22
 *
 * The main class that calls to begin the
 * application, using DataCoordinator.
 * 
 * Created on 21 March 2014, 02:03
 */

#include <cstdlib>
#include <stdio.h>
#include "DataCoordinator.h"

using namespace std;

/*
 * 
 */
int main(int argc, char** argv) {
    DataCoordinator * data_coordinator = new DataCoordinator();
    data_coordinator->runApp();
    delete(data_coordinator);
    return 0;
}

